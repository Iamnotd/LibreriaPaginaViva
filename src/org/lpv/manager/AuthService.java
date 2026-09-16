package org.lpv.manager;

import java.sql.SQLException;

import org.lpv.dao.UsuarioDAO;
import org.lpv.dao.impl.UsuarioDAOImpl;
import org.lpv.model.Usuario;
import org.lpv.util.SecurityUtil;

public class AuthService {

    private final UsuarioDAO usuarioDAO;

    public AuthService() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    public Usuario autenticar(String username, String password)
            throws SQLException {

        // Validar campos vacíos
        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {

            return null;
        }

        // Buscar usuario
        Usuario usuario =
                usuarioDAO.buscarPorUsername(username.trim());

        // Usuario inexistente
        if (usuario == null) {
            return null;
        }

        // Usuario desactivado
        if (!usuario.isActivo()) {
            return null;
        }

        // Convertir la contraseña ingresada a SHA-256
        String passwordHash =
                SecurityUtil.sha256(password);

        // Comparar hashes
        if (!passwordHash.equalsIgnoreCase(
                usuario.getPasswordHash())) {

            return null;
        }

        // Credenciales correctas
        return usuario;
    }
}