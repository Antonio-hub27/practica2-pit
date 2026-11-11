package telem;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;

public class GestorAlquileres {

    private int contadorAlquileres = 0;
    private Alquiler[] alquileres = new Alquiler[1000];

    // Para simplificar, vamos a definir un array de clientes y
    // otro de vehículos dentro del gestor.
    // En una aplicación real estarían en una base de datos.
    private Cliente[] clientes = new Cliente[10];
    private Vehiculo[] vehiculos = new Vehiculo[100];


    public GestorAlquileres() {
        inicializarClientes();
        inicializarVehiculos();
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // INICIALIZACIÓN DE CLIENTES Y VEHÍCULOS DE EJEMPLO

    // Definimos métodos privados para inicializar clientes y vehículos de ejemplo
    private void inicializarClientes() {
        clientes[0] = new Cliente("Clara Videncia", "001");
        clientes[1] = new Cliente("Aitor Menta", "002");
        clientes[2] = new Cliente("Lola Mento", "003");
        clientes[3] = new Cliente("Armando Guerra", "004");
        clientes[4] = new Cliente("Ana Lítica", "005");
        clientes[5] = new Cliente("Carmelo Cotón", "006");
    }

    private void inicializarVehiculos() {
        vehiculos[0] = new Turismo("TNA2020", Categoria.NORMAL, 2020, 5, 450.0);
        vehiculos[1] = new Motocicleta("MNA2021", Categoria.NORMAL, 2021, 200);
        vehiculos[2] = new VehiculoIndustrial("CNA2019", Categoria.NORMAL, 2019, 1400, 2);
        vehiculos[3] = new Turismo("TSA2024", Categoria.SUPERIOR, 2024, 5, 550.0);
        vehiculos[4] = new Motocicleta("MSA2021", Categoria.SUPERIOR, 2021, 500);
        vehiculos[5] = new VehiculoIndustrial("CSA2025", Categoria.SUPERIOR, 2025, 2400, 3);
        vehiculos[6] = new Turismo("TEA2026", Categoria.ESPECIAL, 2026, 7, 650.0);
        vehiculos[7] = new Motocicleta("MEA2026", Categoria.ESPECIAL, 2026, 1000);
        vehiculos[8] = new VehiculoIndustrial("CEA2025", Categoria.ESPECIAL, 2025, 4400, 5);

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                vehiculo.setDisponible(true);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR VEHÍCULOS

     public Vehiculo[] obtenerVehiculos(String tipo) {

        Vehiculo[] vehiculosFiltrados = new Vehiculo[vehiculos.length];
        int contador = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                if (vehiculo.getTipo().equals(tipo)) {
                    vehiculosFiltrados[contador++] = vehiculo;
                }
            }
        }

        // Ajustar el tamaño del array para que solo contenga los vehículos encontrados
        Vehiculo[] resultado = new Vehiculo[contador];
        System.arraycopy(vehiculosFiltrados, 0, resultado, 0, contador);
        return resultado;
    }

