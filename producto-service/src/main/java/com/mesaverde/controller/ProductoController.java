package com.mesaverde.controller;

import com.mesaverde.dto.request.VentaProdRequest;
import com.mesaverde.entity.DetalleVenta;
import com.mesaverde.entity.Producto;
import com.mesaverde.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;


    @GetMapping
    public ResponseEntity<List<Producto>> listaProductos() {
        return ResponseEntity.ok(productoService.listarProductos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerById(@PathVariable Integer id) {
        return productoService.obtenerProducto(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Integer id, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Integer id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/buscar/nombre")
    public ResponseEntity<List<Producto>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(productoService.buscarXNombre(nombre));
    }
    
    // de venta
	
	@PostMapping("/descProducto")
	public void descProducto(@RequestBody List<DetalleVenta> detalles) {
		
		/*
		List<DetalleVenta> detalles = ventaProdRequest.getProductos().stream().map(p -> {
		
            DetalleVenta detalle = new DetalleVenta();
            detalle.setProductoId(p.getId());
            detalle.setCantidad(p.getCantidad());
            detalle.setPrecioUnitario(BigDecimal.valueOf(p.getPrecio_unitario()));
            return detalle;
        }).collect(Collectors.toList());

		 */

		productoService.descProducto(detalles);
	}
    
}
