package org.lpv.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.lpv.dao.VentaDAO;
import org.lpv.model.Venta;

public class VentaDAOImpl implements VentaDAO {

    @Override
    public int registrarVenta(
            Venta venta,
            Connection conexion
    ) throws SQLException {

        String sql = """
                INSERT INTO ventas (
                    subtotal,
                    total,
                    descuento,
                    usuario_autoriza_descuento,
                    estado,
                    id_usuario,
                    cui_cliente
                )
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )) {

            ps.setBigDecimal(
                    1,
                    venta.getSubtotal()
            );

            ps.setBigDecimal(
                    2,
                    venta.getTotal()
            );

            ps.setBigDecimal(
                    3,
                    venta.getDescuento()
            );

            if (venta.getUsuarioAutorizaDescuento() != null) {
                ps.setInt(
                        4,
                        venta.getUsuarioAutorizaDescuento()
                );
            } else {
                ps.setNull(
                        4,
                        java.sql.Types.INTEGER
                );
            }

            ps.setString(
                    5,
                    venta.getEstado()
            );

            if (venta.getIdUsuario() != null) {
                ps.setInt(
                        6,
                        venta.getIdUsuario()
                );
            } else {
                ps.setNull(
                        6,
                        java.sql.Types.INTEGER
                );
            }

            if (venta.getCuiCliente() != null) {
                ps.setLong(
                        7,
                        venta.getCuiCliente()
                );
            } else {
                ps.setNull(
                        7,
                        java.sql.Types.BIGINT
                );
            }

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se pudo registrar la venta."
                );
            }

            try (ResultSet rs =
                    ps.getGeneratedKeys()) {

                if (rs.next()) {

                    int idVenta = rs.getInt(1);

                    venta.setIdVenta(idVenta);

                    return idVenta;
                }
            }

            throw new SQLException(
                    "No se obtuvo el ID de la venta."
            );
        }
    }
}