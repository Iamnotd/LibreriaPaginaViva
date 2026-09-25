package org.lpv.dao;

import java.util.List;
import org.lpv.model.MovimientoInventario;

public interface MovimientoInventarioDAO {

    boolean registrar(MovimientoInventario movimiento);

    List<MovimientoInventario> listar();

}