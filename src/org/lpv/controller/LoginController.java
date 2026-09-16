package org.lpv.controller;

import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import org.lpv.manager.AuthService;
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
                    authService.autenticar(username, password);

            if (usuario == null) {
                lblMensaje.setText(
                        "Usuario o contraseña incorrectos."
                );
                return;
            }

            lblMensaje.setText(
                    "Bienvenido, " + usuario.getUsername()
            );

            System.out.println(
                    "Inicio de sesión: "
                    + usuario.getUsername()
                    + " | Rol: "
                    + usuario.getRol()
            );

        } catch (SQLException e) {

            lblMensaje.setText(
                    "No fue posible conectar con la base de datos."
            );

            System.err.println(
                    "Error de autenticación: " + e.getMessage()
            );
        }
    }
}