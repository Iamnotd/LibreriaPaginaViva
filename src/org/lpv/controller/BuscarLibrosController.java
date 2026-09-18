package org.lpv.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import org.lpv.dao.LibroDAO;
import org.lpv.dao.impl.LibroDAOImpl;
import org.lpv.model.Libro;

public class BuscarLibrosController implements Initializable {

    @FXML
    private ComboBox<String> cmbTipoBusqueda;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Label lblMensaje;

    @FXML
    private TableView<Libro> tblLibros;

    @FXML
    private TableColumn<Libro, String> colIsbn;

    @FXML
    private TableColumn<Libro, String> colTitulo;

    @FXML
    private TableColumn<Libro, BigDecimal> colPrecio;

    @FXML
    private TableColumn<Libro, Integer> colStock;

    @FXML
    private TableColumn<Libro, String> colEstado;

    private LibroDAO libroDAO;

    @Override
    public void initialize(
            URL location,
            ResourceBundle resources
    ) {

        libroDAO = new LibroDAOImpl();

        cmbTipoBusqueda.getItems().addAll(
                "ISBN",
                "Título",
                "Autor"
        );

        cmbTipoBusqueda.setValue("Título");

        configurarTabla();
    }

    private void configurarTabla() {

        colIsbn.setCellValueFactory(
                new PropertyValueFactory<>("isbn")
        );

        colTitulo.setCellValueFactory(
                new PropertyValueFactory<>("titulo")
        );

        colPrecio.setCellValueFactory(
                new PropertyValueFactory<>("precio")
        );

        colStock.setCellValueFactory(
                new PropertyValueFactory<>("stockActual")
        );

        colEstado.setCellValueFactory(
                cellData -> {

                    String estado =
                            cellData.getValue().isActivo()
                                    ? "Activo"
                                    : "Inactivo";

                    return new javafx.beans.property
                            .SimpleStringProperty(estado);
                }
        );
    }

    @FXML
    private void buscar() {

        String texto = txtBusqueda.getText().trim();
        String tipo = cmbTipoBusqueda.getValue();

        if (texto.isEmpty()) {

            lblMensaje.setText(
                    "Ingrese un dato para realizar la búsqueda."
            );

            tblLibros.getItems().clear();
            return;
        }

        try {

            List<Libro> resultados = new ArrayList<>();

            switch (tipo) {

                case "ISBN" -> {

                    Libro libro =
                            libroDAO.buscarPorIsbn(texto);

                    if (libro != null) {
                        resultados.add(libro);
                    }
                }

                case "Título" ->
                    resultados =
                            libroDAO.buscarPorTitulo(texto);

                case "Autor" ->
                    resultados =
                            libroDAO.buscarPorAutor(texto);

                default ->
                    lblMensaje.setText(
                            "Seleccione un tipo de búsqueda."
                    );
            }

            ObservableList<Libro> datos =
                    FXCollections.observableArrayList(
                            resultados
                    );

            tblLibros.setItems(datos);

            if (resultados.isEmpty()) {

                lblMensaje.setText(
                        "No se encontraron libros."
                );

            } else {

                lblMensaje.setText(
                        "Resultados encontrados: "
                        + resultados.size()
                );
            }

        } catch (SQLException e) {

            lblMensaje.setText(
                    "Error al consultar los libros."
            );

            System.err.println(
                    "Error al buscar libros: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }
    }
}