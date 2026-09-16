package org.lpv.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    private static Conexion instancia;
    private Connection conexion;

    private static final String URL =
            "jdbc:mysql://localhost:3306/libreriadb_in4cm";

    private static final String USUARIO = "root";
    private static final String PASSWORD = "Dkendry37.";

    private Conexion() {
        conectar();
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }

        return instancia;
    }

    private void conectar() {
        try {
            conexion = DriverManager.getConnection(
                    URL,
                    USUARIO,
                    PASSWORD
            );

            System.out.println("Conexión a MySQL establecida correctamente.");

        } catch (SQLException e) {
            System.err.println(
                    "Error al conectar con MySQL: " + e.getMessage()
            );
        }
    }

    public Connection getConexion() {
        return conexion;
    }
}