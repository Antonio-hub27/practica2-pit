package telem;

import java.time.LocalDate;

public class CalculadorPrecioTemporadaAlta implements ICalculadorPrecio {

    @Override
    public int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo) {
        int dias = ICalculadorPrecio.calcularDias(fechaInicio, fechaFin);
        return vehiculo.getPrecioBase() * 2 * dias;
    }
}