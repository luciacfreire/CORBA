import Biblioteca.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

import java.util.Scanner;

import org.omg.CORBA.*;

public class ClienteBiblioteca {
    public static void main(String args[]) {
        try {
            // Inicializar el ORB (Object Request Broker)
            ORB orb = ORB.init(args, null);

            // Obtener referencia al servicio de nombres
            org.omg.CORBA.Object objRef = orb.resolve_initial_references("NameService");
            NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

            // Buscar la referencia del objeto (servidor) en el servicio de nombres
            String name = "GestionBiblioteca";
            GestionBiblioteca gestionBiblioteca = GestionBibliotecaHelper.narrow(ncRef.resolve_str(name));

            // Usar la interfaz para llamar a las operaciones del servidor
            // Ejemplo: Buscar un libro
            Scanner scanner = new Scanner(System.in);
            boolean continuar = true;

            while (continuar) {
                // Mostrar el menú de opciones
                System.out.println("\n--- Menú de Opciones ---");
                System.out.println("1. Buscar un libro");
                System.out.println("2. Prestar un libro");
                System.out.println("3. Devolver un libro");
                System.out.println("4. Mostrar todos los libros");
                System.out.println("5.Mostrar libros en una categoria");
                System.out.println("6. Salir");
                System.out.print("Seleccione una opción: ");

                int opcion = scanner.nextInt();
                scanner.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1:
                        // Buscar un libro
                        System.out.print("Ingrese el titulo del libro que desea buscar: ");
                        String tituloLibro = scanner.nextLine();
                        Libro libro = gestionBiblioteca.buscarLibro(tituloLibro);
                        System.out.println("Libro encontrado: " + libro.titulo + ", " + libro.autor +
                                ", Categoria: " + libro.categoria + ", ISBN: " + libro.ISBN + ", Disponible:"
                                + libro.estaDisponible);
                        break;

                    case 2:
                        // Prestar un libro
                        System.out.print("Ingrese el ISBN del libro que desea prestar: ");
                        String isbnPrestar = scanner.nextLine();
                        boolean resultadoPrestamo = gestionBiblioteca.prestarLibro(isbnPrestar);
                        if (resultadoPrestamo) {
                            System.out.println("Libro prestado con exito.");
                        } else {
                            System.out.println("El libro no está disponible para prestamo.");
                        }
                        break;

                    case 3:
                        // Devolver un libro
                        System.out.print("Ingrese el ISBN del libro que desea devolver: ");
                        String isbnDevolver = scanner.nextLine();
                        boolean resultadoDevolucion = gestionBiblioteca.devolverLibro(isbnDevolver);
                        if (resultadoDevolucion) {
                            System.out.println("Libro devuelto con exito.");
                        } else {
                            System.out.println("El libro no esta disponible para devolver.");
                        }
                        break;
                    case 4:
                        // Mostrar todos los libros
                        System.out.println("Lista de todos los libros:");
                        Libro[] libros = gestionBiblioteca.mostrarTodosLosLibros();
                        for (Libro l : libros) {
                            System.out.println("Titulo: " + l.titulo + ", Autor: " + l.autor +
                                    ", ISBN: " + l.ISBN + ", Categoria: " + l.categoria +
                                    ", Disponible: " + (l.estaDisponible ? "Si" : "No"));
                        }
                        break;
                        case 5:
                        // Mostrar libros de una categoría
                        System.out.print("Ingrese la categoría de los libros que desea ver: ");
                        String categoria = scanner.nextLine();
                        Libro[] librosCategoria = gestionBiblioteca.mostrarLibrosCategoria(categoria);
                        if (librosCategoria.length == 0) {
                            System.out.println("No se encontraron libros en la categoría: " + categoria);
                        } else {
                            System.out.println("Lista de libros en la categoría '" + categoria + "':");
                            for (Libro l : librosCategoria) {
                                System.out.println("Título: " + l.titulo + ", Autor: " + l.autor +
                                                   ", ISBN: " + l.ISBN + ", Disponible: " + (l.estaDisponible ? "Sí" : "No"));
                            }
                        }
                        break;
                    case 6:
                        // Salir
                        System.out.println("Saliendo del sistema...");
                        continuar = false;
                        break;

                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                        break;
                }
            }
            scanner.close();

        } catch (Exception e) {
            System.out.println("Error: " + e);
            e.printStackTrace(System.out);
        }
    }
}
