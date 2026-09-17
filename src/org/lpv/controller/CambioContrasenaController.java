package org.lpv.controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import org.lpv.dao.UsuarioDAO;
import org.lpv.dao.impl.UsuarioDAOImpl;
import org.lpv.manager.Sesion;
import org.lpv.model.Usuario;
import org.lpv.util.SecurityUtil;

public class CambioContrasenaController {

    @FXML
    private PasswordField txtContrasenaActual;

    @FXML
    private PasswordField txtNuevaContrasena;

    @FXML
    private PasswordField txtConfirmarContrasena;

    @FXML
    private Label lblMensaje;

    private final UsuarioDAO usuarioDAO;

    public CambioContrasenaController() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    @FXML
    public void initialize() {

        if (!Sesion.haySesion()) {
            lblMensaje.setText(
                    "No hay una sesión activa."
            );
        }
    }

    @FXML
    private void cambiarContrasena() {

        if (!Sesion.haySesion()) {

            mostrarMensaje(
                    "No hay una sesión activa."
            );

            return;
        }

        String actual =
                txtContrasenaActual.getText();

        String nueva =
                txtNuevaContrasena.getText();

        String confirmacion =
                txtConfirmarContrasena.getText();

        // Validar campos
        if (actual.isBlank()
                || nueva.isBlank()
                || confirmacion.isBlank()) {

            mostrarMensaje(
                    "Debe completar todos los campos."
            );

            return;
        }

        // Validar confirmación
        if (!nueva.equals(confirmacion)) {

            mostrarMensaje(
                    "La nueva contraseña y su confirmación no coinciden."
            );

            return;
        }

        // Evitar utilizar la misma contraseña
        if (nueva.equals(actual)) {

            mostrarMensaje(
                    "La nueva contraseña debe ser diferente a la actual."
            );

            return;
        }

        Usuario usuario =
                Sesion.getUsuarioActual();

        // Validar contraseña actual
        String hashActual =
                SecurityUtil.sha256(actual);

        if (!hashActual.equals(
                usuario.getPasswordHash())) {

            mostrarMensaje(
                    "La contraseña actual es incorrecta."
            );

            return;
        }

        // Generar hash de la nueva contraseña
        String nuevoHash =
                SecurityUtil.sha256(nueva);

        try {

            boolean actualizado =
                    usuarioDAO.cambiarContrasena(
                            usuario.getId(),
                            nuevoHash
                    );

            if (actualizado) {

                // Actualizar la contraseña almacenada
                // en la sesión actual
                usuario.setPasswordHash(
                        nuevoHash
                );

                limpiar();

                mostrarAlertaExito();

            } else {

                mostrarMensaje(
                        "No fue posible actualizar la contraseña."
                );
            }

        } catch (SQLException e) {

            mostrarMensaje(
                    "Error al actualizar la contraseña."
            );

            System.err.println(
                    "Error al cambiar contraseña: "
                    + e.getMessage()
            );
        }
    }

    @FXML
    private void limpiar() {

        txtContrasenaActual.clear();
        txtNuevaContrasena.clear();
        txtConfirmarContrasena.clear();

        lblMensaje.setText("");
    }

    @FXML
    private void regresar() throws IOException {

        Usuario usuario =
                Sesion.getUsuarioActual();

        if (usuario == null) {
            abrirLogin();
            return;
        }

        FXMLLoader loader;

        switch (usuario.getRol().toLowerCase()) {

            case "admin":

                loader = new FXMLLoader(
                        getClass().getResource(
                                "/org/lpv/view/dashboardAdmin.fxml"
                        )
                );

                break;

            case "bodega":

                loader = new FXMLLoader(
                        getClass().getResource(
                                "/org/lpv/view/dashboardBodega.fxml"
                        )
                );

                break;

            case "cajero":

                loader = new FXMLLoader(
                        getClass().getResource(
                                "/org/lpv/view/dashboardCajero.fxml"
                        )
                );

                break;

            default:

                abrirLogin();
                return;
        }

        Scene scene =
                new Scene(loader.load());

        Stage stage =
                (Stage) txtContrasenaActual
                        .getScene()
                        .getWindow();

        stage.setScene(scene);

        stage.setTitle(
                "Librería Página Viva - "
                + usuario.getRol()
        );

        stage.centerOnScreen();
    }

    private void abrirLogin() throws IOException {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/org/lpv/view/login.fxml"
                        )
                );

        Scene scene =
                new Scene(loader.load());

        Stage stage =
                (Stage) txtContrasenaActual
                        .getScene()
                        .getWindow();

        stage.setScene(scene);

        stage.setTitle(
                "Librería Página Viva - Login"
        );

        stage.centerOnScreen();
    }

    private void mostrarMensaje(
            String mensaje
    ) {

        lblMensaje.setText(mensaje);
    }

    private void mostrarAlertaExito() {

        Alert alerta = new Alert(
                Alert.AlertType.INFORMATION
        );

        alerta.setTitle(
                "Contraseña actualizada"
        );

        alerta.setHeaderText(
                "Cambio realizado correctamente"
        );

        alerta.setContentText(
                "Su contraseña ha sido actualizada correctamente."
        );

        alerta.showAndWait();
    }
}