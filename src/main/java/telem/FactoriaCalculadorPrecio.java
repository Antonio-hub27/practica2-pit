package telem;

public class FactoriaCalculadorPrecio {

    public static ICalculadorPrecio getCalculador(PoliticasDePrecios politica) {
        switch (politica) {
            case Normal:
                return new CalculadorPrecioNormal();
            case TemporadaAlta:
                return new CalculadorPrecioTemporadaAlta();
            case ClienteFrecuente:
                return new CalculadorPrecioClienteFrecuente();
            case Oferton:
                return new CalculadorPrecioOferton();
            default:
                throw new IllegalArgumentException("Política de precios desconocida: " + politica);
        }
    }

    public static ICalculadorPrecio getCalculador(String nombrePolitica) {
        return getCalculador(PoliticasDePrecios.valueOf(nombrePolitica));
    }
}
