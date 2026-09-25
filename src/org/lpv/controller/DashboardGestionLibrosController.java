package org.lpv.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.lpv.dao.LibroDAO;
import org.lpv.dao.impl.LibroDAOImpl;
import org.lpv.model.Libro;

public class DashboardGestionLibrosController {

    @FXML
    private TextField txtIsbn;

    @FXML
    private TextField txtTitulo;

    @FXML
    private DatePicker dpFechaPublicacion;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtIdCategoria;

    @FXML
    private TextField txtNitEditorial;

    @FXML
    private TextField txtStockActual;

    @FXML
    private TextField txtStockMinimo;

    @FXML
    private TableView<Libro> tablaLibros;

    @FXML
    private TableColumn<Libro, String> colIsbn;

    @FXML
    private TableColumn<Libro, String> colTitulo;

    @FXML
    private TableColumn<Libro, BigDecimal> colPrecio;

    @FXML
    private TableColumn<Libro, Integer> colStock;

    @FXML
    private TableColumn<Libro, Integer> colStockMinimo;

    @FXML
    private TableColumn<Libro, Boolean> colActivo;

    private LibroDAO libroDAO;


    @FXML
    public void initialize() {

        libroDAO = new LibroDAOImpl();

        configurarTabla();

        tablaLibros.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable, anterior, seleccionado) -> {

                            if (seleccionado != null) {
                                cargarLibroEnFormulario(seleccionado);
                            }
                        }
                );

        cargarLibros();
    }


    private void configurarTabla() {

        colIsbn.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        dato.getValue().getIsbn()
                )
        );

        colTitulo.setCellValueFactory(
                dato -> new SimpleStringProperty(
                        dato.getValue().getTitulo()
                )
        );

        colPrecio.setCellValueFactory(
                dato -> new SimpleObjectProperty<>(
                        dato.getValue().getPrecio()
                )
        );

        colStock.setCellValueFactory(
                dato -> new SimpleIntegerProperty(
                        dato.getValue().getStockActual()
                ).asObject()
        );

        colStockMinimo.setCellValueFactory(
                dato -> new SimpleIntegerProperty(
                        dato.getValue().getStockMinimo()
                ).asObject()
        );

        colActivo.setCellValueFactory(
                dato -> new SimpleBooleanProperty(
                        dato.getValue().isActivo()
                ).asObject()
        );
    }


    private void cargarLibros() {

        try {

            List<Libro> libros =
                    libroDAO.listarTodos();

            tablaLibros.getItems().setAll(libros);

        } catch (SQLException e) {

            mostrarError(
                    "No se pudieron cargar los libros.\n"
                    + e.getMessage()
            );
        }
    }


    private void cargarLibroEnFormulario(
            Libro libro
    ) {

        txtIsbn.setText(libro.getIsbn());

        txtTitulo.setText(libro.getTitulo());

        dpFechaPublicacion.setValue(
                libro.getFechaPublicacion()
        );

        txtPrecio.setText(
                libro.getPrecio() != null
                        ? libro.getPrecio().toString()
                        : ""
        );

        txtIdCategoria.setText(
                libro.getIdCategoria() != null
                        ? libro.getIdCategoria().toString()
                        : ""
        );

        txtNitEditorial.setText(
                libro.getNitEditorial() != null
                        ? libro.getNitEditorial()
                        : ""
        );

        txtStockActual.setText(
                String.valueOf(libro.getStockActual())
        );

        txtStockMinimo.setText(
                String.valueOf(libro.getStockMinimo())
        );

        /*
         * No permitimos modificar el ISBN de un libro
         * que ya fue seleccionado.
         */
        txtIsbn.setDisable(true);
    }


    @FXML
    private void registrarLibro() {

        try {

            validarFormulario();

            Libro libro = crearLibroDesdeFormulario();

            libro.setActivo(true);

            libroDAO.registrar(libro);

            mostrarInformacion(
                    "Libro registrado correctamente."
            );

            cargarLibros();
            limpiarFormulario();

        } catch (NumberFormatException e) {

            mostrarError(
                    "Precio, categoría y stock deben contener valores válidos."
            );

        } catch (SQLException e) {

            mostrarError(
                    "No se pudo registrar el libro.\n"
                    + e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            mostrarError(e.getMessage());
        }
    }


    @FXML
    private void actualizarLibro() {

        Libro seleccionado =
                tablaLibros
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarError(
                    "Seleccione un libro de la tabla."
            );

            return;
        }

        try {

            validarFormulario();

            Libro libro =
                    crearLibroDesdeFormulario();

            libro.setActivo(
                    seleccionado.isActivo()
            );

            libroDAO.actualizar(libro);

            mostrarInformacion(
                    "Libro actualizado correctamente."
            );

            cargarLibros();
            limpiarFormulario();

        } catch (NumberFormatException e) {

            mostrarError(
                    "Precio, categoría y stock deben contener valores válidos."
            );

        } catch (SQLException e) {

            mostrarError(
                    "No se pudo actualizar el libro.\n"
                    + e.getMessage()
            );

        } catch (IllegalArgumentException e) {

            mostrarError(e.getMessage());
        }
    }


    @FXML
    private void cambiarEstadoLibro() {

        Libro seleccionado =
                tablaLibros
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarError(
                    "Seleccione un libro de la tabla."
            );

            return;
        }

        try {

            boolean nuevoEstado =
                    !seleccionado.isActivo();

            libroDAO.cambiarEstado(
                    seleccionado.getIsbn(),
                    nuevoEstado
            );

            if (nuevoEstado) {

                mostrarInformacion(
                        "Libro activado correctamente."
                );

            } else {

                mostrarInformacion(
                        "Libro desactivado correctamente."
                );
            }

            cargarLibros();
            limpiarFormulario();

        } catch (SQLException e) {

            mostrarError(
                    "No se pudo cambiar el estado del libro.\n"
                    + e.getMessage()
            );
        }
    }


    private Libro crearLibroDesdeFormulario() {

        Libro libro = new Libro();

        libro.setIsbn(
                txtIsbn.getText().trim()
        );

        libro.setTitulo(
                txtTitulo.getText().trim()
        );

        libro.setFechaPublicacion(
                dpFechaPublicacion.getValue()
        );

        libro.setPrecio(
                new BigDecimal(
                        txtPrecio.getText().trim()
                )
        );

        if (txtIdCategoria.getText().trim().isEmpty()) {

            libro.setIdCategoria(null);

        } else {

            libro.setIdCategoria(
                    Integer.valueOf(
                            txtIdCategoria
                                    .getText()
                                    .trim()
                    )
            );
        }

        String nit =
                txtNitEditorial
                        .getText()
                        .trim();

        libro.setNitEditorial(
                nit.isEmpty() ? null : nit
        );

        libro.setStockActual(
                Integer.parseInt(
                        txtStockActual
                                .getText()
                                .trim()
                )
        );

        libro.setStockMinimo(
                Integer.parseInt(
                        txtStockMinimo
                                .getText()
                                .trim()
                )
        );

        return libro;
    }


    private void validarFormulario() {

        if (txtIsbn.getText().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El ISBN es obligatorio."
            );
        }

        if (txtTitulo.getText().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El título es obligatorio."
            );
        }

        if (txtPrecio.getText().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El precio es obligatorio."
            );
        }

        if (txtStockActual.getText().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El stock actual es obligatorio."
            );
        }

        if (txtStockMinimo.getText().trim().isEmpty()) {

            throw new IllegalArgumentException(
                    "El stock mínimo es obligatorio."
            );
        }
    }


    @FXML
    private void limpiarFormulario() {

        txtIsbn.clear();
        txtTitulo.clear();

        dpFechaPublicacion.setValue(null);

        txtPrecio.clear();
        txtIdCategoria.clear();
        txtNitEditorial.clear();
        txtStockActual.clear();
        txtStockMinimo.clear();

        tablaLibros
                .getSelectionModel()
                .clearSelection();

        txtIsbn.setDisable(false);
    }


    @FXML
    private void regresar()
            throws IOException {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource(
                        "/org/lpv/view/dashboardBodega.fxml"
                )
        );

        Scene scene =
                new Scene(loader.load());

        Stage stage =
                (Stage) tablaLibros
                        .getScene()
                        .getWindow();

        stage.setTitle(
                "Librería Página Viva - Bodega"
        );

        stage.setScene(scene);
        stage.centerOnScreen();
    }


    private void mostrarInformacion(
            String mensaje
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alerta.setTitle(
                "Librería Página Viva"
        );

        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }


    private void mostrarError(
            String mensaje
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alerta.setTitle(
                "Librería Página Viva"
        );

        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}