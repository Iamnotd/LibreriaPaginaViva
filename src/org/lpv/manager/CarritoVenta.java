package org.lpv.manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.lpv.model.CarritoItem;
import org.lpv.model.Libro;

public class CarritoVenta {

    private final List<CarritoItem> items;

    public CarritoVenta() {
        items = new ArrayList<>();
    }

    public void agregarProducto(Libro libro, int cantidad) {

        if (libro == null) {
            throw new IllegalArgumentException(
                    "El libro no puede ser nulo."
            );
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero."
            );
        }

        for (CarritoItem item : items) {

            if (item.getLibro()
                    .getIsbn()
                    .equals(libro.getIsbn())) {

                int nuevaCantidad =
                        item.getCantidad() + cantidad;

                item.setCantidad(nuevaCantidad);
                return;
            }
        }

        items.add(
                new CarritoItem(libro, cantidad)
        );
    }

    public void eliminarProducto(String isbn) {

        items.removeIf(
                item -> item.getLibro()
                        .getIsbn()
                        .equals(isbn)
        );
    }

    public void actualizarCantidad(
            String isbn,
            int cantidad
    ) {

        if (cantidad <= 0) {
            eliminarProducto(isbn);
            return;
        }

        for (CarritoItem item : items) {

            if (item.getLibro()
                    .getIsbn()
                    .equals(isbn)) {

                item.setCantidad(cantidad);
                return;
            }
        }
    }

    public List<CarritoItem> getItems() {
        return new ArrayList<>(items);
    }

    public BigDecimal calcularSubtotal() {

        BigDecimal subtotal = BigDecimal.ZERO;

        for (CarritoItem item : items) {
            subtotal = subtotal.add(
                    item.getSubtotal()
            );
        }

        return subtotal;
    }

    public BigDecimal calcularTotal() {
        return calcularSubtotal();
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public int cantidadProductos() {
        return items.size();
    }

    public void limpiar() {
        items.clear();
    }
}