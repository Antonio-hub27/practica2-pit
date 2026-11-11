package telem;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public interface ICalculadorPrecio {

    int calcularPrecioAlquiler(LocalDate fechaInicio, LocalDate fechaFin, Vehiculo vehiculo);

    static int calcularDias(LocalDate fechaInicio, LocalDate fechaFin) {
        return (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin) + 1;
    }
}