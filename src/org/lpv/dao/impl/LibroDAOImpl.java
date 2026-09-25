package org.lpv.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.lpv.dao.LibroDAO;
import org.lpv.model.Libro;
import org.lpv.util.Conexion;

public class LibroDAOImpl implements LibroDAO {

    private final Connection conexion;

    public LibroDAOImpl() {
        this.conexion = Conexion.getInstancia().getConexion();
    }

    @Override
    public void registrar(Libro libro) throws SQLException {

        String sql = """
                INSERT INTO libros (
                    isbn,
                    titulo,
                    fecha_publicacion,
                    precio,
                    id_categoria,
                    nit_editorial,
                    stock_actual,
                    stock_minimo,
                    activo
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setString(1, libro.getIsbn());
            ps.setString(2, libro.getTitulo());

            if (libro.getFechaPublicacion() != null) {
                ps.setDate(
                        3,
                        Date.valueOf(libro.getFechaPublicacion())
                );
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            ps.setBigDecimal(4, libro.getPrecio());

            if (libro.getIdCategoria() != null) {
                ps.setInt(5, libro.getIdCategoria());
            } else {
                ps.setNull(5, java.sql.Types.INTEGER);
            }

            if (libro.getNitEditorial() != null
                    && !libro.getNitEditorial().isBlank()) {

                ps.setString(
                        6,
                        libro.getNitEditorial()
                );

            } else {
                ps.setNull(
                        6,
                        java.sql.Types.VARCHAR
                );
            }

            ps.setInt(
                    7,
                    libro.getStockActual()
            );

            ps.setInt(
                    8,
                    libro.getStockMinimo()
            );

            ps.setBoolean(
                    9,
                    libro.isActivo()
            );

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se pudo registrar el libro."
                );
            }
        }
    }

    @Override
    public void actualizar(Libro libro)
            throws SQLException {

        String sql = """
                UPDATE libros
                SET titulo = ?,
                    fecha_publicacion = ?,
                    precio = ?,
                    id_categoria = ?,
                    nit_editorial = ?,
                    stock_actual = ?,
                    stock_minimo = ?,
                    activo = ?
                WHERE isbn = ?
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setString(
                    1,
                    libro.getTitulo()
            );

            if (libro.getFechaPublicacion() != null) {
                ps.setDate(
                        2,
                        Date.valueOf(
                                libro.getFechaPublicacion()
                        )
                );
            } else {
                ps.setNull(
                        2,
                        java.sql.Types.DATE
                );
            }

            ps.setBigDecimal(
                    3,
                    libro.getPrecio()
            );

            if (libro.getIdCategoria() != null) {
                ps.setInt(
                        4,
                        libro.getIdCategoria()
                );
            } else {
                ps.setNull(
                        4,
                        java.sql.Types.INTEGER
                );
            }

            if (libro.getNitEditorial() != null
                    && !libro.getNitEditorial().isBlank()) {

                ps.setString(
                        5,
                        libro.getNitEditorial()
                );

            } else {
                ps.setNull(
                        5,
                        java.sql.Types.VARCHAR
                );
            }

            ps.setInt(
                    6,
                    libro.getStockActual()
            );

            ps.setInt(
                    7,
                    libro.getStockMinimo()
            );

            ps.setBoolean(
                    8,
                    libro.isActivo()
            );

            ps.setString(
                    9,
                    libro.getIsbn()
            );

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se encontró el libro para actualizar: "
                        + libro.getIsbn()
                );
            }
        }
    }

    @Override
    public List<Libro> listarTodos()
            throws SQLException {

        List<Libro> libros =
                new ArrayList<>();

        String sql = """
                SELECT isbn, titulo, fecha_publicacion, precio,
                       id_categoria, nit_editorial, stock_actual,
                       stock_minimo, activo, fecha_actualizacion
                FROM libros
                ORDER BY titulo
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql);
             ResultSet rs =
                ps.executeQuery()) {

            while (rs.next()) {
                libros.add(
                        mapearLibro(rs)
                );
            }
        }

        return libros;
    }

    @Override
    public void cambiarEstado(
            String isbn,
            boolean activo
    ) throws SQLException {

        String sql = """
                UPDATE libros
                SET activo = ?
                WHERE isbn = ?
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setBoolean(
                    1,
                    activo
            );

            ps.setString(
                    2,
                    isbn
            );

            int filas = ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se encontró el libro: "
                        + isbn
                );
            }
        }
    }

    @Override
    public Libro buscarPorIsbn(
            String isbn
    ) throws SQLException {

        String sql = """
                SELECT isbn, titulo, fecha_publicacion, precio,
                       id_categoria, nit_editorial, stock_actual,
                       stock_minimo, activo, fecha_actualizacion
                FROM libros
                WHERE isbn = ?
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setString(
                    1,
                    isbn
            );

            try (ResultSet rs =
                    ps.executeQuery()) {

                if (rs.next()) {
                    return mapearLibro(rs);
                }
            }
        }

        return null;
    }

    @Override
    public List<Libro> buscarPorTitulo(
            String titulo
    ) throws SQLException {

        List<Libro> libros =
                new ArrayList<>();

        String sql = """
                SELECT isbn, titulo, fecha_publicacion, precio,
                       id_categoria, nit_editorial, stock_actual,
                       stock_minimo, activo, fecha_actualizacion
                FROM libros
                WHERE titulo LIKE ?
                ORDER BY titulo
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setString(
                    1,
                    "%" + titulo + "%"
            );

            try (ResultSet rs =
                    ps.executeQuery()) {

                while (rs.next()) {
                    libros.add(
                            mapearLibro(rs)
                    );
                }
            }
        }

        return libros;
    }

    @Override
    public List<Libro> buscarPorAutor(
            String autor
    ) throws SQLException {

        List<Libro> libros =
                new ArrayList<>();

        String sql = """
                SELECT DISTINCT
                       l.isbn,
                       l.titulo,
                       l.fecha_publicacion,
                       l.precio,
                       l.id_categoria,
                       l.nit_editorial,
                       l.stock_actual,
                       l.stock_minimo,
                       l.activo,
                       l.fecha_actualizacion
                FROM libros l
                INNER JOIN autores_libro al
                        ON l.isbn = al.isbn
                INNER JOIN autores a
                        ON al.id_autor = a.id_autor
                WHERE a.nombre_autor LIKE ?
                   OR a.apellido_autor LIKE ?
                   OR CONCAT(
                        a.nombre_autor,
                        ' ',
                        a.apellido_autor
                   ) LIKE ?
                ORDER BY l.titulo
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            String busqueda =
                    "%" + autor + "%";

            ps.setString(
                    1,
                    busqueda
            );

            ps.setString(
                    2,
                    busqueda
            );

            ps.setString(
                    3,
                    busqueda
            );

            try (ResultSet rs =
                    ps.executeQuery()) {

                while (rs.next()) {
                    libros.add(
                            mapearLibro(rs)
                    );
                }
            }
        }

        return libros;
    }

    @Override
    public void actualizarStock(
            String isbn,
            int cantidad,
            Connection conexion
    ) throws SQLException {

        String sql = """
                UPDATE libros
                SET stock_actual = stock_actual - ?
                WHERE isbn = ?
                  AND stock_actual >= ?
                """;

        try (PreparedStatement ps =
                conexion.prepareStatement(sql)) {

            ps.setInt(
                    1,
                    cantidad
            );

            ps.setString(
                    2,
                    isbn
            );

            ps.setInt(
                    3,
                    cantidad
            );

            int filas =
                    ps.executeUpdate();

            if (filas == 0) {
                throw new SQLException(
                        "No se pudo actualizar el stock del libro: "
                        + isbn
                );
            }
        }
    }

    private Libro mapearLibro(
            ResultSet rs
    ) throws SQLException {

        Libro libro =
                new Libro();

        libro.setIsbn(
                rs.getString("isbn")
        );

        libro.setTitulo(
                rs.getString("titulo")
        );

        Date fechaPublicacion =
                rs.getDate(
                        "fecha_publicacion"
                );

        if (fechaPublicacion != null) {

            libro.setFechaPublicacion(
                    fechaPublicacion.toLocalDate()
            );
        }

        libro.setPrecio(
                rs.getBigDecimal("precio")
        );

        int idCategoria =
                rs.getInt(
                        "id_categoria"
                );

        if (rs.wasNull()) {

            libro.setIdCategoria(
                    null
            );

        } else {

            libro.setIdCategoria(
                    idCategoria
            );
        }

        libro.setNitEditorial(
                rs.getString(
                        "nit_editorial"
                )
        );

        libro.setStockActual(
                rs.getInt(
                        "stock_actual"
                )
        );

        libro.setStockMinimo(
                rs.getInt(
                        "stock_minimo"
                )
        );

        libro.setActivo(
                rs.getBoolean(
                        "activo"
                )
        );

        Timestamp fechaActualizacion =
                rs.getTimestamp(
                        "fecha_actualizacion"
                );

        if (fechaActualizacion != null) {

            libro.setFechaActualizacion(
                    fechaActualizacion
                            .toLocalDateTime()
            );
        }

        return libro;
    }
}