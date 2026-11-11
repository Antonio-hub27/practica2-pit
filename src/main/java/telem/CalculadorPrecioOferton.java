package telem;

import java.time.LocalDate;

public class CalculadorPrecioOferton implements ICalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        int dias = ICalculadorPrecio.calcularDias(fechaInicio, fechaFin);
        double total = vehiculo.getPrecioBase() * 0.70 * dias;
        if (dias > 7) {
            total = total * 0.90;
        }
        return (int) Math.round(total);
    }
}