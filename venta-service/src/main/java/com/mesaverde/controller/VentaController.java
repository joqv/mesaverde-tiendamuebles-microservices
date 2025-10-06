package com.mesaverde.controller;

import com.mesaverde.dto.response.VentaResponse;
import com.mesaverde.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @GetMapping("/{id}")
    public VentaResponse obtenerVenta(@PathVariable Integer id) {

        return ventaService.obtenerVenta(id);
    }
}
