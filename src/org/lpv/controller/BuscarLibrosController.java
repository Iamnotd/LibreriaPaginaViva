package org.lpv.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class BuscarLibrosController implements Initializable {

    @FXML
    private ComboBox<String> cmbTipoBusqueda;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Label lblMensaje;

    @Override
    public void initialize(
            URL location,
            ResourceBundle resources
    ) {

        cmbTipoBusqueda.getItems().addAll(
                "ISBN",
                "Título",
                "Autor"
        );

        cmbTipoBusqueda.setValue("Título");
    }

    @FXML
    private void buscar() {

        String texto = txtBusqueda.getText().trim();
        String tipo = cmbTipoBusqueda.getValue();

        if (texto.isEmpty()) {
            lblMensaje.setText(
                    "Ingrese un dato para realizar la búsqueda."
            );
            return;
        }

        lblMensaje.setText(
                "Buscando por "
                + tipo
                + ": "
                + texto
        );
    }
}