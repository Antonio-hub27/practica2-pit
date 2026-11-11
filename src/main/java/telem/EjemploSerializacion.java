package telem;

import java.time.LocalDate;

public class EjemploSerializacion {

    public static void main(String[] args) {
        Cliente cliente = new Cliente("Ana López", "87654321B");
        Vehiculo[] vehiculos = {
            new Turismo("1111AAA", Categoria.ESPECIAL, 2022, 5, 450.0),
            new Motocicleta("2222BBB", Categoria.NORMAL, 2021, 600),
            new VehiculoIndustrial("3333CCC", Categoria.SUPERIOR, 2019, 3500, 3)
        };

        for (Vehiculo v : vehiculos) {
            Alquiler original = new Alquiler(v, cliente, LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5));
            String json = original.toString();
            Alquiler copia = new Alquiler(json);

            System.out.println("JSON:  " + json);
            System.out.println("Vehículo reconstruido: " + copia.getVehiculo().getClass().getSimpleName()
                    + " " + copia.getVehiculo().getMatricula());
            System.out.println("¿Mismo JSON? " + json.equals(copia.toString()));
            System.out.println("----------------------------------------------");
        }
    }
}
