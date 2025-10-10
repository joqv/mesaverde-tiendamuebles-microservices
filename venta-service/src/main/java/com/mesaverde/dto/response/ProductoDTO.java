package com.mesaverde.dto.response;

import java.math.BigDecimal;

import com.mesaverde.entity.Producto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoDTO {
    private Integer id;
    private String nombre;
    private BigDecimal precio;
    private String tipo;
    private int stock;
    private String descripcion;
    private String imagen;
    private String categoriaNombre;

    public ProductoDTO(Producto producto) {
        this.id = producto.getId();
        this.nombre = producto.getNombre();
        this.precio = producto.getPrecio();
        this.tipo = producto.getTipo();
        this.stock = producto.getStock();
        this.descripcion = producto.getDescripcion();
        this.imagen = producto.getImagen();
        this.categoriaNombre = producto.getCategoria() != null ? producto.getCategoria().getNombre() : null;
    }
    
    public ProductoDTO() {
    }

}

