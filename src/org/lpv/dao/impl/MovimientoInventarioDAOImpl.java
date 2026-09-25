package org.lpv.dao.impl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.lpv.dao.MovimientoInventarioDAO;
import org.lpv.model.MovimientoInventario;
import org.lpv.util.Conexion;

public class MovimientoInventarioDAOImpl implements MovimientoInventarioDAO {

    @Override
    public boolean registrar(MovimientoInventario movimiento) {

        String sql = "{CALL sp_registrarmovimiento(?, ?, ?, ?, ?, ?)}";

        Connection conexion = Conexion.getInstancia().getConexion();

        try (CallableStatement statement = conexion.prepareCall(sql)) {

            statement.setString(1, movimiento.getIsbn());
            statement.setString(2, movimiento.getTipoMovimiento());
            statement.setInt(3, movimiento.getCantidad());

            if (movimiento.getIdUsuario() != null) {
                statement.setInt(4, movimiento.getIdUsuario());
            } else {
                statement.setNull(4, java.sql.Types.INTEGER);
            }

            statement.setString(5, movimiento.getObservacion());

            if (movimiento.getNitProveedor() != null
                    && !movimiento.getNitProveedor().isBlank()) {
                statement.setString(6, movimiento.getNitProveedor());
            } else {
                statement.setNull(6, java.sql.Types.VARCHAR);
            }

            statement.execute();

            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar movimiento: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<MovimientoInventario> listar() {

        List<MovimientoInventario> movimientos = new ArrayList<>();

        String sql = "{CALL sp_listarmovimientos()}";

        Connection conexion = Conexion.getInstancia().getConexion();

        try (CallableStatement statement = conexion.prepareCall(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {

                MovimientoInventario movimiento = new MovimientoInventario();

                movimiento.setIdMovimiento(
                        resultSet.getInt("id_movimiento"));

                movimiento.setIsbn(
                        resultSet.getString("isbn"));

                movimiento.setTipoMovimiento(
                        resultSet.getString("tipo_movimiento"));

                movimiento.setCantidad(
                        resultSet.getInt("cantidad"));

                Timestamp fecha = resultSet.getTimestamp("fecha_movimiento");

                if (fecha != null) {
                    movimiento.setFechaMovimiento(
                            fecha.toLocalDateTime());
                }

                int idUsuario = resultSet.getInt("id_usuario");

                if (!resultSet.wasNull()) {
                    movimiento.setIdUsuario(idUsuario);
                }

                movimiento.setObservacion(
                        resultSet.getString("observacion"));

                movimiento.setNitProveedor(
                        resultSet.getString("nit_proveedor"));

                movimientos.add(movimiento);
            }

        } catch (SQLException e) {
            System.err.println("Error al listar movimientos: " + e.getMessage());
        }

        return movimientos;
    }
}