package com.mesaverde.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class VentaMessage {
    private Integer ventaId;
    private BigDecimal total;
    private LocalDateTime fecha;
    public VentaMessage() {
    }

    public VentaMessage(Integer ventaId, BigDecimal total, LocalDateTime fecha) {
        this.ventaId = ventaId;
        this.total = total;
        this.fecha = fecha;
    }

    public Integer getVentaId() {
        return ventaId;
    }

    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "VentaMessage{" +
                "ventaId=" + ventaId +
                ", total=" + total +
                ", fecha=" + fecha +
                '}';
    }
}
