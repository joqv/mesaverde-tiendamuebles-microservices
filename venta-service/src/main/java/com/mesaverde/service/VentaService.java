package com.mesaverde.service;

import com.error.springerrorhandler.exceptions.BusinessException;
import com.mesaverde.client.ClienteClient;
import com.mesaverde.client.ProductoClient;
import com.mesaverde.dto.response.VentaResponse;
import com.mesaverde.entity.DetalleVenta;
import com.mesaverde.entity.Producto;
import com.mesaverde.entity.Venta;
import com.mesaverde.repository.VentaRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteClient clienteClient;
    private final ProductoClient productoClient;
    
    public List<Producto> todosProductos(){
    	return ventaRepository.todosProductos();
    }

    public VentaResponse obtenerVenta(Integer id) {

        Venta venta = ventaRepository.findById(id).orElseThrow(() ->
                new BusinessException("venta.not.found"));

        VentaResponse response = VentaResponse.builder()
                //.nombreCliente(venta.getCliente().getNombre())
                .nombreUsuario(venta.getUsuario().getNombre())
                .fecha(venta.getFecha())
                .total(venta.getTotal())
                .clientes(clienteClient.getClientes())
                .build();

        return response;
    }
    
    @Transactional
    @CircuitBreaker(name = "procesarVentaRepository", fallbackMethod = "fallbackProcesarVenta")
	@Retry(name = "procesarVentaRepository")
    public void procesarVenta(Venta venta, List<DetalleVenta> detalles) {
        // 1. Registrar la venta
    	Integer ventaId = ventaRepository.registrarVenta(
    		    venta.getUsuario() != null ? venta.getUsuario().getId() : null,
    		    venta.getTotal()
    	);
        venta.setId(ventaId);

        // 2. Procesar cada detalle
        
        productoClient.descontarProducto(detalles);
        
        for (DetalleVenta detalle : detalles) {
            // 2.1 Descontar stock
            
        	//ventaRepository.descontarProducto(
            //    detalle.getProductoId(),
             //   detalle.getCantidad(),
             //   detalle.getPrecioUnitario(),
             //   venta.getUsuario() != null ? venta.getUsuario().getNombre() : "sistema"
            //);
        	
        	

            // 2.2 Registrar detalle
            ventaRepository.registrarDetalleVenta(
                ventaId,
                detalle.getProductoId(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario()
            );
        }
    }

    //Ingresa al fallback cuando se termina los reintentos
    public void fallbackProcesarVenta(Venta venta, List<DetalleVenta> detalles,Throwable ex) {
        throw new RuntimeException("No se puede realizar la venta en este momento. Intentalo más tarde.");

    }
    
}
