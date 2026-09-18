package org.lpv.manager;

import java.sql.SQLException;
import java.util.List;

import org.lpv.dao.DetalleVentaDAO;
import org.lpv.dao.VentaDAO;
import org.lpv.dao.impl.DetalleVentaDAOImpl;
import org.lpv.dao.impl.VentaDAOImpl;
import org.lpv.model.DetalleVenta;
import org.lpv.model.Venta;

public class ComprobanteManager {

    private final VentaDAO ventaDAO;
    private final DetalleVentaDAO detalleVentaDAO;

    public ComprobanteManager() {

        ventaDAO = new VentaDAOImpl();
        detalleVentaDAO = new DetalleVentaDAOImpl();
    }

    public Venta obtenerVenta(
            int idVenta
    ) throws SQLException {

        if (idVenta <= 0) {
            throw new SQLException(
                    "El ID de la venta no es válido."
            );
        }

        Venta venta =
                ventaDAO.buscarPorId(idVenta);

        if (venta == null) {
            throw new SQLException(
                    "No se encontró la venta con ID: "
                    + idVenta
            );
        }

        return venta;
    }

    public List<DetalleVenta> obtenerDetalles(
            int idVenta
    ) throws SQLException {

        if (idVenta <= 0) {
            throw new SQLException(
                    "El ID de la venta no es válido."
            );
        }

        List<DetalleVenta> detalles =
                detalleVentaDAO.buscarPorVenta(
                        idVenta
                );

        if (detalles.isEmpty()) {
            throw new SQLException(
                    "La venta no tiene detalles registrados."
            );
        }

        return detalles;
    }
}