package org.lpv.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

    private static Conexion instancia;

    private static final String CONFIG_FILE = "/db.properties";

    private final String url;
    private final String user;
    private final String password;

    private Conexion() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(
                    "No se encontró el driver de MySQL.",
                    e
            );
        }

        Properties propiedades = new Properties();

        try (InputStream entrada =
                     getClass().getResourceAsStream(CONFIG_FILE)) {

            if (entrada == null) {
                throw new IllegalStateException(
                        "No se encontró /db.properties en el classpath. "
                        + "Coloca db.properties dentro de src."
                );
            }

            propiedades.load(entrada);

        } catch (IOException e) {
            throw new IllegalStateException(
                    "No se pudo leer /db.properties.",
                    e
            );
        }

        url = propiedades.getProperty("db.url");
        user = propiedades.getProperty("db.user");
        password = propiedades.getProperty("db.password");

        if (url == null || user == null || password == null) {
            throw new IllegalStateException(
                    "Faltan propiedades en db.properties. "
                    + "Se requieren: db.url, db.user y db.password."
            );
        }
    }

    public static synchronized Conexion getInstancia() {

        if (instancia == null) {
            instancia = new Conexion();
        }

        return instancia;
    }

    public Connection conectar() throws SQLException {

        return DriverManager.getConnection(
                url,
                user,
                password
        );
    }

    public Connection getConexion() {

        try {
            return conectar();

        } catch (SQLException e) {

            throw new IllegalStateException(
                    "No se pudo conectar a MySQL.",
                    e
            );
        }
    }
}