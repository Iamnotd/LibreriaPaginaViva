package org.lpv.model;

public class Usuario {

    private int id;
    private String username;
    private String passwordHash;
    private String nombre;
    private String apellido;
    private String correo;
    private String rol;
    private boolean activo;

    public Usuario() {
    }

    public Usuario(int id, String username, String passwordHash,
                   String nombre, String apellido, String correo,
                   String rol, boolean activo) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.rol = rol;
        this.activo = activo;
    }

    public Usuario(String username, String passwordHash,
                   String nombre, String apellido, String correo,
                   String rol, boolean activo) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return username + " - " + rol;
    }
}