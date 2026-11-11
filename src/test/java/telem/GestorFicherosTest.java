package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;

import org.json.JSONObject;
import org.junit.Test;

public class GestorFicherosTest {

    private final Cliente clara = new Cliente("Clara Videncia", "001");
    private final Cliente aitor = new Cliente("Aitor Menta", "002");

    private File ficheroTemporal() throws IOException {
        File f = File.createTempFile("alquileres", ".json");
        f.deleteOnExit();
        return f;
    }

    private GestorAlquileres gestorConAlquileres() {
        GestorAlquileres g = new GestorAlquileres();
        Vehiculo t = g.obtenerVehiculos("Turismo", Categoria.SUPERIOR)[0];
        Vehiculo m = g.obtenerVehiculos("Motocicleta", Categoria.NORMAL)[0];
        Vehiculo c = g.obtenerVehiculos("VehiculoIndustrial", Categoria.ESPECIAL)[0];

        g.agregarAlquiler(new Alquiler(t, clara, LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 3)));
        g.agregarAlquiler(new Alquiler(m, aitor, LocalDate.of(2026, 11, 5), LocalDate.of(2026, 11, 6)));
        g.agregarAlquiler(new Alquiler(c, clara, LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 20)));
        t.setDisponible(false);
        return g;
    }

    @Test
    public void guardarCreaUnFicheroJsonValido() throws IOException {
        File f = ficheroTemporal();
        gestorConAlquileres().guardarEnFichero(f.getPath());

        String contenido = new String(Files.readAllBytes(f.toPath()), StandardCharsets.UTF_8);
        JSONObject json = new JSONObject(contenido);
        assertEquals(6, json.getJSONArray("clientes").length());
        assertEquals(9, json.getJSONArray("vehiculos").length());
        assertEquals(3, json.getJSONArray("alquileres").length());
    }

    @Test
    public void guardarYCargarRecuperaLosAlquileres() throws IOException {
        File f = ficheroTemporal();
        gestorConAlquileres().guardarEnFichero(f.getPath());

        GestorAlquileres g2 = new GestorAlquileres();
        assertEquals(0, g2.obtenerAlquileres(clara).length);

        g2.cargarDeFichero(f.getPath());

        Alquiler[] deClara = g2.obtenerAlquileres(clara);
        assertEquals(2, deClara.length);
        assertEquals(1, g2.obtenerAlquileres(aitor).length);
        assertEquals("TSA2024", deClara[0].getVehiculo().getMatricula());
        assertEquals(LocalDate.of(2026, 11, 1), deClara[0].getFechaInicio());
        assertEquals(LocalDate.of(2026, 11, 3), deClara[0].getFechaFin());
        assertTrue(deClara[1].getVehiculo() instanceof VehiculoIndustrial);
    }

    @Test
    public void cargarConservaLaDisponibilidadDeLosVehiculos() throws IOException {
        File f = ficheroTemporal();
        gestorConAlquileres().guardarEnFichero(f.getPath());

        GestorAlquileres g2 = new GestorAlquileres();
        g2.cargarDeFichero(f.getPath());

        assertFalse(g2.obtenerVehiculos("Turismo", Categoria.SUPERIOR)[0].isDisponible());
        assertTrue(g2.obtenerVehiculos("Turismo", Categoria.NORMAL)[0].isDisponible());
    }

    @Test
    public void losAlquileresCargadosApuntanALosVehiculosDelGestor() throws IOException {
        File f = ficheroTemporal();
        gestorConAlquileres().guardarEnFichero(f.getPath());

        GestorAlquileres g2 = new GestorAlquileres();
        g2.cargarDeFichero(f.getPath());

        Vehiculo enGestor = g2.obtenerVehiculos("Turismo", Categoria.SUPERIOR)[0];
        Vehiculo enAlquiler = g2.obtenerAlquileres(clara)[0].getVehiculo();
        assertSame(enGestor, enAlquiler);
    }

    @Test
    public void cargarReemplazaLosAlquileresAnteriores() throws IOException {
        File f = ficheroTemporal();
        new GestorAlquileres().guardarEnFichero(f.getPath());

        GestorAlquileres g = gestorConAlquileres();
        g.cargarDeFichero(f.getPath());
        assertEquals(0, g.obtenerAlquileres(clara).length);
    }

    @Test(expected = IOException.class)
    public void cargarFicheroInexistenteLanzaExcepcion() throws IOException {
        new GestorAlquileres().cargarDeFichero("no_existe_este_fichero_12345.json");
    }
}
