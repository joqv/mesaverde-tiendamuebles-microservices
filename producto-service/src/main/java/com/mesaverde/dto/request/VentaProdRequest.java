package com.mesaverde.dto.request;

import java.util.List;

public class VentaProdRequest {
	
	private List<ProductoRequest> productos;
    private String fecha;
    private Double total;

    // Getters y setters
    public List<ProductoRequest> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoRequest> productos) {
        this.productos = productos;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
	
}
