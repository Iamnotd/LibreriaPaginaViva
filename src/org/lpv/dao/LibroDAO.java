package org.lpv.dao;

import java.sql.SQLException;
import java.util.List;
import org.lpv.model.Libro;

public interface LibroDAO {

    Libro buscarPorIsbn(String isbn) throws SQLException;

    List<Libro> buscarPorTitulo(String titulo) throws SQLException;

    List<Libro> buscarPorAutor(String autor) throws SQLException;

}