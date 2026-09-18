package org.lpv.dao;

import java.sql.Connection;
import java.sql.SQLException;
import org.lpv.model.Venta;

public interface VentaDAO {

    int registrarVenta(
            Venta venta,
            Connection conexion
    ) throws SQLException;

    Venta buscarPorId(
            int idVenta
    ) throws SQLException;
}
