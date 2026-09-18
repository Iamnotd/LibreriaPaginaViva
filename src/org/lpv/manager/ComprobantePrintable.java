package org.lpv.manager;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class ComprobantePrintable
        implements Printable {

    private final String comprobante;

    public ComprobantePrintable(
            String comprobante
    ) {
        this.comprobante = comprobante;
    }

    @Override
    public int print(
            Graphics graphics,
            PageFormat pageFormat,
            int pageIndex
    ) throws PrinterException {

        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2 =
                (Graphics2D) graphics;

        g2.translate(
                pageFormat.getImageableX(),
                pageFormat.getImageableY()
        );

        g2.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        9
                )
        );

        int y = 20;

        String[] lineas =
                comprobante.split("\n");

        for (String linea : lineas) {

            g2.drawString(
                    linea,
                    0,
                    y
            );

            y += 12;
        }

        return PAGE_EXISTS;
    }
}