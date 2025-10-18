package com.mesaverde.repository;

import com.mesaverde.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Buscar x nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);



}
