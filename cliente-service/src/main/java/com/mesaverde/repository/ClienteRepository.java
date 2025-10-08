package com.mesaverde.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mesaverde.entity.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
}
