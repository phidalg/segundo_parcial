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

                    break;

                case 3: // Modificación

                    break;

                case 4: // Lista

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
