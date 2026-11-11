package telem;

import static org.junit.Assert.assertEquals;

import org.json.JSONObject;
import org.junit.Test;

/**
 * Tests unitarios de la clase Cliente.
 */
public class ClienteTest {

    @Test
    public void constructorYGetters() {
        Cliente c = new Cliente("Juan Pérez", "12345678A");
        assertEquals("Juan Pérez", c.getNombre());
        assertEquals("12345678A", c.getDni());
    }

    @Test
    public void toStringGeneraJsonConNombreYDni() {
        Cliente c = new Cliente("Ana López", "87654321B");
        JSONObject json = new JSONObject(c.toString());
        assertEquals("Ana López", json.getString("nombre"));
        assertEquals("87654321B", json.getString("dni"));
        assertEquals(2, json.length());
    }
}