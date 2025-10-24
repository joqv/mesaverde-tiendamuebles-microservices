package com.mesaverde.client;

import com.mesaverde.entity.Cliente;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "cliente-service", url = "http://api-gateway:8080/cliente-service")
public interface ClienteClient {

    @GetMapping("/cliente/listartodo")
    List<Cliente> getClientes();

}
