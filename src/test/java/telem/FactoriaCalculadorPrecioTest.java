package telem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class FactoriaCalculadorPrecioTest {

    @Test
    public void creaElCalculadorDeCadaPolitica() {
        assertTrue(FactoriaCalculadorPrecio.getCalculador(PoliticasDePrecios.Normal) instanceof CalculadorPrecioNormal);
        assertTrue(FactoriaCalculadorPrecio.getCalculador(PoliticasDePrecios.TemporadaAlta) instanceof CalculadorPrecioTemporadaAlta);
        assertTrue(FactoriaCalculadorPrecio.getCalculador(PoliticasDePrecios.ClienteFrecuente) instanceof CalculadorPrecioClienteFrecuente);
        assertTrue(FactoriaCalculadorPrecio.getCalculador(PoliticasDePrecios.Oferton) instanceof CalculadorPrecioOferton);
    }

    @Test
    public void creaElCalculadorAPartirDelNombre() {
        assertTrue(FactoriaCalculadorPrecio.getCalculador("Normal") instanceof CalculadorPrecioNormal);
        assertTrue(FactoriaCalculadorPrecio.getCalculador("TemporadaAlta") instanceof CalculadorPrecioTemporadaAlta);
        assertTrue(FactoriaCalculadorPrecio.getCalculador("ClienteFrecuente") instanceof CalculadorPrecioClienteFrecuente);
        assertTrue(FactoriaCalculadorPrecio.getCalculador("Oferton") instanceof CalculadorPrecioOferton);
    }

    @Test
    public void todasLasPoliticasTienenCalculador() {
        for (PoliticasDePrecios p : PoliticasDePrecios.values()) {
            assertTrue(FactoriaCalculadorPrecio.getCalculador(p) != null);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void nombreDesconocidoLanzaExcepcion() {
        FactoriaCalculadorPrecio.getCalculador("Gratis");
    }

    @Test
    public void elCalculadorCreadoCalculaBien() {
        Vehiculo turismo = new Turismo("A", Categoria.NORMAL, 2020, 5, 400.0);
        LocalDate inicio = LocalDate.of(2026, 10, 1);
        LocalDate fin = LocalDate.of(2026, 10, 10);
        assertEquals(500, FactoriaCalculadorPrecio.getCalculador("Normal").calcularPrecioAlquiler(inicio, fin, turismo));
        assertEquals(1000, FactoriaCalculadorPrecio.getCalculador("TemporadaAlta").calcularPrecioAlquiler(inicio, fin, turismo));
        assertEquals(330, FactoriaCalculadorPrecio.getCalculador("ClienteFrecuente").calcularPrecioAlquiler(inicio, fin, turismo));
        assertEquals(315, FactoriaCalculadorPrecio.getCalculador("Oferton").calcularPrecioAlquiler(inicio, fin, turismo));
    }
}