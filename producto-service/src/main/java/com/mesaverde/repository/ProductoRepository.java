package com.mesaverde.repository;

import com.mesaverde.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.math.BigDecimal;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Buscar x nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    //de venta
    
    @Procedure(procedureName = "sp_descontar_producto")
    void descontarProducto(Integer p_id_producto, Integer p_cantidad, BigDecimal p_precio_unitario);

}
