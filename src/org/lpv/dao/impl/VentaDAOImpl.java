package org.lpv.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import org.lpv.dao.VentaDAO;
import org.lpv.model.Venta;
import org.lpv.util.Conexion;

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

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {

                throw new SQLException(
                        "No se pudo registrar la venta."
                );
            }

            try (ResultSet rs =
                    ps.getGeneratedKeys()) {

                if (rs.next()) {

                    int idVenta =
                            rs.getInt(1);

                    venta.setIdVenta(idVenta);

                    return idVenta;
                }
            }

            throw new SQLException(
                    "No se obtuvo el ID de la venta."
            );
        }
    }

    @Override
    public Venta buscarPorId(
            int idVenta
    ) throws SQLException {

        String sql = """
                SELECT
                    id_venta,
                    subtotal,
                    fecha_venta,
                    total,
                    descuento,
                    usuario_autoriza_descuento,
                    estado,
                    id_usuario,
                    fecha_anulacion,
                    usuario_anulacion,
                    motivo_anulacion,
                    cui_cliente
                FROM ventas
                WHERE id_venta = ?
                """;

        Connection conexion =
                Conexion.getInstancia().getConexion();

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    idVenta
            );

            try (ResultSet rs =
                    ps.executeQuery()) {

                if (rs.next()) {

                    return mapearVenta(rs);
                }
            }
        }

        return null;
    }

    private Venta mapearVenta(
            ResultSet rs
    ) throws SQLException {

        Venta venta =
                new Venta();

        venta.setIdVenta(
                rs.getInt("id_venta")
        );

        venta.setSubtotal(
                rs.getBigDecimal("subtotal")
        );

        Timestamp fechaVenta =
                rs.getTimestamp("fecha_venta");

        if (fechaVenta != null) {

            venta.setFechaVenta(
                    fechaVenta.toLocalDateTime()
            );
        }

        venta.setTotal(
                rs.getBigDecimal("total")
        );

        venta.setDescuento(
                rs.getBigDecimal("descuento")
        );

        int usuarioAutoriza =
                rs.getInt(
                        "usuario_autoriza_descuento"
                );

        if (rs.wasNull()) {

            venta.setUsuarioAutorizaDescuento(
                    null
            );

        } else {

            venta.setUsuarioAutorizaDescuento(
                    usuarioAutoriza
            );
        }

        venta.setEstado(
                rs.getString("estado")
        );

        int idUsuario =
                rs.getInt("id_usuario");

        if (rs.wasNull()) {

            venta.setIdUsuario(null);

        } else {

            venta.setIdUsuario(
                    idUsuario
            );
        }

        Timestamp fechaAnulacion =
                rs.getTimestamp(
                        "fecha_anulacion"
                );

        if (fechaAnulacion != null) {

            venta.setFechaAnulacion(
                    fechaAnulacion.toLocalDateTime()
            );
        }

        int usuarioAnulacion =
                rs.getInt(
                        "usuario_anulacion"
                );

        if (rs.wasNull()) {

            venta.setUsuarioAnulacion(
                    null
            );

        } else {

            venta.setUsuarioAnulacion(
                    usuarioAnulacion
            );
        }

        venta.setMotivoAnulacion(
                rs.getString(
                        "motivo_anulacion"
                )
        );

        long cuiCliente =
                rs.getLong(
                        "cui_cliente"
                );

        if (rs.wasNull()) {

            venta.setCuiCliente(null);

        } else {

            venta.setCuiCliente(
                    cuiCliente
            );
        }

        return venta;
    }
}
