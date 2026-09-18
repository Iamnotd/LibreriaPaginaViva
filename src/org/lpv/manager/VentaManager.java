package org.lpv.manager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.lpv.dao.DetalleVentaDAO;
import org.lpv.dao.LibroDAO;
import org.lpv.dao.VentaDAO;
import org.lpv.dao.impl.DetalleVentaDAOImpl;
import org.lpv.dao.impl.LibroDAOImpl;
import org.lpv.dao.impl.VentaDAOImpl;
import org.lpv.model.CarritoItem;
import org.lpv.model.DetalleVenta;
import org.lpv.model.Libro;
import org.lpv.model.Venta;
import org.lpv.util.Conexion;

public class VentaManager {

    private final VentaDAO ventaDAO;
    private final DetalleVentaDAO detalleVentaDAO;
    private final LibroDAO libroDAO;

    public VentaManager() {

        ventaDAO = new VentaDAOImpl();
        detalleVentaDAO = new DetalleVentaDAOImpl();
        libroDAO = new LibroDAOImpl();
    }

    public int registrarVenta(
            Venta venta,
            List<CarritoItem> carrito
    ) throws SQLException {

        if (venta == null) {
            throw new SQLException(
                    "La venta no puede ser nula."
            );
        }

        if (carrito == null || carrito.isEmpty()) {
            throw new SQLException(
                    "El carrito está vacío."
            );
        }

        Connection conexion =
                Conexion.getInstancia().getConexion();

        boolean estadoAnterior =
                conexion.getAutoCommit();

        try {

            conexion.setAutoCommit(false);

            validarStock(carrito);

            calcularTotales(
                    venta,
                    carrito
            );

            int idVenta =
                    ventaDAO.registrarVenta(
                            venta,
                            conexion
                    );

            for (CarritoItem item : carrito) {

                BigDecimal precio =
                        item.getLibro().getPrecio();

                BigDecimal subtotal =
                        precio.multiply(
                                BigDecimal.valueOf(
                                        item.getCantidad()
                                )
                        );

                DetalleVenta detalle =
                        new DetalleVenta();

                detalle.setIdVenta(idVenta);

                detalle.setIsbn(
                        item.getLibro().getIsbn()
                );

                detalle.setCantidad(
                        item.getCantidad()
                );

                detalle.setPrecioUnitario(
                        precio
                );

                detalle.setSubtotal(
                        subtotal
                );

                detalleVentaDAO.registrarDetalle(
                        detalle,
                        conexion
                );

                libroDAO.actualizarStock(
                        item.getLibro().getIsbn(),
                        item.getCantidad(),
                        conexion
                );
            }

            conexion.commit();

            return idVenta;

        } catch (SQLException e) {

            try {
                conexion.rollback();
            } catch (SQLException rollbackError) {
                e.addSuppressed(rollbackError);
            }

            throw e;

        } finally {

            conexion.setAutoCommit(
                    estadoAnterior
            );
        }
    }

    private void validarStock(
            List<CarritoItem> carrito
    ) throws SQLException {

        for (CarritoItem item : carrito) {

            if (item == null) {
                throw new SQLException(
                        "El carrito contiene un elemento inválido."
                );
            }

            if (item.getLibro() == null) {
                throw new SQLException(
                        "El carrito contiene un libro inválido."
                );
            }

            if (item.getCantidad() <= 0) {
                throw new SQLException(
                        "La cantidad debe ser mayor que cero."
                );
            }

            Libro libro =
                    libroDAO.buscarPorIsbn(
                            item.getLibro().getIsbn()
                    );

            if (libro == null) {
                throw new SQLException(
                        "El libro no existe: "
                        + item.getLibro().getIsbn()
                );
            }

            if (!libro.isActivo()) {
                throw new SQLException(
                        "El libro está inactivo: "
                        + libro.getTitulo()
                );
            }

            if (libro.getStockActual()
                    < item.getCantidad()) {

                throw new SQLException(
                        "Stock insuficiente para: "
                        + libro.getTitulo()
                );
            }
        }
    }

    private void calcularTotales(
            Venta venta,
            List<CarritoItem> carrito
    ) {

        BigDecimal subtotal =
                BigDecimal.ZERO;

        for (CarritoItem item : carrito) {

            BigDecimal subtotalItem =
                    item.getLibro()
                            .getPrecio()
                            .multiply(
                                    BigDecimal.valueOf(
                                            item.getCantidad()
                                    )
                            );

            subtotal =
                    subtotal.add(subtotalItem);
        }

        BigDecimal descuento =
                venta.getDescuento();

        if (descuento == null) {
            descuento = BigDecimal.ZERO;
        }

        BigDecimal total =
                subtotal.subtract(descuento);

        if (total.compareTo(
                BigDecimal.ZERO
        ) < 0) {

            total = BigDecimal.ZERO;
        }

        venta.setSubtotal(subtotal);
        venta.setDescuento(descuento);
        venta.setTotal(total);
    }
}