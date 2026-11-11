package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Tests unitarios de la clase Alquiler.
 */
public class AlquilerTest {

    private final Vehiculo turismo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
    private final Cliente cliente = new Cliente("Juan Pérez", "12345678A");
    private final LocalDate inicio = LocalDate.of(2026, 10, 9);
    private final LocalDate fin = LocalDate.of(2026, 10, 12);

    // ---------- Constructor y getters ----------

    @Test
    public void constructorYGetters() {
        Alquiler a = new Alquiler(turismo, cliente, inicio, fin);
        assertSame(turismo, a.getVehiculo());
        assertSame(cliente, a.getCliente());
        assertEquals(inicio, a.getFechaInicio());
        assertEquals(fin, a.getFechaFin());
    }

    // ---------- calcularPrecioAlquiler ----------

    @Test
    public void precioMismoDiaCuentaUnDia() {
        Alquiler a = new Alquiler(turismo, cliente, inicio, inicio);
        // inicio == fin -> 1 día
        assertEquals(1 * Alquiler.PRECIO_BASE, a.calcularPrecioAlquiler(inicio, inicio));
    }

    @Test
    public void precioVariosDiasIncluyeElDiaDeInicio() {
        Alquiler a = new Alquiler(turismo, cliente, inicio, fin);
        // del 9 al 12 de octubre -> 4 días (9, 10, 11 y 12)
        assertEquals(4 * Alquiler.PRECIO_BASE, a.calcularPrecioAlquiler(inicio, fin));
    }

    @Test
    public void precioCruzandoCambioDeMes() {
        Alquiler a = new Alquiler(turismo, cliente, inicio, fin);
        LocalDate i = LocalDate.of(2026, 1, 30);
        LocalDate f = LocalDate.of(2026, 2, 2);
        // 30, 31 de enero, 1 y 2 de febrero -> 4 días
        assertEquals(4 * Alquiler.PRECIO_BASE, a.calcularPrecioAlquiler(i, f));
    }

    @Test
    public void precioAnioBisiesto() {
        Alquiler a = new Alquiler(turismo, cliente, inicio, fin);
        LocalDate i = LocalDate.of(2028, 2, 28);
        LocalDate f = LocalDate.of(2028, 3, 1);
        // 2028 es bisiesto: 28 y 29 de febrero, 1 de marzo -> 3 días
        assertEquals(3 * Alquiler.PRECIO_BASE, a.calcularPrecioAlquiler(i, f));
    }

    // ---------- agregarAlquiler / obtenerAlquileres ----------

    @Test
    public void agregarAlquilerLoGuardaEnElRegistro() {
        Alquiler a = new Alquiler(turismo, cliente, inicio, fin);
        Alquiler.agregarAlquiler(a);

        boolean encontrado = false;
        for (Alquiler x : Alquiler.obtenerAlquileres()) {
            if (x == a) {
                encontrado = true;
                break;
            }
        }
        assertTrue(encontrado);
    }

    // ---------- Serialización JSON ----------

    @Test
    public void toStringGeneraJsonConObjetosAnidados() {
        Alquiler a = new Alquiler(turismo, cliente, inicio, fin);
        JSONObject json = new JSONObject(a.toString());

        assertEquals("2026-10-09", json.getString("fechaInicio"));
        assertEquals("2026-10-12", json.getString("fechaFin"));

        JSONObject jCliente = json.getJSONObject("cliente");
        assertEquals("Juan Pérez", jCliente.getString("nombre"));
        assertEquals("12345678A", jCliente.getString("dni"));

        JSONObject jVehiculo = json.getJSONObject("vehiculo");
        assertEquals("Turismo", jVehiculo.getString("tipo"));
        assertEquals("ABC123", jVehiculo.getString("matricula"));
    }
}