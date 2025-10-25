package com.mesaverde.service;

import com.error.springerrorhandler.exceptions.BusinessException;
import com.mesaverde.client.ClienteClient;
import com.mesaverde.client.ProductoClient;
import com.mesaverde.dto.response.VentaResponse;
import com.mesaverde.entity.Auditoria;
import com.mesaverde.entity.DetalleVenta;
import com.mesaverde.entity.Producto;
import com.mesaverde.entity.Venta;
import com.mesaverde.repository.VentaRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteClient clienteClient;
    private final ProductoClient productoClient;

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

            // 2.2 Registrar detalle
            ventaRepository.registrarDetalleVenta(
                    ventaId,
                    detalle.getProductoId(),
                    detalle.getCantidad(),
                    detalle.getPrecioUnitario()
            );
        }

        try {
            Optional<Venta> ventaNew = ventaRepository.findById(ventaId);

            Auditoria auditoria = new Auditoria();
            auditoria.setVentaid(ventaId);
            auditoria.setTotal(ventaNew.get().getTotal());
            auditoria.setFecha(LocalDateTime.now());

            messageProducerService.sendMessage(auditoria);
            System.out.println("✅ Auditoría ASÍNCRONA: Venta ID " + ventaId + " enviada a RabbitMQ.");

        } catch (Exception e) {
            System.err.println("❌ ADVERTENCIA: Falló el envío del mensaje de auditoría a RabbitMQ. La venta continúa. Error: " + e.getMessage());
        }
    }
}