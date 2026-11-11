package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Tests unitarios de Vehiculo y sus subclases (Turismo, Motocicleta, VehiculoIndustrial).
 */
public class VehiculoTest {

    // ---------- Atributos comunes (heredados de Vehiculo) ----------

    @Test
    public void gettersComunes() {
        Vehiculo v = new Turismo("1234ABC", Categoria.ESPECIAL, 2021, 5, 450.0);
        assertEquals("1234ABC", v.getMatricula());
        assertEquals(Categoria.ESPECIAL, v.getCategoria());
        assertEquals(2021, v.getAnnio());
    }

    @Test
    public void disponibilidad() {
        Vehiculo v = new Motocicleta("5678DEF", Categoria.NORMAL, 2019, 600);
        assertFalse(v.isDisponible()); // por defecto, false
        v.setDisponible(true);
        assertTrue(v.isDisponible());
        v.setDisponible(false);
        assertFalse(v.isDisponible());
    }

    // ---------- Atributos propios de cada subclase ----------

    @Test
    public void gettersTurismo() {
        Turismo t = new Turismo("1111AAA", Categoria.NORMAL, 2020, 5, 400.0);
        assertEquals(5, t.getPlazas());
        assertEquals(400.0, t.getCapacidadMaletero(), 0.001);
    }

    @Test
    public void gettersMotocicleta() {
        Motocicleta m = new Motocicleta("2222BBB", Categoria.NORMAL, 2020, 125);
        assertEquals(125, m.getCilindrada());
    }

    @Test
    public void gettersVehiculoIndustrial() {
        VehiculoIndustrial vi = new VehiculoIndustrial("3333CCC", Categoria.NORMAL, 2020, 1000, 3);
        assertEquals(1000, vi.getCapacidadCarga());
        assertEquals(3, vi.getNumeroEjes());
    }

    // ---------- getTipo ----------

    @Test
    public void getTipoDevuelveElNombreDeLaSubclase() {
        assertEquals("Turismo", new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0).getTipo());
        assertEquals("Motocicleta", new Motocicleta("B", Categoria.NORMAL, 2020, 125).getTipo());
        assertEquals("VehiculoIndustrial",
                new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 3).getTipo());
    }

    // ---------- getPrecioBase: todas las combinaciones tipo x categoría ----------

    @Test
    public void precioBaseTurismo() {
        assertEquals(50, new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0).getPrecioBase());
        assertEquals(80, new Turismo("A", Categoria.ESPECIAL, 2020, 5, 400.0).getPrecioBase());
        assertEquals(100, new Turismo("A", Categoria.SUPERIOR, 2020, 5, 400.0).getPrecioBase());
    }

    @Test
    public void precioBaseMotocicleta() {
        assertEquals(30, new Motocicleta("B", Categoria.NORMAL, 2020, 125).getPrecioBase());
        assertEquals(50, new Motocicleta("B", Categoria.ESPECIAL, 2020, 125).getPrecioBase());
        assertEquals(70, new Motocicleta("B", Categoria.SUPERIOR, 2020, 125).getPrecioBase());
    }

    @Test
    public void precioBaseVehiculoIndustrial() {
        assertEquals(80, new VehiculoIndustrial("C", Categoria.NORMAL, 2020, 1000, 3).getPrecioBase());
        assertEquals(120, new VehiculoIndustrial("C", Categoria.ESPECIAL, 2020, 1000, 3).getPrecioBase());
        assertEquals(150, new VehiculoIndustrial("C", Categoria.SUPERIOR, 2020, 1000, 3).getPrecioBase());
    }

    // ---------- Serialización JSON ----------

    @Test
    public void toStringTurismoIncluyeTipoYPrecioBase() {
        Vehiculo v = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
        JSONObject json = new JSONObject(v.toString());
        assertEquals("Turismo", json.getString("tipo"));
        assertEquals(50, json.getInt("precioBase"));
        assertEquals("ABC123", json.getString("matricula"));
        assertEquals("NORMAL", json.getString("categoria"));
        assertEquals(2020, json.getInt("annio"));
        assertEquals(5, json.getInt("plazas"));
        assertEquals(400.0, json.getDouble("capacidadMaletero"), 0.001);
        assertFalse(json.getBoolean("disponible"));
    }

    @Test
    public void toStringVehiculoIndustrialIncluyeSusCampos() {
        Vehiculo v = new VehiculoIndustrial("XYZ789", Categoria.SUPERIOR, 2018, 2000, 4);
        JSONObject json = new JSONObject(v.toString());
        assertEquals("VehiculoIndustrial", json.getString("tipo"));
        assertEquals(150, json.getInt("precioBase"));
        assertEquals(2000, json.getInt("capacidadCarga"));
        assertEquals(4, json.getInt("numeroEjes"));
    }
}