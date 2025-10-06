package com.mesaverde.service;

import com.error.springerrorhandler.exceptions.BusinessException;
import com.mesaverde.client.ClienteClient;
import com.mesaverde.dto.response.VentaResponse;
import com.mesaverde.entity.Venta;
import com.mesaverde.repository.VentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VentaService {

    private final VentaRepository ventaRepository;
    private final ClienteClient clienteClient;

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
}
