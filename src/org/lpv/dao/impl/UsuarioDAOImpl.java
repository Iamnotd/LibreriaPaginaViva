package org.lpv.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.lpv.dao.UsuarioDAO;
import org.lpv.model.Usuario;
import org.lpv.util.Conexion;

public class UsuarioDAOImpl implements UsuarioDAO {

    private final Connection conexion;

    public UsuarioDAOImpl() {
        this.conexion =
                Conexion.getInstancia().getConexion();
    }

    @Override
    public Usuario buscarPorUsername(
            String username
    ) throws SQLException {

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

        try (PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultado =
                         statement.executeQuery()) {

                if (resultado.next()) {
                    return convertirUsuario(resultado);
                }
            }
        }

        return null;
    }

    @Override
    public List<Usuario> listar()
            throws SQLException {

        List<Usuario> usuarios =
                new ArrayList<>();

        String sql = """
                SELECT
                    id,
                    username,
                    password_hash,
                    rol,
                    activo
                FROM usuarios
                ORDER BY id
                """;

        try (PreparedStatement statement =
                     conexion.prepareStatement(sql);
             ResultSet resultado =
                     statement.executeQuery()) {

            while (resultado.next()) {

                usuarios.add(
                        convertirUsuario(resultado)
                );
            }
        }

        return usuarios;
    }

    @Override
    public boolean crear(
            Usuario usuario
    ) throws SQLException {

        String sql = """
                INSERT INTO usuarios
                    (
                        username,
                        password_hash,
                        rol,
                        activo
                    )
                VALUES (?, ?, ?, ?)
                """;

        try (PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(
                    1,
                    usuario.getUsername()
            );

            statement.setString(
                    2,
                    usuario.getPasswordHash()
            );

            statement.setString(
                    3,
                    usuario.getRol()
            );

            statement.setBoolean(
                    4,
                    usuario.isActivo()
            );

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean actualizar(
            Usuario usuario
    ) throws SQLException {

        String sql = """
                UPDATE usuarios
                SET username = ?,
                    rol = ?,
                    activo = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(
                    1,
                    usuario.getUsername()
            );

            statement.setString(
                    2,
                    usuario.getRol()
            );

            statement.setBoolean(
                    3,
                    usuario.isActivo()
            );

            statement.setInt(
                    4,
                    usuario.getId()
            );

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cambiarEstado(
            int id,
            boolean activo
    ) throws SQLException {

        String sql = """
                UPDATE usuarios
                SET activo = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setBoolean(1, activo);
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean cambiarContrasena(
            int id,
            String passwordHash
    ) throws SQLException {

        String sql = """
                UPDATE usuarios
                SET password_hash = ?
                WHERE id = ?
                """;

        try (PreparedStatement statement =
                     conexion.prepareStatement(sql)) {

            statement.setString(
                    1,
                    passwordHash
            );

            statement.setInt(
                    2,
                    id
            );

            return statement.executeUpdate() > 0;
        }
    }

    private Usuario convertirUsuario(
            ResultSet resultado
    ) throws SQLException {

        Usuario usuario = new Usuario();

        usuario.setId(
                resultado.getInt("id")
        );

        usuario.setUsername(
                resultado.getString("username")
        );

        usuario.setPasswordHash(
                resultado.getString("password_hash")
        );

        usuario.setRol(
                resultado.getString("rol")
        );

        usuario.setActivo(
                resultado.getBoolean("activo")
        );

        return usuario;
    }
}