import java.io.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) throws Exception {
        String path = "src/saldo.dat";
        String user = "carlos";
        crearArchivo(path, user);
        limpiarConsola();
        Scanner scanner = new Scanner(System.in);
        app(scanner, path, user);
    }

    public static void limpiarConsola() {
        System.out.println("\033[H\033[2J");
        System.out.flush();
    }

    public static void menu() {
        System.out.println("¡Bienvenido! \n¿Qué deseas hacer?");
        System.out.println("1. Consultar Dinero \n2. Retirar Dinero \n3. Salir");
    }

    public static void pressEnterToContinue(Scanner scanner) {
        System.out.println("Presione Enter para continuar...");
        scanner.nextLine();
    }

    private static int leerOpcion(Scanner scanner) {
        int opcion = -1;
        try {
            opcion = Integer.parseInt(scanner.nextLine());
            if (opcion < 1 || opcion > 3) {
                System.out.println("Por favor, ingrese una opción válida entre 1 y 3.");
                pressEnterToContinue(scanner);
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor ingrese un número.");
            pressEnterToContinue(scanner);
        }
        return opcion;
    }

    public static void crearArchivo(String path, String user) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(path))) {
            dos.writeUTF(user);
            dos.writeInt(1000);
        } catch (Exception e) {
            System.out.println("No se pudo crear el archivo.");
        }
    }

    public static int obtenerSaldo(String path, String user) {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(path))) {
            String usuarioArchivo = dis.readUTF();
            int saldo = dis.readInt();
            if (usuarioArchivo.equals(user)) {
                return saldo;
            }
        } catch (Exception e) {
            System.out.println("Error al leer el saldo.");
        }
        return -1;
    }

    public static void actualizarSaldo(String path, String user, int nuevoSaldo) {
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(path))) {
            dos.writeUTF(user);
            dos.writeInt(nuevoSaldo);
        } catch (Exception e) {
            System.out.println("Error al actualizar el saldo.");
        }
    }

    public static void leerSaldo(String path, String user) {
        int saldo = obtenerSaldo(path, user);
        if (saldo != -1) {
            System.out.println("Su saldo actual es: $" + saldo);
        } else {
            System.out.println("No se pudo obtener el saldo.");
        }
        System.out.println();
    }

    public static void retirarDinero(String path, String user, Scanner scanner) {
        int saldo = obtenerSaldo(path, user);
        if (saldo == -1) {
            System.out.println("Error al obtener el saldo.");
            return;
        }

        System.out.print("Ingrese la cantidad a retirar: ");
        try {
            int cantidad = Integer.parseInt(scanner.nextLine());
            if (cantidad > saldo) {
                System.out.println("Saldo insuficiente.");
            } else if (cantidad <= 0) {
                System.out.println("Ingrese una cantidad válida.");
            } else {
                saldo -= cantidad;
                actualizarSaldo(path, user, saldo);
                System.out.println("Retiro exitoso. Su nuevo saldo es: $" + saldo);
            }
        } catch (NumberFormatException e) {
            System.out.println("Ingrese un número válido.");
        }
        System.out.println();
    }

    public static void app(Scanner scanner, String path, String user) {
        limpiarConsola();
        int opcion;
        do {
            menu();
            opcion = leerOpcion(scanner);
            limpiarConsola();

            switch (opcion) {
                case 1:
                    System.out.println("Consulta de dinero");
                    leerSaldo(path, user);
                    pressEnterToContinue(scanner);
                    break;
                case 2:
                    System.out.println("Retiro de dinero");
                    retirarDinero(path, user, scanner);
                    pressEnterToContinue(scanner);
                    break;
                case 3:
                    System.out.println("Feliz tarde!!!");
                    break;
                default:
                    System.out.println("Ingrese una opción válida.");
            }
        } while (opcion != 3);
        scanner.close();
    }
}
