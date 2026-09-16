package org.lpv.model;

public enum Rol {

    ADMIN("admin"),
    BODEGA("bodega"),
    CAJERO("cajero");

    private final String valor;

    Rol(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }

    public static Rol desdeString(String rol) {

        if (rol == null) {
            throw new IllegalArgumentException(
                    "El rol no puede ser nulo."
            );
        }

        for (Rol r : values()) {

            if (r.valor.equalsIgnoreCase(rol.trim())) {
                return r;
            }
        }

        throw new IllegalArgumentException(
                "Rol no reconocido: " + rol
        );
    }
}