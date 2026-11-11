package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class GestorAlquileresTest {

    private final Cliente clara = new Cliente("Clara Videncia", "001");
    private final Cliente aitor = new Cliente("Aitor Menta", "002");
    private final LocalDate inicio = LocalDate.of(2026, 11, 1);
    private final LocalDate fin = LocalDate.of(2026, 11, 3);

    @Test
    public void obtenerVehiculosPorTipo() {
        GestorAlquileres g = new GestorAlquileres();
        assertEquals(3, g.obtenerVehiculos("Turismo").length);
        assertEquals(3, g.obtenerVehiculos("Motocicleta").length);
        assertEquals(3, g.obtenerVehiculos("VehiculoIndustrial").length);
        assertEquals(0, g.obtenerVehiculos("Avion").length);
    }

    @Test
    public void obtenerVehiculosPorTipoYCategoria() {
        GestorAlquileres g = new GestorAlquileres();
        Vehiculo[] v = g.obtenerVehiculos("Turismo", Categoria.SUPERIOR);
        assertEquals(1, v.length);
        assertEquals("TSA2024", v[0].getMatricula());

        Vehiculo[] m = g.obtenerVehiculos("Motocicleta", Categoria.ESPECIAL);
        assertEquals(1, m.length);
        assertEquals("MEA2026", m[0].getMatricula());

        Vehiculo[] c = g.obtenerVehiculos("VehiculoIndustrial", Categoria.NORMAL);
        assertEquals(1, c.length);
        assertEquals("CNA2019", c[0].getMatricula());
    }

    @Test
    public void losVehiculosEmpiezanDisponibles() {
        GestorAlquileres g = new GestorAlquileres();
        for (String tipo : new String[] {"Turismo", "Motocicleta", "VehiculoIndustrial"}) {
            for (Vehiculo v : g.obtenerVehiculos(tipo)) {
                assertTrue(v.isDisponible());
            }
        }
    }

    @Test
    public void obtenerAlquileresDeUnCliente() {
        GestorAlquileres g = new GestorAlquileres();
        Vehiculo t = g.obtenerVehiculos("Turismo")[0];
        Vehiculo m = g.obtenerVehiculos("Motocicleta")[0];

        g.agregarAlquiler(new Alquiler(t, clara, inicio, fin));
        g.agregarAlquiler(new Alquiler(m, aitor, inicio, fin));
        g.agregarAlquiler(new Alquiler(m, clara, inicio, fin));

        assertEquals(2, g.obtenerAlquileres(clara).length);
        assertEquals(1, g.obtenerAlquileres(aitor).length);
        assertEquals(0, g.obtenerAlquileres(new Cliente("Lola Mento", "003")).length);
    }

    @Test
    public void obtenerAlquileresSinAlquileresDevuelveArrayVacio() {
        GestorAlquileres g = new GestorAlquileres();
        assertEquals(0, g.obtenerAlquileres(clara).length);
    }

    @Test
    public void clienteFrecuenteSegunUmbral() {
        GestorAlquileres g = new GestorAlquileres();
        Vehiculo t = g.obtenerVehiculos("Turismo")[0];

        assertFalse(g.esClienteFrecuente(clara, 3));
        g.agregarAlquiler(new Alquiler(t, clara, inicio, fin));
        g.agregarAlquiler(new Alquiler(t, clara, inicio, fin));
        assertFalse(g.esClienteFrecuente(clara, 3));
        g.agregarAlquiler(new Alquiler(t, clara, inicio, fin));
        assertTrue(g.esClienteFrecuente(clara, 3));
        assertFalse(g.esClienteFrecuente(aitor, 3));
    }
}
