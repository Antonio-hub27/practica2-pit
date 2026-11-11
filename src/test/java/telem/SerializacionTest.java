package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.json.JSONObject;
import org.junit.Test;

public class SerializacionTest {

    @Test
    public void clienteIdaYVuelta() {
        Cliente original = new Cliente("Juan Pérez", "12345678A");
        Cliente copia = new Cliente(original.toString());
        assertEquals("Juan Pérez", copia.getNombre());
        assertEquals("12345678A", copia.getDni());
        assertEquals(original.toString(), copia.toString());
    }

    @Test
    public void factoriaCreaTurismo() {
        Turismo original = new Turismo("1111AAA", Categoria.ESPECIAL, 2022, 5, 450.0);
        original.setDisponible(true);
        Vehiculo v = FactoriaVehiculos.getVehiculo(original.toString());

        assertTrue(v instanceof Turismo);
        Turismo t = (Turismo) v;
        assertEquals("1111AAA", t.getMatricula());
        assertEquals(Categoria.ESPECIAL, t.getCategoria());
        assertEquals(2022, t.getAnnio());
        assertTrue(t.isDisponible());
        assertEquals(5, t.getPlazas());
        assertEquals(450.0, t.getCapacidadMaletero(), 0.001);
        assertEquals(original.toString(), t.toString());
    }

    @Test
    public void factoriaCreaMotocicleta() {
        Motocicleta original = new Motocicleta("2222BBB", Categoria.NORMAL, 2021, 600);
        Vehiculo v = FactoriaVehiculos.getVehiculo(original.toString());

        assertTrue(v instanceof Motocicleta);
        assertEquals(600, ((Motocicleta) v).getCilindrada());
        assertEquals(original.toString(), v.toString());
    }

    @Test
    public void factoriaCreaVehiculoIndustrial() {
        VehiculoIndustrial original = new VehiculoIndustrial("3333CCC", Categoria.SUPERIOR, 2019, 3500, 3);
        Vehiculo v = FactoriaVehiculos.getVehiculo(original.toString());

        assertTrue(v instanceof VehiculoIndustrial);
        VehiculoIndustrial vi = (VehiculoIndustrial) v;
        assertEquals(3500, vi.getCapacidadCarga());
        assertEquals(3, vi.getNumeroEjes());
        assertEquals(original.toString(), vi.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void factoriaRechazaTipoDesconocido() {
        FactoriaVehiculos.getVehiculo("{\"tipo\":\"Avion\",\"matricula\":\"X\"}");
    }

    @Test
    public void alquilerIdaYVuelta() {
        Vehiculo v = new Motocicleta("2222BBB", Categoria.NORMAL, 2021, 600);
        Cliente c = new Cliente("Ana López", "87654321B");
        LocalDate inicio = LocalDate.of(2026, 12, 1);
        LocalDate fin = LocalDate.of(2026, 12, 5);
        Alquiler original = new Alquiler(v, c, inicio, fin);

        Alquiler copia = new Alquiler(original.toString());

        assertEquals(inicio, copia.getFechaInicio());
        assertEquals(fin, copia.getFechaFin());
        assertEquals("Ana López", copia.getCliente().getNombre());
        assertTrue(copia.getVehiculo() instanceof Motocicleta);
        assertEquals("2222BBB", copia.getVehiculo().getMatricula());
        assertEquals(original.toString(), copia.toString());
    }

    @Test
    public void alquilerDesdeJsonEscritoAMano() {
        String json = "{\"cliente\":{\"nombre\":\"Juan Pérez\",\"dni\":\"123456789\"},"
                + "\"fechaInicio\":\"2026-10-09\","
                + "\"vehiculo\":{\"tipo\":\"Turismo\",\"precioBase\":50,\"categoria\":\"NORMAL\","
                + "\"matricula\":\"ABC123\",\"plazas\":5,\"annio\":2020,"
                + "\"capacidadMaletero\":400,\"disponible\":true},"
                + "\"fechaFin\":\"2026-10-12\"}";

        Alquiler a = new Alquiler(json);

        assertEquals(LocalDate.of(2026, 10, 9), a.getFechaInicio());
        assertEquals(LocalDate.of(2026, 10, 12), a.getFechaFin());
        assertEquals("123456789", a.getCliente().getDni());
        assertTrue(a.getVehiculo() instanceof Turismo);
        assertEquals(5, ((Turismo) a.getVehiculo()).getPlazas());
        assertTrue(a.getVehiculo().isDisponible());
    }

    @Test
    public void jsonDeAlquilerContieneElTipoDelVehiculo() {
        Alquiler a = new Alquiler(new VehiculoIndustrial("X", Categoria.NORMAL, 2020, 1000, 2),
                new Cliente("A", "B"), LocalDate.of(2026, 1, 1), LocalDate.of(2026, 1, 2));
        JSONObject json = new JSONObject(a.toString());
        assertEquals("VehiculoIndustrial", json.getJSONObject("vehiculo").getString("tipo"));
    }
}
