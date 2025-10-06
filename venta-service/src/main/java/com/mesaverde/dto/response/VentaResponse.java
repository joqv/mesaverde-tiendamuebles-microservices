package com.mesaverde.dto.response;

import com.mesaverde.entity.Cliente;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
public class VentaResponse {

    private String nombreCliente;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private BigDecimal total;
    private List<Cliente> clientes;
}
