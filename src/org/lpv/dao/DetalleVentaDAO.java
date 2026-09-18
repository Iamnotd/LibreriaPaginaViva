package org.lpv.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.lpv.model.DetalleVenta;

public interface DetalleVentaDAO {

    void registrarDetalle(
            DetalleVenta detalle,
            Connection conexion
    ) throws SQLException;

    List<DetalleVenta> buscarPorVenta(
            int idVenta
    ) throws SQLException;
}
