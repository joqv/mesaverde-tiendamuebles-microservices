package com.mesaverde.service;

import com.mesaverde.entity.DetalleVenta;
import com.mesaverde.entity.Producto;
import com.mesaverde.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> obtenerProducto(Integer id) {
        return productoRepository.findById(id);
    }

    public Producto guardarProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto actualizarProducto(Integer id, Producto producto) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setNombre(producto.getNombre());
                    p.setPrecio(producto.getPrecio());
                    p.setTipo(producto.getTipo());
                    p.setStock(producto.getStock());
                    p.setDescripcion(producto.getDescripcion());
                    p.setImagen(producto.getImagen());
                    p.setCategoria(producto.getCategoria());
                    return productoRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
    }

    public void eliminarProducto(Integer id) {
        productoRepository.deleteById(id);
    }

    public List<Producto> buscarXNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // de venta
    
    @Transactional
    public void descProducto(List<DetalleVenta> detalles) {
       

        // 2. Procesar cada detalle
        for (DetalleVenta detalle : detalles) {
            // 2.1 Descontar stock
        	productoRepository.descontarProducto(
                detalle.getProductoId(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario()
            );
        }
    }



}
