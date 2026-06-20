package edu.utn.segundo_parcial.model.menu;

import edu.utn.segundo_parcial.model.Categoria;
import edu.utn.segundo_parcial.repository.CategoriaRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public abstract class MenuCategorias extends Menu{

    private static final Map<Integer, String> MENU_CATEGORIAS;

    static {
        MENU_CATEGORIAS = new HashMap<>();
        MENU_CATEGORIAS.put(1, "Alta de categorías");
        MENU_CATEGORIAS.put(2, "Baja lógica de categorías");
        MENU_CATEGORIAS.put(3, "Modificación de categorías");
        MENU_CATEGORIAS.put(4, "Listado de categorías");
        MENU_CATEGORIAS.put(5, "Volver al menú principal");
    }

    private static void altaCategoria() {
        Scanner scanner = new Scanner(System.in);
        CategoriaRepository categoriaRepo = new CategoriaRepository();

        try {
            System.out.println("\n--- Alta de Nueva Categoría ---");

            System.out.print("Ingrese el nombre de la categoría: ");
            String nombre = scanner.nextLine().trim();

            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre de la categoría no puede estar vacío.");
                return;
            }

            System.out.print("Ingrese la descripción (opcional): ");
            String descripcion = scanner.nextLine().trim();

            Categoria nueva = Categoria.builder()
                    .nombre(nombre)
                    .descripcion(descripcion.isEmpty() ? null : descripcion)
                    .build();

            Categoria guardada = categoriaRepo.guardar(nueva);
            System.out.println("\nCategoría creada exitosamente con ID: " + guardada.getId());

        } catch (Exception e) {
            System.out.println("\nError al crear la categoría: " + e.getMessage());
        }
    }

    private static void bajaCategoria() {
        Scanner scanner = new Scanner(System.in);
        CategoriaRepository categoriaRepo = new CategoriaRepository();
        try {
            System.out.println("\n--- Baja Lógica de Categoría ---");

            java.util.List<Categoria> categorias = categoriaRepo.listarActivos();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías activas para eliminar.");
                return;
            }

            System.out.println("\nCategorias activas:");
            for (Categoria c : categorias) {
                System.out.println("ID: " + c.getId() + " - Nombre: " + c.getNombre() + (c.getDescripcion() != null ? " - " + c.getDescripcion() : ""));
            }

            System.out.print("Ingrese el ID de la categoría a eliminar: ");
            String input = scanner.nextLine().trim();

            Long id;
            try {
                id = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                return;
            }

            boolean eliminado = categoriaRepo.eliminarLogico(id);
            if (eliminado) {
                System.out.println("\nCategoría eliminada lógicamente con éxito.");
            } else {
                System.out.println("\nNo se encontró la categoría con ID: " + id);
            }

        } catch (Exception e) {
            System.out.println("\nError al eliminar la categoría: " + e.getMessage());
        }
    }

    private static void modificarCategoria() {
        Scanner scanner = new Scanner(System.in);
        CategoriaRepository categoriaRepo = new CategoriaRepository();

        try {
            System.out.println("\n--- Modificación de Categoría ---");

            java.util.List<Categoria> categorias = categoriaRepo.listarActivos();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías activas para modificar.");
                return;
            }

            System.out.println("\nCategorias activas:");
            for (Categoria c : categorias) {
                System.out.println("ID: " + c.getId() + " - Nombre: " + c.getNombre() + (c.getDescripcion() != null ? " - " + c.getDescripcion() : ""));
            }

            System.out.print("Ingrese el ID de la categoría a modificar: ");
            String input = scanner.nextLine().trim();

            Long id;
            try {
                id = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                return;
            }

            java.util.Optional<Categoria> optionalCategoria = categoriaRepo.buscarPorId(id);
            if (!optionalCategoria.isPresent()) {
                System.out.println("\nNo se encontró la categoría con ID: " + id);
                return;
            }

            Categoria categoria = optionalCategoria.get();

            System.out.println("Nombre actual: " + categoria.getNombre());
            System.out.print("Ingrese nuevo nombre (enter para mantener): ");
            String nuevoNombre = scanner.nextLine().trim();
            if (!nuevoNombre.isEmpty()) {
                categoria.setNombre(nuevoNombre);
            }

            System.out.println("Descripción actual: " + (categoria.getDescripcion() == null ? "" : categoria.getDescripcion()));
            System.out.print("Ingrese nueva descripción (enter para mantener, - para borrar): ");
            String nuevaDescRaw = scanner.nextLine();
            String nuevaDesc = nuevaDescRaw == null ? "" : nuevaDescRaw.trim();

            if ("-".equals(nuevaDescRaw)) {
                categoria.setDescripcion(null);
            } else if (!nuevaDesc.isEmpty()) {
                categoria.setDescripcion(nuevaDesc);
            }

            Categoria guardada = categoriaRepo.guardar(categoria);
            System.out.println("\nCategoría modificada exitosamente. ID: " + guardada.getId());

        } catch (Exception e) {
            System.out.println("\nError al modificar la categoría: " + e.getMessage());
        }
    }

    private static void listarCategorias() {
        CategoriaRepository categoriaRepo = new CategoriaRepository();
        try {
            System.out.println("\n--- Listado de Categorías Activas ---");
            java.util.List<Categoria> categorias = categoriaRepo.listarActivos();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías activas.");
                return;
            }
            System.out.println();
            for (Categoria c : categorias) {
                System.out.println("ID: " + c.getId() + " | Nombre: " + c.getNombre() + (c.getDescripcion() != null ? " | " + c.getDescripcion() : ""));
            }
        } catch (Exception e) {
            System.out.println("\nError al listar las categorías: " + e.getMessage());
        }
    }

    static void gestionarCategorias() {
        boolean volver = false;

        while (!volver) {
            imprimirEncabezado();
            System.out.println("\n                           > GESTION DE CATEGORÍAS <\n");
            int opcion = elegirOpcion(MENU_CATEGORIAS);

            switch (opcion) {
                case 1: // Alta
                    altaCategoria();
                    break;

                case 2: // Baja
                    bajaCategoria();
                    break;

                case 3: // Modificación
                    modificarCategoria();
                    break;

                case 4: // Lista
                    listarCategorias();
                    break;

                case 5: // Volver
                    volver = true;
                    break;

                default:
                    System.out.println("Error, opción invalida en switch.");
                    break;
            }
        }
    }
}