    /**
     * Devuelve un array con los vehículos del tipo y categoría especificados.
     * @param tipo
     * @param categoria
     * @return lista de vehículos del tipo y categoría especificados
     */
    public Vehiculo[] obtenerVehiculos(String tipo, Categoria categoria) {

        Vehiculo[] vehiculosFiltrados = new Vehiculo[vehiculos.length];
        int contador = 0;

        for (Vehiculo vehiculo : vehiculos) {
            if (vehiculo != null) {
                if (vehiculo.getTipo().equals(tipo) && vehiculo.getCategoria() == categoria) {
                    vehiculosFiltrados[contador++] = vehiculo;
                }
            }
        }

        Vehiculo[] resultado = new Vehiculo[contador];
        System.arraycopy(vehiculosFiltrados, 0, resultado, 0, contador);
        return resultado;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // MÉTODOS PARA GESTIONAR ALQUILERES

    /**
     * Agrega un nuevo alquiler al array de alquileres.
     * @param alquiler
     */
    public void agregarAlquiler(Alquiler alquiler) {
        if (contadorAlquileres < alquileres.length) {
            alquileres[contadorAlquileres] = alquiler;
            contadorAlquileres++;
        } else {
            System.out.println("No se pueden agregar más alquileres. Capacidad máxima alcanzada.");
        }
    }

    /**
     * Devuelve un array con todos los alquileres registrados en el gestor.
     * @return
     */
    public Alquiler[] obtenerAlquileres() {
        return alquileres;
    }

    public Alquiler[] obtenerAlquileres(Cliente cliente) {

        Alquiler[] alquileresCliente = new Alquiler[contadorAlquileres];
        int contador = 0;

        for (int i = 0; i < contadorAlquileres; i++) {
            if (alquileres[i].getCliente().getDni().equals(cliente.getDni())) {
                alquileresCliente[contador++] = alquileres[i];
            }
        }

        Alquiler[] resultado = new Alquiler[contador];
        System.arraycopy(alquileresCliente, 0, resultado, 0, contador);
        return resultado;
    }


    /**
     * Informa si un cliente es cliente frecuente en función del número de alquileres previos.
     * @param cliente cliente a evaluar
     * @param umbral número mínimo de alquileres previos que se consideran para determinar si es cliente frecuente
     * @return
     */
    boolean esClienteFrecuente(Cliente cliente, int umbral) {

        return obtenerAlquileres(cliente).length >= umbral;
    }


    public void guardarEnFichero(String ruta) throws IOException {
        JSONArray jClientes = new JSONArray();
        for (Cliente c : clientes) {
            if (c != null) {
                jClientes.put(new JSONObject(c.toString()));
            }
        }

        JSONArray jVehiculos = new JSONArray();
        for (Vehiculo v : vehiculos) {
            if (v != null) {
                jVehiculos.put(new JSONObject(v.toString()));
            }
        }

        JSONArray jAlquileres = new JSONArray();
        for (int i = 0; i < contadorAlquileres; i++) {
            jAlquileres.put(new JSONObject(alquileres[i].toString()));
        }

        JSONObject datos = new JSONObject();
        datos.put("clientes", jClientes);
        datos.put("vehiculos", jVehiculos);
        datos.put("alquileres", jAlquileres);

        Files.write(Paths.get(ruta), datos.toString(2).getBytes(StandardCharsets.UTF_8));
    }

    public void cargarDeFichero(String ruta) throws IOException {
        String contenido = new String(Files.readAllBytes(Paths.get(ruta)), StandardCharsets.UTF_8);
        JSONObject datos = new JSONObject(contenido);

        Cliente[] nuevosClientes = new Cliente[clientes.length];
        JSONArray jClientes = datos.getJSONArray("clientes");
        for (int i = 0; i < jClientes.length() && i < nuevosClientes.length; i++) {
            nuevosClientes[i] = new Cliente(jClientes.getJSONObject(i).toString());
        }

        Vehiculo[] nuevosVehiculos = new Vehiculo[vehiculos.length];
        JSONArray jVehiculos = datos.getJSONArray("vehiculos");
        for (int i = 0; i < jVehiculos.length() && i < nuevosVehiculos.length; i++) {
            nuevosVehiculos[i] = FactoriaVehiculos.getVehiculo(jVehiculos.getJSONObject(i).toString());
        }

        clientes = nuevosClientes;
        vehiculos = nuevosVehiculos;
        alquileres = new Alquiler[alquileres.length];
        contadorAlquileres = 0;

        JSONArray jAlquileres = datos.getJSONArray("alquileres");
        for (int i = 0; i < jAlquileres.length(); i++) {
            Alquiler leido = new Alquiler(jAlquileres.getJSONObject(i).toString());
            Vehiculo vehiculo = buscarVehiculo(leido.getVehiculo().getMatricula());
            Cliente cliente = buscarCliente(leido.getCliente().getDni());
            if (vehiculo == null) {
                vehiculo = leido.getVehiculo();
            }
            if (cliente == null) {
                cliente = leido.getCliente();
            }
            agregarAlquiler(new Alquiler(vehiculo, cliente, leido.getFechaInicio(), leido.getFechaFin()));
        }
    }

    private Vehiculo buscarVehiculo(String matricula) {
        for (Vehiculo v : vehiculos) {
            if (v != null && v.getMatricula().equals(matricula)) {
                return v;
            }
        }
        return null;
    }

    private Cliente buscarCliente(String dni) {
        for (Cliente c : clientes) {
            if (c != null && c.getDni().equals(dni)) {
                return c;
            }
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////////////////////////
    // INTERFAZ DE USUARIO (CONSOLA) Y LÓGICA DE NEGOCIO

    // La interfaz de usuario y la lógica de negocio suelen implementarse por separado.
    // Aquí se integran ambas por simplicidad, pero en una aplicación real hay que separarlas.


    public void menuPrincipal(){

        boolean salir = false;

        while(!salir){

            // Se da oportunidad de salir inmediatamente de la app.
            System.out.println("Gestíon de Alquileres");
            System.out.println("¿Desea salir de la app (S/N)?");
            System.out.println("(G) guardar datos en fichero, (C) cargar datos de fichero");

            char s = KeyBoard.readChar();
            if (s == 's' || s == 'S'){
                System.out.println("Bye ...");
                return;
            }
            if (s == 'g' || s == 'G'){
                menuGuardar();
                continue;
            }
            if (s == 'c' || s == 'C'){
                menuCargar();
                continue;
            }

            // Primero se identifica al cliente:
            Cliente cliente = identificarCliente();

            // Ahora el cliente puede elegir una opción o salir de la aplicación.
            if (cliente != null) {
                System.out.println("Sr " + cliente.getNombre() + " Introduzca una opción");
                System.out.println("(1) seleccionar coche para alquilar");
                System.out.println("(2) entregar un coche alquilado");
                System.out.println("(3) listar su histórico de alquileres");

                int opcion = KeyBoard.readInt();
                System.out.println("opción escogida: " + opcion);

                switch (opcion) {
                    case 1:
                        Vehiculo v = seleccionarVehiculo(cliente);
                        if (v != null && v.isDisponible()){
                            // para simplificar la IU se supone que el alquiler comienza "ahora"
                            // y el usuario especifica duración en días.
                            System.out.println("¿cuántos días desea alquilar el vehículo?");
                            int dias = KeyBoard.readInt();
                            if (dias <= 0 || dias == KeyBoard.INT_ERR) {
                                System.out.println("Número de días inválido.");
                                break;
                            }
                            LocalDate inicio = LocalDate.now();
                            LocalDate fin = inicio.plusDays(dias - 1);

                            //Comprobamos si el cliente es usuario frecuente para aplicarle
                            // la tarifa que le corresponde.
                            boolean usuarioFrecuente = this.esClienteFrecuente(cliente, 3);

                            // Creamos y registramos el alquiler
                            Alquiler q = new Alquiler(v, cliente, inicio, fin);
                            this.agregarAlquiler(q);
                            v.setDisponible(false);

                            // Calculamos precio.
                            // Aquí habría que determinar la política de precios aplicable según el tipo de cliente y la fecha.
                            // Para simplificar, se aplicará la política de cliente frecuente si el cliente tiene más de 3 alquileres previos,
                            // y la política normal en caso contrario.

                            PoliticasDePrecios politica;
                            if (usuarioFrecuente){

                                // aplicamos tarifa de cliente frecuente.
                                System.out.println("Alquiler realizado. Tarifa Usuario frecuente.");
                                politica = PoliticasDePrecios.ClienteFrecuente;
                            }
                            else{
                                // aplicamos tarifa normal.
                                System.out.println("Alquiler realizado. Tarifa Normal.");
                                politica = PoliticasDePrecios.Normal;
                            }

                            int precio = q.calcularPrecioAlquiler(inicio, fin, politica, v);
                            System.out.println("Vehículo " + v.getMatricula() + " del " + inicio + " al " + fin
                                    + " (" + dias + " días). Precio: " + precio + " euros");

                        }
                        else{
                            System.out.println("Debe seleccionar un vehículo disponible");
                        }
                        break;
                    case 2:
                        entregarVehiculo(cliente);
                        break;
                    case 3:
                        Alquiler alqs [] = obtenerAlquileres(cliente);
                        if (alqs.length == 0) {
                            System.out.println("No tiene alquileres registrados.");
                        }
                        for (Alquiler q: alqs){
                            System.out.println(q);
                        }
                        break;
                    default:
                        break;
                }
            }
            else{
                System.out.println("Debe identificarse correctamente o salir de la app");
            }
        }
    }

    private String leerNombreFichero() {
        String ruta = KeyBoard.readString();
        while (ruta != null && ruta.trim().isEmpty()) {
            ruta = KeyBoard.readString();
        }
        return ruta;
    }

    private void menuGuardar() {
        System.out.println("Nombre del fichero donde guardar (por ejemplo, datos.json):");
        String ruta = leerNombreFichero();
        if (ruta == null) {
            System.out.println("Nombre de fichero no válido.");
            return;
        }
        try {
            guardarEnFichero(ruta.trim());
            System.out.println("Datos guardados en " + ruta.trim());
        } catch (IOException e) {
            System.out.println("No se pudo guardar el fichero: " + e.getMessage());
        }
    }

    private void menuCargar() {
        System.out.println("Nombre del fichero a cargar (por ejemplo, datos.json):");
        String ruta = leerNombreFichero();
        if (ruta == null) {
            System.out.println("Nombre de fichero no válido.");
            return;
        }
        try {
            cargarDeFichero(ruta.trim());
            System.out.println("Datos cargados de " + ruta.trim() + ": " + contadorAlquileres + " alquileres.");
        } catch (IOException e) {
            System.out.println("No se pudo leer el fichero: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("El fichero no tiene un formato válido: " + e.getMessage());
        }
    }

    public void entregarVehiculo(Cliente cliente) {

        Alquiler[] alqs = obtenerAlquileres(cliente);
        Alquiler[] pendientes = new Alquiler[alqs.length];
        int contador = 0;
        for (Alquiler q : alqs) {
            if (!q.getVehiculo().isDisponible()) {
                pendientes[contador++] = q;
            }
        }

        if (contador == 0) {
            System.out.println("No tiene ningún vehículo pendiente de entregar.");
            return;
        }

        System.out.println("Vehículos pendientes de entregar:");
        for (int i = 0; i < contador; i++) {
            System.out.println(i + " ---> " + pendientes[i].getVehiculo().getTipo() + " "
                    + pendientes[i].getVehiculo().getMatricula()
                    + " (del " + pendientes[i].getFechaInicio() + " al " + pendientes[i].getFechaFin() + ")");
        }
        System.out.println("Introduzca el número del vehículo que entrega:");
        int seleccion = KeyBoard.readInt();

        if (seleccion < 0 || seleccion >= contador) {
            System.out.println("Selección inválida.");
            return;
        }

        pendientes[seleccion].getVehiculo().setDisponible(true);
        System.out.println("Vehículo " + pendientes[seleccion].getVehiculo().getMatricula() + " entregado. Gracias.");
    }

    public Cliente identificarCliente(){

        boolean seleccionado = false;
        Cliente cliente = null;

        while (!seleccionado) {

            System.out.println("Bienvenido al Gestor de Alquileres");
            for (int i = 0; i < clientes.length; i++) {
                if (clientes[i] != null) {
                    System.out.println(i + " ---> " + clientes[i].getNombre() + " - " + clientes[i].getDni());
                }
            }
            System.out.println("Identífiquese como cliente con su número de cliente (-1 para salir):");
            int numeroCliente = KeyBoard.readInt();
            if (numeroCliente == -1) {
                return null;
            }
            else if (numeroCliente < 0 || numeroCliente >= clientes.length || clientes[numeroCliente] == null) {
                System.out.println("identificarCliente:Número de cliente inválido.");
                return null;
            }
            else{
                seleccionado = true;
                cliente = clientes[numeroCliente];
            }
        }
        return cliente;
    }

    public Vehiculo seleccionarVehiculo(Cliente cliente){
        boolean seleccionado = false;
        Vehiculo vehiculo = null;

        while (!seleccionado) {

            System.out.println("Estimado Sr./Sra. " + cliente.getNombre());
            System.out.println("Introduzca el número correspondiente al tipo de vehículo que desea alquilar:");
            System.out.println("1. Turismo, 2. Motocicleta, 3. Vehículo Industrial: ");

            int tipoVehiculo = KeyBoard.readInt();
            String tipo = "";
            Vehiculo [] elegibles = new Vehiculo[100];

            switch (tipoVehiculo) {
                case 1:
                    System.out.println("Ha seleccionado Turismo.");
                    tipo = "Turismo";
                    break;
                case 2:
                    System.out.println("Ha seleccionado Motocicleta.");
                    tipo = "Motocicleta";
                    break;
                case 3:
                    System.out.println("Ha seleccionado Vehículo Industrial.");
                    tipo = "VehiculoIndustrial";
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, seleccione un tipo de vehículo válido.");
                    continue; // Volver a pedir la selección
            }

            System.out.println("Vehiculos disponibles");

            System.out.println("Estimado Sr./Sra. " + cliente.getNombre() + "\n seleccione categoría.");
            System.out.println("Introduzca el número correspondiente a la categoría que desea alquilar:");
            System.out.println("1. Normal, 2. Superior, 3. Especial, 4.Cualquiera: ");

            int categoriaVehiculo = KeyBoard.readInt();

            switch (categoriaVehiculo) {
                case 1:
                    System.out.println("Ha seleccionado Categoría Normal.");
                    elegibles = obtenerVehiculos(tipo, Categoria.NORMAL);
                    break;
                case 2:
                    System.out.println("Ha seleccionado Categoría Superior.");
                    elegibles = obtenerVehiculos(tipo, Categoria.SUPERIOR);
                    break;
                case 3:
                    System.out.println("Ha seleccionado Categoría Especial.");
                    elegibles = obtenerVehiculos(tipo, Categoria.ESPECIAL);
                    break;
                default:
                    elegibles = obtenerVehiculos(tipo);
                    break;
            }

            if (elegibles.length == 0) {
                System.out.println("No hay vehículos de ese tipo y categoría.");
                return null;
            }

            System.out.println("Coches elegibles");

            for(int i = 0; i < elegibles.length; i++){
                System.out.println(i + " ---> " + elegibles[i]);
            }

            System.out.println("Introduzca número coche (sólo si está disponible)");
            int seleccion = KeyBoard.readInt();
            if (seleccion < 0 || seleccion >= elegibles.length) {
                System.out.println("Número de coche inválido.");
                return null;
            }
            vehiculo = elegibles[seleccion];
            seleccionado = true;
        }
        return vehiculo;
    }

    public void listarAlquileres (){
        System.out.println("Listado de alquileres");
        for (int i = 0; i < contadorAlquileres; i++){
            System.out.println(alquileres[i]);
        }
        System.out.println();
    }

    public void listarVehiculos(){
        System.out.println("Listado de vehículos");
        for (Vehiculo veh: vehiculos){
            if (veh != null) {
                System.out.println(veh);
            }
        }
        System.out.println();
    }


    public static void main(String [] args){
        GestorAlquileres app = new GestorAlquileres();
        app.menuPrincipal();
    }
}
