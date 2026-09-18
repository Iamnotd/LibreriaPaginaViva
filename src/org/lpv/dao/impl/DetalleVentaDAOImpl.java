package org.lpv.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lpv.dao.DetalleVentaDAO;
import org.lpv.model.DetalleVenta;

public class DetalleVentaDAOImpl
        implements DetalleVentaDAO {

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

            ps.setInt(
                    1,
                    detalle.getIdVenta()
            );

            ps.setString(
                    2,
                    detalle.getIsbn()
            );

            ps.setInt(
                    3,
                    detalle.getCantidad()
            );

            ps.setBigDecimal(
                    4,
                    detalle.getPrecioUnitario()
            );

            ps.setBigDecimal(
                    5,
                    detalle.getSubtotal()
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se pudo registrar el detalle de la venta."
                );
            }
        }
    }

    @Override
    public List<DetalleVenta> buscarPorVenta(
            int idVenta
    ) throws SQLException {

        List<DetalleVenta> detalles =
                new ArrayList<>();

        String sql = """
                SELECT
                    id_detalle,
                    id_venta,
                    isbn,
                    cantidad,
                    precio_unitario,
                    subtotal
                FROM detalle_venta
                WHERE id_venta = ?
                ORDER BY id_detalle
                """;

        Connection conexion =
                org.lpv.util.Conexion
                        .getInstancia()
                        .getConexion();

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setInt(1, idVenta);

            try (ResultSet rs =
                    ps.executeQuery()) {

                while (rs.next()) {

                    DetalleVenta detalle =
                            new DetalleVenta();

                    detalle.setIdDetalle(
                            rs.getInt(
                                    "id_detalle"
                            )
                    );

                    detalle.setIdVenta(
                            rs.getInt(
                                    "id_venta"
                            )
                    );

                    detalle.setIsbn(
                            rs.getString(
                                    "isbn"
                            )
                    );

                    detalle.setCantidad(
                            rs.getInt(
                                    "cantidad"
                            )
                    );

                    detalle.setPrecioUnitario(
                            rs.getBigDecimal(
                                    "precio_unitario"
                            )
                    );

                    detalle.setSubtotal(
                            rs.getBigDecimal(
                                    "subtotal"
                            )
                    );

                    detalles.add(detalle);
                }
            }
        }

        return detalles;
    }
}
