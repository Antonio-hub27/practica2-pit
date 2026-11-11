package telem;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

public class CalculadorPrecioTest {

    private final Vehiculo turismo = new Turismo("ABC123", Categoria.NORMAL, 2020, 5, 400.0);
    private final Vehiculo moto = new Motocicleta("MOTO1", Categoria.NORMAL, 2020, 125);
    private final Cliente cliente = new Cliente("Juan Pérez", "12345678A");

    private final LocalDate dia1 = LocalDate.of(2026, 10, 1);

    private LocalDate finTras(int dias) {
        return dia1.plusDays(dias - 1);
    }

    @Test
    public void calcularDiasIncluyeInicioYFin() {
        assertEquals(1, ICalculadorPrecio.calcularDias(dia1, dia1));
        assertEquals(3, ICalculadorPrecio.calcularDias(dia1, finTras(3)));
    }

    @Test
    public void normal() {
        ICalculadorPrecio c = new CalculadorPrecioNormal();
        assertEquals(50, c.calcularPrecioAlquiler(dia1, finTras(1), turismo));
        assertEquals(150, c.calcularPrecioAlquiler(dia1, finTras(3), turismo));
        assertEquals(500, c.calcularPrecioAlquiler(dia1, finTras(10), turismo));
        assertEquals(90, c.calcularPrecioAlquiler(dia1, finTras(3), moto));
    }

    @Test
    public void temporadaAltaDuplicaElPrecioBase() {
        ICalculadorPrecio c = new CalculadorPrecioTemporadaAlta();
        assertEquals(100, c.calcularPrecioAlquiler(dia1, finTras(1), turismo));
        assertEquals(300, c.calcularPrecioAlquiler(dia1, finTras(3), turismo));
        assertEquals(60, c.calcularPrecioAlquiler(dia1, finTras(1), moto));
    }

    @Test
    public void ofertonHastaSieteDias() {
        ICalculadorPrecio c = new CalculadorPrecioOferton();
        assertEquals(35, c.calcularPrecioAlquiler(dia1, finTras(1), turismo));
        assertEquals(105, c.calcularPrecioAlquiler(dia1, finTras(3), turismo));
        assertEquals(245, c.calcularPrecioAlquiler(dia1, finTras(7), turismo));
    }

    @Test
    public void ofertonMasDeSieteDiasAplicaDescuentoExtra() {
        ICalculadorPrecio c = new CalculadorPrecioOferton();
        assertEquals(252, c.calcularPrecioAlquiler(dia1, finTras(8), turismo));
        assertEquals(315, c.calcularPrecioAlquiler(dia1, finTras(10), turismo));
    }

    @Test
    public void clienteFrecuenteTresPrimerosDias() {
        ICalculadorPrecio c = new CalculadorPrecioClienteFrecuente();
        assertEquals(40, c.calcularPrecioAlquiler(dia1, finTras(1), turismo));
        assertEquals(120, c.calcularPrecioAlquiler(dia1, finTras(3), turismo));
    }

    @Test
    public void clienteFrecuenteAPartirDelCuartoDia() {
        ICalculadorPrecio c = new CalculadorPrecioClienteFrecuente();
        assertEquals(150, c.calcularPrecioAlquiler(dia1, finTras(4), turismo));
        assertEquals(330, c.calcularPrecioAlquiler(dia1, finTras(10), turismo));
    }

    @Test
    public void alquilerAplicaLaPoliticaIndicada() {
        Alquiler a = new Alquiler(turismo, cliente, dia1, finTras(10));
        LocalDate fin = finTras(10);
        assertEquals(500, a.calcularPrecioAlquiler(dia1, fin, PoliticasDePrecios.Normal, turismo));
        assertEquals(1000, a.calcularPrecioAlquiler(dia1, fin, PoliticasDePrecios.TemporadaAlta, turismo));
        assertEquals(315, a.calcularPrecioAlquiler(dia1, fin, PoliticasDePrecios.Oferton, turismo));
        assertEquals(330, a.calcularPrecioAlquiler(dia1, fin, PoliticasDePrecios.ClienteFrecuente, turismo));
    }

    @Test
    public void elPrecioDependeDelVehiculo() {
        Alquiler a = new Alquiler(moto, cliente, dia1, finTras(2));
        assertEquals(60, a.calcularPrecioAlquiler(dia1, finTras(2), PoliticasDePrecios.Normal, moto));
        Vehiculo turismoSuperior = new Turismo("SUP1", Categoria.SUPERIOR, 2024, 5, 500.0);
        assertEquals(200, a.calcularPrecioAlquiler(dia1, finTras(2), PoliticasDePrecios.Normal, turismoSuperior));
    }
}
