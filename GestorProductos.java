package proyectodb;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class GestorProductos {
	
	private static final String URL = "jdbc:mariadb://localhost:3306/tienda";
    private static final String USER = "SELVIN";
    private static final String PASSWORD = "S3lv1n1985";
    private static final Scanner entrada = new Scanner(System.in);

    public void ejecutar() {
        try {
            while (true) {
                System.out.print("Elige una opción:");
                System.out.println();
                System.out.println("1. Escribir en base de datos");
                System.out.println("2. Leer de base de datos");
                System.out.println("3. Salir");
                int opcion = entrada.nextInt();
                entrada.nextLine();

                switch (opcion) {
                    case 1:
                        escribirEnBaseDeDatos();
                        break;
                    case 2:
                        leerDeBaseDeDatos();
                        break;
                    case 3:
                        System.out.println("Saliendo...");
                        return;
                    default:
                        System.out.println("Opción no válida. Inténtalo de nuevo.");
                }
            }
        } finally {
            entrada.close(); // Cerrar Scanner
        }
    }

    private void escribirEnBaseDeDatos() {
        List<Producto> productos = new ArrayList<>();
        boolean agregarOtro = true;

        while (agregarOtro) {
            Producto producto = crearProducto();
            productos.add(producto);

            System.out.println("¿Deseas agregar otro producto? (s/n)");
            String respuesta = entrada.nextLine();
            agregarOtro = respuesta.equalsIgnoreCase("s");
        }

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "INSERT INTO productos (nombre, estado, origen, precio) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                for (Producto producto : productos) {
                    statement.setString(1, producto.getNombre());
                    statement.setString(2, producto.getEstado());
                    statement.setString(3, producto.getOrigen());
                    statement.setBigDecimal(4, producto.getPrecio());
                    statement.executeUpdate();
                }
                System.out.println("Productos guardados en la base de datos.");
            }
        } catch (SQLException e) {
            System.out.println("Error al escribir en la base de datos: " + e.getMessage());
        }
    }

    private void leerDeBaseDeDatos() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM productos";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String nombre = resultSet.getString("nombre");
                    String estado = resultSet.getString("estado");
                    String origen = resultSet.getString("origen");
                    BigDecimal precio = resultSet.getBigDecimal("precio");

                    System.out.printf("ID: %d, Nombre: %s, Estado: %s, Origen: %s, Precio: %.2f%n",
                            id, nombre, estado, origen, precio);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al leer de la base de datos: " + e.getMessage());
        }
    }

    private Producto crearProducto() {
        String nombre = obtenerEntradaValida("Nombre: ", "[a-zA-Z]+");
        String estado = obtenerEntradaValida("Estado (produccion o terminado): ", "produccion|terminado");
        String origen = obtenerEntradaValida("Origen (local o importado): ", "local|importado");
        BigDecimal precio = new BigDecimal(obtenerEntradaValida("Precio (Q): ", "\\d+(\\.\\d+)?"));

        return new Producto(nombre, estado, origen, precio);
    }

    private String obtenerEntradaValida(String mensaje, String regex) {
        String entradaValida;
        while (true) {
            System.out.print(mensaje);
            String entrada = GestorProductos.entrada.nextLine().toLowerCase(); // Convertir a minúsculas
            if (entrada.matches(regex)) {
                entradaValida = entrada;
                break;
            } else {
                System.out.println("Entrada no válida. Inténtalo de nuevo.");
            }
        }
        return entradaValida;
    }
}


