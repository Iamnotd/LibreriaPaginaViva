package org.lpv.system;

import java.sql.Connection;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.lpv.util.Conexion;

public class Main extends Application {

    @Override
    public void start(Stage stage) {


        Connection conexion = Conexion.getInstancia().getConexion();

        if (conexion != null) {
            System.out.println("Base de datos conectada correctamente.");
        } else {
            System.out.println("No fue posible conectar con la base de datos.");
        }


        Label label = new Label("Librería Página Viva");

        StackPane root = new StackPane(label);

        Scene scene = new Scene(root, 700, 450);

        stage.setTitle("Librería Página Viva");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}