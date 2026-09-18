package org.lpv.manager;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.lpv.model.DetalleVenta;
import org.lpv.model.Venta;

public class ComprobanteGenerador {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm"
            );

    public String generar(
            Venta venta,
            List<DetalleVenta> detalles
    ) {

        if (venta == null) {
            throw new IllegalArgumentException(
                    "La venta no puede ser nula."
            );
        }

        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta no tiene detalles."
            );
        }

        StringBuilder comprobante =
                new StringBuilder();

        comprobante.append(
                "========================================\n"
        );

        comprobante.append(
                "          LIBRERÍA PÁGINA VIVA\n"
        );

        comprobante.append(
                "              COMPROBANTE\n"
        );

        comprobante.append(
                "========================================\n"
        );

        comprobante.append(
                "Venta No.: "
        );

        comprobante.append(
                venta.getIdVenta()
        );

        comprobante.append("\n");

        if (venta.getFechaVenta() != null) {

            comprobante.append(
                    "Fecha: "
            );

            comprobante.append(
                    venta.getFechaVenta()
                            .format(FORMATO_FECHA)
            );

            comprobante.append("\n");
        }

        comprobante.append(
                "----------------------------------------\n"
        );

        comprobante.append(
                String.format(
                        "%-13s %5s %10s",
                        "ISBN",
                        "Cant.",
                        "Subtotal"
                )
        );

        comprobante.append("\n");

        comprobante.append(
                "----------------------------------------\n"
        );

        for (DetalleVenta detalle : detalles) {

            comprobante.append(
                    String.format(
                            "%-13s %5d %10.2f",
                            detalle.getIsbn(),
                            detalle.getCantidad(),
                            detalle.getSubtotal()
                    )
            );

            comprobante.append("\n");

            comprobante.append(
                    String.format(
                            "Precio unitario: Q %.2f",
                            detalle.getPrecioUnitario()
                    )
            );

            comprobante.append("\n");
        }

        comprobante.append(
                "----------------------------------------\n"
        );

        BigDecimal subtotal =
                venta.getSubtotal();

        BigDecimal descuento =
                venta.getDescuento();

        BigDecimal total =
                venta.getTotal();

        if (subtotal == null) {
            subtotal = BigDecimal.ZERO;
        }

        if (descuento == null) {
            descuento = BigDecimal.ZERO;
        }

        if (total == null) {
            total = BigDecimal.ZERO;
        }

        comprobante.append(
                String.format(
                        "%-25s Q %10.2f",
                        "Subtotal:",
                        subtotal
                )
        );

        comprobante.append("\n");

        comprobante.append(
                String.format(
                        "%-25s Q %10.2f",
                        "Descuento:",
                        descuento
                )
        );

        comprobante.append("\n");

        comprobante.append(
                String.format(
                        "%-25s Q %10.2f",
                        "TOTAL:",
                        total
                )
        );

        comprobante.append("\n");

        comprobante.append(
                "========================================\n"
        );

        comprobante.append(
                "        Gracias por su compra\n"
        );

        comprobante.append(
                "========================================\n"
        );

        return comprobante.toString();
    }
}