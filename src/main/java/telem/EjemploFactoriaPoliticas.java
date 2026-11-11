package telem;

import java.time.LocalDate;

public class EjemploFactoriaPoliticas {

    public static void main(String[] args) {
        Vehiculo moto = new Motocicleta("2222BBB", Categoria.ESPECIAL, 2023, 650);
        LocalDate inicio = LocalDate.of(2026, 7, 1);
        LocalDate fin = LocalDate.of(2026, 7, 5);

        String[] nombres = {"Normal", "TemporadaAlta", "ClienteFrecuente", "Oferton"};

        System.out.println("Motocicleta ESPECIAL (" + moto.getPrecioBase() + " €/día), del " + inicio + " al " + fin);
        System.out.println("----------------------------------------------");
        for (String nombre : nombres) {
            ICalculadorPrecio calculador = FactoriaCalculadorPrecio.getCalculador(nombre);
            int precio = calculador.calcularPrecioAlquiler(inicio, fin, moto);
            System.out.printf("%-18s %-35s %5d €%n", nombre, calculador.getClass().getSimpleName(), precio);
        }
    }
}

