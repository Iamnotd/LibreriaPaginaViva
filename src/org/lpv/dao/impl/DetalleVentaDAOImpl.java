package org.lpv.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.lpv.dao.DetalleVentaDAO;
import org.lpv.model.DetalleVenta;

public class DetalleVentaDAOImpl implements DetalleVentaDAO {

    @Override
    public void registrarDetalle(
            DetalleVenta detalle,
            Connection conexion
    ) throws SQLException {

        String sql = """
                INSERT INTO detalle_venta (
                    id_venta,
                    isbn,
                    cantidad,
                    precio_unitario,
                    subtotal
                )
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setInt(1, detalle.getIdVenta());
            ps.setString(2, detalle.getIsbn());
            ps.setInt(3, detalle.getCantidad());
            ps.setBigDecimal(4, detalle.getPrecioUnitario());
            ps.setBigDecimal(5, detalle.getSubtotal());

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se pudo registrar el detalle de la venta."
                );
            }
        }
    }
}