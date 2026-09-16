package org.lpv.manager;

import java.sql.SQLException;

import org.lpv.dao.UsuarioDAO;
import org.lpv.dao.impl.UsuarioDAOImpl;
import org.lpv.model.Usuario;

public class AuthService {

    private final UsuarioDAO usuarioDAO;

    public AuthService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public Usuario autenticar(String username, String password)
            throws SQLException {

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            return null;
        }


        Usuario usuario = usuarioDAO.buscarPorUsername(username);

        if (usuario == null) {
            return null;
        }


        if (!usuario.isActivo()) {
            return null;
        }



        return usuario;
    }
}