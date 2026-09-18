package org.lpv.manager;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class ImpresionManager {

    public void imprimir(
            String comprobante
    ) throws PrinterException {

        if (comprobante == null ||
                comprobante.isBlank()) {

            throw new IllegalArgumentException(
                    "El comprobante está vacío."
            );
        }

        PrinterJob trabajo =
                PrinterJob.getPrinterJob();

        trabajo.setPrintable(
                new ComprobantePrintable(
                        comprobante
                )
        );

        if (trabajo.printDialog()) {
            trabajo.print();
        }
    }
}