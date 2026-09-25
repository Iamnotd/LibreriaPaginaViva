package org.lpv.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import org.lpv.model.Libro;

public interface LibroDAO {


    void registrar(Libro libro) throws SQLException;

    void actualizar(Libro libro) throws SQLException;

    List<Libro> listarTodos() throws SQLException;

    void cambiarEstado(
            String isbn,
            boolean activo
    ) throws SQLException;

    Libro buscarPorIsbn(String isbn) throws SQLException;

    List<Libro> buscarPorTitulo(String titulo) throws SQLException;

    List<Libro> buscarPorAutor(String autor) throws SQLException;

    void actualizarStock(
            String isbn,
            int cantidad,
            Connection conexion
    ) throws SQLException;
}