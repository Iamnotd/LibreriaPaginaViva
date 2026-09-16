package org.lpv.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.lpv.dao.UsuarioDAO;
import org.lpv.model.Usuario;
import org.lpv.util.Conexion;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final Connection conexion;

    public UsuarioDAOImpl() {
        this.conexion = Conexion.getInstancia().getConexion();
    }

    @Override
    public Usuario buscarPorUsername(String username) throws SQLException {

        String sql = """
                SELECT
                    id,
                    username,
                    password_hash,
                    rol,
                    activo
                FROM usuarios
                WHERE username = ?
                """;

        try (PreparedStatement statement = conexion.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultado = statement.executeQuery()) {

                if (resultado.next()) {

                    Usuario usuario = new Usuario();

                    usuario.setId(resultado.getInt("id"));
                    usuario.setUsername(resultado.getString("username"));
                    usuario.setPasswordHash(
                            resultado.getString("password_hash")
                    );
                    usuario.setRol(resultado.getString("rol"));
                    usuario.setActivo(resultado.getBoolean("activo"));

                    return usuario;
                }
            }
        }

        return null;
    }
}