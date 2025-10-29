package com.mesaverde.repository;

import com.mesaverde.entity.Producto;
import com.mesaverde.entity.Venta;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface VentaRepository extends JpaRepository<Venta, Integer> {

    @Query(value = "SELECT * FROM productos", nativeQuery = true)
    List<Producto> todosProductos();

    @Procedure(procedureName = "sp_descontar_producto")
    void descontarProducto(Integer p_id_producto, Integer p_cantidad, BigDecimal p_precio_unitario);

    //@Modifying
    //@Transactional
    //@Query(value = "CALL sp_registrar_venta(:usuarioId, :total)", nativeQuery = true)
    //Integer registrarVenta(@Param("usuarioId") Integer usuarioId, @Param("total") BigDecimal total);

    @Modifying
    @Transactional
    @Query(value = "CALL sp_registrar_venta(:usuarioId, :total)", nativeQuery = true)
    Integer registrarVenta(@Param("usuarioId") Integer usuarioId, @Param("total") BigDecimal total);


    @Procedure(procedureName = "sp_registrar_detalle_venta")
    void registrarDetalleVenta(Integer ventaId, Integer productoId, Integer cantidad, BigDecimal precioUnitario);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Integer getLastVentaId();
}
