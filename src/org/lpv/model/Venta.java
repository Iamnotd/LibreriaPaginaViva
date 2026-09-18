package org.lpv.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Venta {

    private int idVenta;
    private BigDecimal subtotal;
    private LocalDateTime fechaVenta;
    private BigDecimal total;
    private BigDecimal descuento;
    private Integer usuarioAutorizaDescuento;
    private String estado;
    private Integer idUsuario;
    private LocalDateTime fechaAnulacion;
    private Integer usuarioAnulacion;
    private String motivoAnulacion;
    private Long cuiCliente;

    public Venta() {
    }

    public Venta(
            int idVenta,
            BigDecimal subtotal,
            LocalDateTime fechaVenta,
            BigDecimal total,
            BigDecimal descuento,
            Integer usuarioAutorizaDescuento,
            String estado,
            Integer idUsuario,
            LocalDateTime fechaAnulacion,
            Integer usuarioAnulacion,
            String motivoAnulacion,
            Long cuiCliente
    ) {
        this.idVenta = idVenta;
        this.subtotal = subtotal;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.descuento = descuento;
        this.usuarioAutorizaDescuento = usuarioAutorizaDescuento;
        this.estado = estado;
        this.idUsuario = idUsuario;
        this.fechaAnulacion = fechaAnulacion;
        this.usuarioAnulacion = usuarioAnulacion;
        this.motivoAnulacion = motivoAnulacion;
        this.cuiCliente = cuiCliente;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public LocalDateTime getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDateTime fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDescuento() {
        return descuento;
    }

    public void setDescuento(BigDecimal descuento) {
        this.descuento = descuento;
    }

    public Integer getUsuarioAutorizaDescuento() {
        return usuarioAutorizaDescuento;
    }

    public void setUsuarioAutorizaDescuento(
            Integer usuarioAutorizaDescuento
    ) {
        this.usuarioAutorizaDescuento =
                usuarioAutorizaDescuento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public LocalDateTime getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(
            LocalDateTime fechaAnulacion
    ) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public Integer getUsuarioAnulacion() {
        return usuarioAnulacion;
    }

    public void setUsuarioAnulacion(
            Integer usuarioAnulacion
    ) {
        this.usuarioAnulacion = usuarioAnulacion;
    }

    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }

    public void setMotivoAnulacion(
            String motivoAnulacion
    ) {
        this.motivoAnulacion = motivoAnulacion;
    }

    public Long getCuiCliente() {
        return cuiCliente;
    }

    public void setCuiCliente(Long cuiCliente) {
        this.cuiCliente = cuiCliente;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "idVenta=" + idVenta +
                ", subtotal=" + subtotal +
                ", fechaVenta=" + fechaVenta +
                ", total=" + total +
                ", descuento=" + descuento +
                ", usuarioAutorizaDescuento="
                + usuarioAutorizaDescuento +
                ", estado='" + estado + '\'' +
                ", idUsuario=" + idUsuario +
                ", fechaAnulacion=" + fechaAnulacion +
                ", usuarioAnulacion=" + usuarioAnulacion +
                ", motivoAnulacion='" + motivoAnulacion + '\'' +
                ", cuiCliente=" + cuiCliente +
                '}';
    }
}