package org.lpv.dao;

import java.sql.SQLException;
import org.lpv.model.Usuario;

public interface UsuarioDAO {

    Usuario buscarPorUsername(String username) throws SQLException;
}