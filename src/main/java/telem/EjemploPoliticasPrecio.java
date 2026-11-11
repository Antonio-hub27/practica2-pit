package telem;

import java.time.LocalDate;

public class EjemploPoliticasPrecio {

    public static void main(String[] args) {
        Vehiculo turismo = new Turismo("1234ABC", Categoria.NORMAL, 2022, 5, 400.0);
        Cliente cliente = new Cliente("Ana López", "87654321B");
        LocalDate inicio = LocalDate.of(2026, 12, 1);
        LocalDate fin = LocalDate.of(2026, 12, 10);

        Alquiler alquiler = new Alquiler(turismo, cliente, inicio, fin);

        System.out.println("Vehículo: " + turismo.getTipo() + " " + turismo.getCategoria()
                + " (precio base " + turismo.getPrecioBase() + " €/día)");
        System.out.println("Alquiler del " + inicio + " al " + fin + " (10 días)");
        System.out.println("----------------------------------------------");

        for (PoliticasDePrecios politica : PoliticasDePrecios.values()) {
            int precio = alquiler.calcularPrecioAlquiler(inicio, fin, politica, turismo);
            System.out.printf("%-18s -> %5d €%n", politica, precio);
        }
    }
}