package org.lpv.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.lpv.dao.MovimientoInventarioDAO;
import org.lpv.dao.impl.MovimientoInventarioDAOImpl;
import org.lpv.manager.Sesion;
import org.lpv.model.MovimientoInventario;

public class IngresoInventarioController {

    @FXML
    private TextField txtIsbn;

    @FXML
    private TextField txtCantidad;

    @FXML
    private TextField txtProveedor;

    @FXML
    private TextField txtObservacion;

    @FXML
    private Label lblUsuario;

    private final MovimientoInventarioDAO movimientoDAO =
            new MovimientoInventarioDAOImpl();

    @FXML
    public void initialize() {

        if (!Sesion.tieneRol("bodega")) {
            return;
        }

        if (Sesion.getUsuarioActual() != null) {
            lblUsuario.setText(
                    "Usuario: "
                    + Sesion.getUsuarioActual().getUsername()
            );
        }
    }

    @FXML
    private void registrarIngreso() {

        String isbn = txtIsbn.getText().trim();
        String cantidadTexto = txtCantidad.getText().trim();
        String proveedor = txtProveedor.getText().trim();
        String observacion = txtObservacion.getText().trim();

        if (isbn.isEmpty()
                || cantidadTexto.isEmpty()) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Campos obligatorios",
                    "Ingrese el ISBN y la cantidad."
            );

            return;
        }

        int cantidad;

        try {

            cantidad = Integer.parseInt(cantidadTexto);

        } catch (NumberFormatException e) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Cantidad inválida",
                    "La cantidad debe ser un número entero."
            );

            return;
        }

        if (cantidad <= 0) {

            mostrarAlerta(
                    Alert.AlertType.WARNING,
                    "Cantidad inválida",
                    "La cantidad debe ser mayor que cero."
            );

            return;
        }

        Integer idUsuario = null;

        if (Sesion.getUsuarioActual() != null) {
            idUsuario = Sesion.getUsuarioActual().getId();
        }

        MovimientoInventario movimiento =
                new MovimientoInventario(
                        isbn,
                        "INGRESO",
                        cantidad,
                        idUsuario,
                        observacion,
                        proveedor
                );

        boolean registrado =
                movimientoDAO.registrar(movimiento);

        if (registrado) {

            mostrarAlerta(
                    Alert.AlertType.INFORMATION,
                    "Ingreso registrado",
                    "El ingreso de inventario se registró correctamente."
            );

            limpiarFormulario();

        } else {

            mostrarAlerta(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo registrar el ingreso de inventario."
            );
        }
    }

    @FXML
    private void volver()
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/org/lpv/view/dashboardBodega.fxml"
                )
        );

        Scene scene = new Scene(loader.load());

        Stage stage =
                (Stage) txtIsbn
                        .getScene()
                        .getWindow();

        stage.setTitle(
                "Librería Página Viva - Dashboard Bodega"
        );

        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void limpiarFormulario() {

        txtIsbn.clear();
        txtCantidad.clear();
        txtProveedor.clear();
        txtObservacion.clear();

        txtIsbn.requestFocus();
    }

    private void mostrarAlerta(
            Alert.AlertType tipo,
            String titulo,
            String mensaje) {

        Alert alerta = new Alert(tipo);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}