package org.lpv.model;

import java.math.BigDecimal;

public class CarritoItem {

    private Libro libro;
    private int cantidad;

    public CarritoItem(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }

    public Libro getLibro() {
        return libro;
    }

    public void setLibro(Libro libro) {
        this.libro = libro;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubtotal() {
        return libro.getPrecio()
                .multiply(BigDecimal.valueOf(cantidad));
    }

    @Override
    public String toString() {
        return "CarritoItem{" +
                "isbn='" + libro.getIsbn() + '\'' +
                ", titulo='" + libro.getTitulo() + '\'' +
                ", cantidad=" + cantidad +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}