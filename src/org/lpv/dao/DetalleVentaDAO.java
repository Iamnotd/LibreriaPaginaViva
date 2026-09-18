package org.lpv.dao;

import java.sql.Connection;
import java.sql.SQLException;
import org.lpv.model.DetalleVenta;

public interface DetalleVentaDAO {

    void registrarDetalle(
            DetalleVenta detalle,
            Connection conexion
    ) throws SQLException;
}