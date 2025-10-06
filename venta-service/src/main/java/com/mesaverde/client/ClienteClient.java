package com.mesaverde.client;

import com.mesaverde.entity.Cliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "cliente-service", url = "http://localhost:8081")
public interface ClienteClient {

    @GetMapping("/clientes")
    List<Cliente> getClientes();

}
