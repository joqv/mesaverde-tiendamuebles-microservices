package com.mesaverde.service;

import com.error.springerrorhandler.exceptions.BusinessException;
import com.mesaverde.client.ClienteClient;
import com.mesaverde.client.ProductoClient;
import com.mesaverde.dto.request.VentaRequest;
import com.mesaverde.dto.response.VentaResponse;
import com.mesaverde.entity.Auditoria;
import com.mesaverde.entity.DetalleVenta;
import com.mesaverde.entity.Producto;
import com.mesaverde.entity.Usuario;
import com.mesaverde.entity.Venta;
import com.mesaverde.repository.VentaRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;



import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteClient clienteClient;
    private final ProductoClient productoClient;
    private final UserService userService;

    @Autowired
    private MessageProducerService messageProducerService;
    
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
    public ResponseEntity<Map<String, Serializable>> procesarVenta(VentaRequest ventaRequest, List<DetalleVenta> detalles) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("---- AUTH DEBUG ----");
        System.out.println("Auth class: " + auth.getClass().getName());
        System.out.println("Principal class: " + auth.getPrincipal().getClass().getName());
        System.out.println("Principal toString: " + auth.getPrincipal());
        System.out.println("Authorities: " + auth.getAuthorities());
        System.out.println("Is authenticated: " + auth.isAuthenticated());
        System.out.println("--------------------");

        //Usuario usuario = (Usuario) auth.getPrincipal();
        //Integer idUsuario = usuario.getId();

        Venta venta = new Venta();
        //Fecha
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime fecha = LocalDateTime.parse(ventaRequest.getFecha(), formatter);
        venta.setFecha(fecha);

        Usuario user=userService.obtenerUsuario(ventaRequest.getUsuario());

        Usuario usuario = new Usuario();
        usuario.setId(user.getId()); // o buscarlo en la BD si existe
        venta.setUsuario(usuario);

        //total
        BigDecimal total = BigDecimal.valueOf(ventaRequest.getTotal());
        venta.setTotal(total);


        // 1. Registrar la venta
        ventaRepository.registrarVenta(
        		venta.getUsuario().getId(),
                //venta.getUsuario() != null ? venta.getUsuario().getId() : null,
                venta.getTotal()
        );
        Integer ventaId = ventaRepository.getLastVentaId();

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

        Optional<Venta> ventaAuditoria = ventaRepository.findById(ventaId);

        Auditoria auditoria = new Auditoria();

        auditoria.setVentaId(ventaId);
        auditoria.setTotal(ventaAuditoria.get().getTotal());
        auditoria.setFecha(Instant.now());

        messageProducerService.sendMessage(auditoria);
        //return "Se ha procesado la venta. Total: " + ventaAuditoria.get().getTotal();

        Map<String, Serializable> body = Map.of(
                "mensaje", "Se ha procesado la venta. Total: " + ventaAuditoria.get().getTotal(),
                "total", ventaAuditoria.get().getTotal(),
                "ventaId", ventaId
        );

        return ResponseEntity.ok(body);

    }

    //Ingresa al fallback cuando se termina los reintentos
    //public String fallbackProcesarVenta(Venta venta, List<DetalleVenta> detalles,Throwable ex) {
    //    throw new RuntimeException("No se puede realizar la venta en este momento. Intentalo más tarde.");
    //    return "No se puede procesar la venta en este momento. Intentalo más tarde.";
    //}

    public ResponseEntity<Map<String, Serializable>> fallbackProcesarVenta(VentaRequest ventaRequest, List<DetalleVenta> detalles, Throwable ex) {
        Map<String, Serializable> body = Map.of(
                "mensaje", "CB. No se puede procesar la venta en este momento. Inténtalo más tarde.",
                "error", ex.getMessage()
        );
        return ResponseEntity.status(503).body(body);
    }
    
}
