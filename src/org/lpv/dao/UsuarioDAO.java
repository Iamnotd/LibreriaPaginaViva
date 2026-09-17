package org.lpv.dao;

import java.sql.SQLException;
import java.util.List;
import org.lpv.model.Usuario;

public interface UsuarioDAO {

    Usuario buscarPorUsername(String username) throws SQLException;

    List<Usuario> listar() throws SQLException;

    boolean crear(Usuario usuario) throws SQLException;

    boolean actualizar(Usuario usuario) throws SQLException;

    boolean cambiarEstado(int id, boolean activo) throws SQLException;
}