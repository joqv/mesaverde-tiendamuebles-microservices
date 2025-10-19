package com.mesaverde.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import com.mesaverde.dto.request.VentaRequest;
import com.mesaverde.entity.DetalleVenta;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name = "producto-service", url = "http://localhost:8080/producto-service")
public interface ProductoClient {
	
	@PostMapping("/productos/descProducto")
    ResponseEntity<?> descontarProducto(@RequestBody List<DetalleVenta> detalles);

}
