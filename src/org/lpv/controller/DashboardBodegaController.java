package org.lpv.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import org.lpv.manager.Sesion;

public class DashboardBodegaController {

    @FXML
    private Label lblUsuario;

    @FXML
    public void initialize() {

        if (!Sesion.tieneRol("bodega")) {
            return;
        }

        lblUsuario.setText(
                "Bienvenido, "
                + Sesion.getUsuarioActual().getUsername()
        );
    }

    @FXML
    private void abrirGestionLibros()
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/org/lpv/view/dashboardgestionLibros.fxml"
                )
        );

        Scene scene =
                new Scene(loader.load());

        Stage stage =
                (Stage) lblUsuario
                        .getScene()
                        .getWindow();

        stage.setTitle(
                "Librería Página Viva - Gestión de libros"
        );

        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    private void abrirCambioContrasena()
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/org/lpv/view/cambioContrasena.fxml"
                )
        );

        Scene scene =
                new Scene(loader.load());

        Stage stage =
                (Stage) lblUsuario
                        .getScene()
                        .getWindow();

        stage.setTitle(
                "Librería Página Viva - Cambiar contraseña"
        );

        stage.setScene(scene);
        stage.centerOnScreen();
    }

    @FXML
    private void cerrarSesion()
            throws IOException {

        Sesion.cerrarSesion();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/org/lpv/view/login.fxml"
                )
        );

        Scene scene =
                new Scene(loader.load());

        Stage stage =
                (Stage) lblUsuario
                        .getScene()
                        .getWindow();

        stage.setTitle(
                "Librería Página Viva - Login"
        );

        stage.setScene(scene);
        stage.centerOnScreen();
    }
}