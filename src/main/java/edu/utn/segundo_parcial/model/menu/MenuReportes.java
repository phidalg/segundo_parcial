package edu.utn.segundo_parcial.model.menu;

import edu.utn.segundo_parcial.model.Categoria;
import edu.utn.segundo_parcial.model.Producto;
import edu.utn.segundo_parcial.repository.CategoriaRepository;
import edu.utn.segundo_parcial.repository.ProductoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class MenuReportes extends Menu {

    private static final Map<Integer, String> MENU_REPORTES;

    static {
        MENU_REPORTES = new HashMap<>();
        MENU_REPORTES.put(1, "Generar reporte de productos por categoría");
        MENU_REPORTES.put(2, "Volver al menú principal");
    }

    static void reportes() {
        boolean volver = false;

        while (!volver) {
            imprimirEncabezado();
            System.out.println("\n                              > GENERAR REPORTES <\n");
            int opcion = elegirOpcion(MENU_REPORTES);

            switch (opcion) {
                case 1:
                    generarReporteProductosPorCategoria();
                    break;

                case 2:
                    volver = true;
                    break;

                default:
                    System.out.println("Error, opción invalida en switch.");
                    break;
            }
        }
    }

    private static void generarReporteProductosPorCategoria() {
        CategoriaRepository categoriaRepository = new CategoriaRepository();
        ProductoRepository productoRepository = new ProductoRepository();

        List<Categoria> categoriasActivas = categoriaRepository.listarActivos();

        if (categoriasActivas.isEmpty()) {
            System.out.println("\nNo hay categorías disponibles.");
            return;
        }

        Map<Integer, Categoria> categoriasMap = new HashMap<>();
        Map<Integer, String> opcionesMenu = new HashMap<>();
        
        int index = 1;
        for (Categoria categoria : categoriasActivas) {
            categoriasMap.put(index, categoria);
            opcionesMenu.put(index, categoria.getNombre());
            index++;
        }
        opcionesMenu.put(index, "Volver");

        System.out.println("\n                        > SELECCIONAR CATEGORÍA <\n");
        int opcion = elegirOpcion(opcionesMenu);

        if (opcion == index) {
            return;
        }

        Categoria categoriaSeleccionada = categoriasMap.get(opcion);
        List<Producto> productos = productoRepository.buscarPorCategoria(categoriaSeleccionada.getId());

        System.out.println("\n                    > PRODUCTOS DE: " + categoriaSeleccionada.getNombre().toUpperCase() + " <\n");

        if (productos.isEmpty()) {
            System.out.println("No hay productos disponibles en esta categoría.");
        } else {
            System.out.println(String.format("%-5s | %-30s | %-10s | %-10s", "ID", "NOMBRE", "PRECIO", "STOCK"));
            System.out.println("------|--------------------------------|------------|----------");
            for (Producto producto : productos) {
                System.out.println(String.format("%-5d | %-30s | $%-9.2f | %-10d",
                        producto.getId(),
                        producto.getNombre(),
                        producto.getPrecio(),
                        producto.getStock()));
            }
        }
    }
}
