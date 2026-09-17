package org.lpv.controller;

import java.io.IOException;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import org.lpv.dao.UsuarioDAO;
import org.lpv.dao.impl.UsuarioDAOImpl;
import org.lpv.manager.Sesion;
import org.lpv.model.Rol;
import org.lpv.model.Usuario;
import org.lpv.util.SecurityUtil;

public class DashboardUsuarioController {

    @FXML
    private TableView<Usuario> tablaUsuarios;
    @FXML
    private TableColumn<Usuario, Integer> colId;
    @FXML
    private TableColumn<Usuario, String> colUsername;
    @FXML
    private TableColumn<Usuario, String> colRol;
    @FXML
    private TableColumn<Usuario, Boolean> colActivo;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private ComboBox<Rol> cmbRol;
    @FXML
    private Label lblMensaje;

    private final UsuarioDAO usuarioDAO;

    private Usuario usuarioSeleccionado;

    public DashboardUsuarioController() {
        this.usuarioDAO = new UsuarioDAOImpl();
    }

    @FXML
    public void initialize() {

        configurarTabla();
        cargarRoles();
        cargarUsuarios();

        tablaUsuarios.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, anterior, nuevo) -> {

                    if (nuevo != null) {
                        cargarUsuarioSeleccionado(nuevo);
                    }
                });
    }

    private void configurarTabla() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        colUsername.setCellValueFactory(
                new PropertyValueFactory<>("username")
        );

        colRol.setCellValueFactory(
                new PropertyValueFactory<>("rol")
        );

        colActivo.setCellValueFactory(
                new PropertyValueFactory<>("activo")
        );
    }

    private void cargarRoles() {

        cmbRol.setItems(
                FXCollections.observableArrayList(
                        Rol.values()
                )
        );
    }

    private void cargarUsuarios() {

        try {

            ObservableList<Usuario> usuarios =
                    FXCollections.observableArrayList(
                            usuarioDAO.listar()
                    );

            tablaUsuarios.setItems(usuarios);

            mostrarMensaje("");

        } catch (SQLException e) {

            mostrarError(
                    "Error al cargar usuarios",
                    e.getMessage()
            );
        }
    }

    private void cargarUsuarioSeleccionado(
            Usuario usuario
    ) {

        usuarioSeleccionado = usuario;

        txtUsername.setText(
                usuario.getUsername()
        );

        cmbRol.setValue(
                Rol.desdeString(usuario.getRol())
        );

        txtPassword.clear();

        mostrarMensaje(
                "Usuario seleccionado: "
                + usuario.getUsername()
        );
    }

    @FXML
    private void crearUsuario() {

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        Rol rol = cmbRol.getValue();

        if (username.isBlank()
                || password.isBlank()
                || rol == null) {

            mostrarMensaje(
                    "Debe completar todos los campos."
            );

            return;
        }

        try {

            if (usuarioDAO.buscarPorUsername(username) != null) {

                mostrarMensaje(
                        "El nombre de usuario ya existe."
                );

                return;
            }

            String passwordHash =
                    SecurityUtil.sha256(password);

            Usuario usuario = new Usuario();

            usuario.setUsername(username);
            usuario.setPasswordHash(passwordHash);
            usuario.setRol(rol.getValor());
            usuario.setActivo(true);

            boolean creado =
                    usuarioDAO.crear(usuario);

            if (creado) {

                mostrarMensaje(
                        "Usuario creado correctamente."
                );

                limpiar();
                cargarUsuarios();

            } else {

                mostrarMensaje(
                        "No fue posible crear el usuario."
                );
            }

        } catch (SQLException e) {

            mostrarError(
                    "Error al crear usuario",
                    e.getMessage()
            );
        }
    }

    @FXML
    private void actualizarUsuario() {

        if (usuarioSeleccionado == null) {

            mostrarMensaje(
                    "Seleccione un usuario de la tabla."
            );

            return;
        }

        String username = txtUsername.getText().trim();
        Rol rol = cmbRol.getValue();

        if (username.isBlank() || rol == null) {

            mostrarMensaje(
                    "Debe ingresar usuario y rol."
            );

            return;
        }

        try {

            Usuario usuarioExistente =
                    usuarioDAO.buscarPorUsername(username);

            if (usuarioExistente != null
                    && usuarioExistente.getId()
                    != usuarioSeleccionado.getId()) {

                mostrarMensaje(
                        "Ese nombre de usuario ya existe."
                );

                return;
            }

            usuarioSeleccionado.setUsername(username);
            usuarioSeleccionado.setRol(
                    rol.getValor()
            );

            boolean actualizado =
                    usuarioDAO.actualizar(
                            usuarioSeleccionado
                    );

            if (actualizado) {

                mostrarMensaje(
                        "Usuario actualizado correctamente."
                );

                limpiar();
                cargarUsuarios();

            } else {

                mostrarMensaje(
                        "No fue posible actualizar el usuario."
                );
            }

        } catch (SQLException e) {

            mostrarError(
                    "Error al actualizar usuario",
                    e.getMessage()
            );
        }
    }

    @FXML
    private void cambiarEstado() {

        if (usuarioSeleccionado == null) {

            mostrarMensaje(
                    "Seleccione un usuario de la tabla."
            );

            return;
        }

        boolean nuevoEstado =
                !usuarioSeleccionado.isActivo();

        try {

            boolean actualizado =
                    usuarioDAO.cambiarEstado(
                            usuarioSeleccionado.getId(),
                            nuevoEstado
                    );

            if (actualizado) {

                mostrarMensaje(
                        nuevoEstado
                        ? "Usuario activado correctamente."
                        : "Usuario desactivado correctamente."
                );

                limpiar();
                cargarUsuarios();

            } else {

                mostrarMensaje(
                        "No fue posible cambiar el estado."
                );
            }

        } catch (SQLException e) {

            mostrarError(
                    "Error al cambiar estado",
                    e.getMessage()
            );
        }
    }

    @FXML
    private void limpiar() {

        usuarioSeleccionado = null;

        txtUsername.clear();
        txtPassword.clear();
        cmbRol.getSelectionModel().clearSelection();

        tablaUsuarios.getSelectionModel()
                .clearSelection();

        mostrarMensaje("");
    }

    @FXML
    private void regresar() throws IOException {

        FXMLLoader loader =
                new FXMLLoader(
                        getClass().getResource(
                                "/org/lpv/view/dashboardAdmin.fxml"
                        )
                );

        Scene scene = new Scene(
                loader.load()
        );

        Stage stage =
                (Stage) tablaUsuarios
                        .getScene()
                        .getWindow();

        stage.setTitle(
                "Librería Página Viva - Administrador"
        );

        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void mostrarMensaje(String mensaje) {

        lblMensaje.setText(mensaje);
    }

    private void mostrarError(
            String titulo,
            String mensaje
    ) {

        Alert alerta =
                new Alert(Alert.AlertType.ERROR);

        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        alerta.showAndWait();
    }
}