package org.lpv.manager;

import org.lpv.model.Usuario;

public final class Sesion {

    private static Usuario usuarioActual;

    private Sesion() {
    }

    public static void iniciarSesion(Usuario usuario) {
        usuarioActual = usuario;
    }

    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public static boolean haySesion() {
        return usuarioActual != null;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }

    public static String getRol() {
        if (usuarioActual == null) {
            return null;
        }

        return usuarioActual.getRol();
    }
}