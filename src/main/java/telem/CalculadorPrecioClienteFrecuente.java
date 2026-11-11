package telem;

import java.time.LocalDate;

public class CalculadorPrecioClienteFrecuente implements ICalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        int dias = ICalculadorPrecio.calcularDias(fechaInicio, fechaFin);
        int precioBase = vehiculo.getPrecioBase();

        int diasConDescuento20 = Math.min(dias, 3);
        int diasConDescuento40 = Math.max(dias - 3, 0);

        double total = diasConDescuento20 * precioBase * 0.80
                     + diasConDescuento40 * precioBase * 0.60;
        return (int) Math.round(total);
    }
}