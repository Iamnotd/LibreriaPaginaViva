package org.lpv.controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.lpv.manager.AuthService;
import org.lpv.manager.Sesion;
import org.lpv.model.Rol;
import org.lpv.model.Usuario;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    private final AuthService authService;

    public LoginController() {
        this.authService = new AuthService();
    }

    @FXML
    private void iniciarSesion() {

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isBlank() || password.isBlank()) {

            lblMensaje.setText(
                    "Debe ingresar usuario y contraseña."
            );

            return;
        }

        try {

            Usuario usuario =
                    authService.autenticar(
                            username,
                            password
                    );

            if (usuario == null) {

                lblMensaje.setText(
                        "Usuario o contraseña incorrectos."
                );

                return;
            }

            // Guardamos el usuario autenticado
            Sesion.iniciarSesion(usuario);

            System.out.println(
                    "Sesión iniciada: "
                    + Sesion.getUsuarioActual().getUsername()
                    + " | Rol: "
                    + Sesion.getRol()
            );

            // Abrimos el dashboard correspondiente
            abrirDashboard();

        } catch (SQLException e) {

            lblMensaje.setText(
                    "No fue posible conectar con la base de datos."
            );

            System.err.println(
                    "Error de autenticación: "
                    + e.getMessage()
            );

        } catch (IOException e) {

            lblMensaje.setText(
                    "No fue posible abrir el dashboard."
            );

            System.err.println(
                    "Error cargando FXML: "
                    + e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            lblMensaje.setText(
                    "El usuario tiene un rol no válido."
            );

            System.err.println(
                    "Error de rol: "
                    + e.getMessage()
            );
        }
    }

    private void abrirDashboard() throws IOException {

        Rol rol = Rol.desdeString(
                Sesion.getRol()
        );

        String rutaFXML;
        String titulo;

        switch (rol) {

            case ADMIN:
                rutaFXML =
                        "/org/lpv/view/dashboardAdmin.fxml";
                titulo =
                        "Librería Página Viva - Administrador";
                break;

            case BODEGA:
                rutaFXML =
                        "/org/lpv/view/dashboardBodega.fxml";
                titulo =
                        "Librería Página Viva - Bodega";
                break;

            case CAJERO:
                rutaFXML =
                        "/org/lpv/view/dashboardCajero.fxml";
                titulo =
                        "Librería Página Viva - Cajero";
                break;

            default:
                throw new IllegalArgumentException(
                        "Rol no permitido."
                );
        }

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(rutaFXML)
        );

        Scene scene = new Scene(loader.load());

        Stage stage =
                (Stage) txtUsername.getScene().getWindow();

        stage.setTitle(titulo);
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}