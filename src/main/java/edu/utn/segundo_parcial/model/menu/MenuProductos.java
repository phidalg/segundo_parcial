package edu.utn.segundo_parcial.model.menu;

import edu.utn.segundo_parcial.model.Categoria;
import edu.utn.segundo_parcial.model.Producto;
import edu.utn.segundo_parcial.repository.CategoriaRepository;
import edu.utn.segundo_parcial.repository.ProductoRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public abstract class MenuProductos extends Menu {

    private static final Map<Integer, String> MENU_PRODUCTOS;

    static {
        MENU_PRODUCTOS = new HashMap<>();
        MENU_PRODUCTOS.put(1, "Alta de productos");
        MENU_PRODUCTOS.put(2, "Baja lógica de productos");
        MENU_PRODUCTOS.put(3, "Modificación de productos");
        MENU_PRODUCTOS.put(4, "Listado de productos");
        MENU_PRODUCTOS.put(5, "Volver al menú principal");
    }

    private static void altaProducto() {
        Scanner scanner = new Scanner(System.in);
        ProductoRepository productoRepo = new ProductoRepository();
        CategoriaRepository categoriaRepo = new CategoriaRepository();

        try {
            System.out.println("\n--- Alta de Nuevo Producto ---");

            System.out.print("Ingrese el nombre del producto: ");
            String nombre = scanner.nextLine().trim();
            if (nombre.isEmpty()) {
                System.out.println("Error: El nombre del producto no puede estar vacío.");
                return;
            }

            System.out.print("Ingrese el precio (ej: 123.45): ");
            String precioStr = scanner.nextLine().trim();
            Double precio = null;
            try {
                if (!precioStr.isEmpty()) precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                System.out.println("Precio inválido.");
                return;
            }

            System.out.print("Ingrese la descripción (opcional): ");
            String descripcion = scanner.nextLine().trim();

            System.out.print("Ingrese el stock (ej: 10): ");
            String stockStr = scanner.nextLine().trim();
            int stock = 0;
            try {
                if (!stockStr.isEmpty()) stock = Integer.parseInt(stockStr);
            } catch (NumberFormatException e) {
                System.out.println("Stock inválido.");
                return;
            }

            System.out.print("Ingrese la URL de la imagen (opcional): ");
            String imagen = scanner.nextLine().trim();

            System.out.print("¿Disponible? (s/n): ");
            String disponibleRaw = scanner.nextLine().trim().toLowerCase();
            Boolean disponible = null;
            if ("s".equals(disponibleRaw)) disponible = true;
            else if ("n".equals(disponibleRaw)) disponible = false;

            // Seleccionar categoría
            List<Categoria> categorias = categoriaRepo.listarActivos();
            if (categorias.isEmpty()) {
                System.out.println("No hay categorías activas. Cree una categoría primero.");
                return;
            }

            System.out.println("\nCategorias activas:");
            for (Categoria c : categorias) {
                System.out.println("ID: " + c.getId() + " - Nombre: " + c.getNombre() + (c.getDescripcion() != null ? " - " + c.getDescripcion() : ""));
            }

            System.out.print("Ingrese el ID de la categoría del producto: ");
            String catInput = scanner.nextLine().trim();
            Long catId;
            try {
                catId = Long.parseLong(catInput);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                return;
            }

            Optional<Categoria> optionalCategoria = categoriaRepo.buscarPorId(catId);
            if (!optionalCategoria.isPresent()) {
                System.out.println("No se encontró la categoría con ID: " + catId);
                return;
            }

            Producto nuevo = Producto.builder()
                    .nombre(nombre)
                    .precio(precio)
                    .descripcion(descripcion.isEmpty() ? null : descripcion)
                    .stock(stock)
                    .imagen(imagen.isEmpty() ? null : imagen)
                    .disponible(disponible)
                    .categoria(optionalCategoria.get())
                    .build();

            Producto guardado = productoRepo.guardar(nuevo);
            System.out.println("\nProducto creado exitosamente con ID: " + guardado.getId());

        } catch (Exception e) {
            System.out.println("\nError al crear el producto: " + e.getMessage());
        }
    }

    private static void bajaProducto() {
        Scanner scanner = new Scanner(System.in);
        ProductoRepository productoRepo = new ProductoRepository();
        try {
            System.out.println("\n--- Baja Lógica de Producto ---");

            List<Producto> productos = productoRepo.listarActivos();
            if (productos.isEmpty()) {
                System.out.println("No hay productos activos para eliminar.");
                return;
            }

            System.out.println("\nProductos activos:");
            for (Producto p : productos) {
                System.out.println("ID: " + p.getId() + " - Nombre: " + p.getNombre() + " - Precio: " + p.getPrecio() + (p.getCategoria() != null ? " - Categoria: " + p.getCategoria().getNombre() : ""));
            }

            System.out.print("Ingrese el ID del producto a eliminar: ");
            String input = scanner.nextLine().trim();

            Long id;
            try {
                id = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                return;
            }

            boolean eliminado = productoRepo.eliminarLogico(id);
            if (eliminado) {
                System.out.println("\nProducto eliminado lógicamente con éxito.");
            } else {
                System.out.println("\nNo se encontró el producto con ID: " + id);
            }

        } catch (Exception e) {
            System.out.println("\nError al eliminar el producto: " + e.getMessage());
        }
    }

    private static void modificarProducto() {
        Scanner scanner = new Scanner(System.in);
        ProductoRepository productoRepo = new ProductoRepository();
        CategoriaRepository categoriaRepo = new CategoriaRepository();

        try {
            System.out.println("\n--- Modificación de Producto ---");

            List<Producto> productos = productoRepo.listarActivos();
            if (productos.isEmpty()) {
                System.out.println("No hay productos activos para modificar.");
                return;
            }

            System.out.println("\nProductos activos:");
            for (Producto p : productos) {
                System.out.println("ID: " + p.getId() + " - Nombre: " + p.getNombre() + " - Precio: " + p.getPrecio() + (p.getCategoria() != null ? " - Categoria: " + p.getCategoria().getNombre() : ""));
            }

            System.out.print("Ingrese el ID del producto a modificar: ");
            String input = scanner.nextLine().trim();

            Long id;
            try {
                id = Long.parseLong(input);
            } catch (NumberFormatException e) {
                System.out.println("ID inválido.");
                return;
            }

            Optional<Producto> optionalProducto = productoRepo.buscarPorId(id);
            if (!optionalProducto.isPresent()) {
                System.out.println("\nNo se encontró el producto con ID: " + id);
                return;
            }

            Producto producto = optionalProducto.get();

            System.out.println("Nombre actual: " + producto.getNombre());
            System.out.print("Ingrese nuevo nombre (enter para mantener): ");
            String nuevoNombre = scanner.nextLine().trim();
            if (!nuevoNombre.isEmpty()) {
                producto.setNombre(nuevoNombre);
            }

            System.out.println("Precio actual: " + producto.getPrecio());
            System.out.print("Ingrese nuevo precio (enter para mantener): ");
            String nuevoPrecioRaw = scanner.nextLine().trim();
            if (!nuevoPrecioRaw.isEmpty()) {
                try {
                    producto.setPrecio(Double.parseDouble(nuevoPrecioRaw));
                } catch (NumberFormatException e) {
                    System.out.println("Precio inválido. Se mantiene el valor anterior.");
                }
            }

            System.out.println("Descripción actual: " + (producto.getDescripcion() == null ? "" : producto.getDescripcion()));
            System.out.print("Ingrese nueva descripción (enter para mantener, - para borrar): ");
            String nuevaDescRaw = scanner.nextLine();
            String nuevaDesc = nuevaDescRaw == null ? "" : nuevaDescRaw.trim();

            if ("-".equals(nuevaDescRaw)) {
                producto.setDescripcion(null);
            } else if (!nuevaDesc.isEmpty()) {
                producto.setDescripcion(nuevaDesc);
            }

            System.out.println("Stock actual: " + producto.getStock());
            System.out.print("Ingrese nuevo stock (enter para mantener): ");
            String nuevoStockRaw = scanner.nextLine().trim();
            if (!nuevoStockRaw.isEmpty()) {
                try {
                    producto.setStock(Integer.parseInt(nuevoStockRaw));
                } catch (NumberFormatException e) {
                    System.out.println("Stock inválido. Se mantiene el valor anterior.");
                }
            }

            System.out.println("Imagen actual: " + (producto.getImagen() == null ? "" : producto.getImagen()));
            System.out.print("Ingrese nueva URL de imagen (enter para mantener, - para borrar): ");
            String nuevaImagenRaw = scanner.nextLine();
            String nuevaImagen = nuevaImagenRaw == null ? "" : nuevaImagenRaw.trim();
            if ("-".equals(nuevaImagenRaw)) {
                producto.setImagen(null);
            } else if (!nuevaImagen.isEmpty()) {
                producto.setImagen(nuevaImagen);
            }

            System.out.println("Disponible actual: " + producto.getDisponible());
            System.out.print("¿Disponible? (s/n enter para mantener): ");
            String disponibleRaw = scanner.nextLine().trim().toLowerCase();
            if ("s".equals(disponibleRaw)) producto.setDisponible(true);
            else if ("n".equals(disponibleRaw)) producto.setDisponible(false);

            // Cambiar categoría
            List<Categoria> categorias = categoriaRepo.listarActivos();
            if (!categorias.isEmpty()) {
                System.out.println("\nCategorias activas:");
                for (Categoria c : categorias) {
                    System.out.println("ID: " + c.getId() + " - Nombre: " + c.getNombre());
                }
                System.out.print("Ingrese el ID de la nueva categoría (enter para mantener): ");
                String catInput = scanner.nextLine().trim();
                if (!catInput.isEmpty()) {
                    try {
                        Long catId = Long.parseLong(catInput);
                        Optional<Categoria> optCat = categoriaRepo.buscarPorId(catId);
                        if (optCat.isPresent()) {
                            producto.setCategoria(optCat.get());
                        } else {
                            System.out.println("No se encontró la categoría con ID: " + catId + ". Se mantiene la categoría anterior.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("ID inválido. Se mantiene la categoría anterior.");
                    }
                }
            }

            Producto guardado = productoRepo.guardar(producto);
            System.out.println("\nProducto modificado exitosamente. ID: " + guardado.getId());

        } catch (Exception e) {
            System.out.println("\nError al modificar el producto: " + e.getMessage());
        }
    }

    private static void listarProductos() {
        ProductoRepository productoRepo = new ProductoRepository();
        try {
            System.out.println("\n--- Listado de Productos Activos ---");
            List<Producto> productos = productoRepo.listarActivos();
            if (productos.isEmpty()) {
                System.out.println("No hay productos activos.");
                return;
            }
            System.out.println();
            for (Producto p : productos) {
                System.out.println("ID: " + p.getId() + " | Nombre: " + p.getNombre() + " | Precio: " + p.getPrecio() + (p.getCategoria() != null ? " | Categoria: " + p.getCategoria().getNombre() : "") + (p.getDescripcion() != null ? " | " + p.getDescripcion() : ""));
            }
        } catch (Exception e) {
            System.out.println("\nError al listar los productos: " + e.getMessage());
        }
    }

    static void gestionarProductos() {
        boolean volver = false;

        while (!volver) {
            imprimirEncabezado();

            System.out.println("\n                            > GESTION DE PRODUCTOS <\n");
            int opcion = elegirOpcion(MENU_PRODUCTOS);

            switch (opcion) {
                case 1: // Alta
                    altaProducto();
                    break;

                case 2: // Baja
                    bajaProducto();
                    break;

                case 3: // Modificación
                    modificarProducto();
                    break;

                case 4: // Lista
                    listarProductos();
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
