package edu.utn.segundo_parcial.model.menu;

import java.util.HashMap;
import java.util.Map;

import static edu.utn.segundo_parcial.model.menu.MenuCategorias.gestionarCategorias;

public abstract class MenuPrincipal extends Menu {

    private static final Map<Integer, String> MENU_PRINCIPAL;

    static {
        MENU_PRINCIPAL = new HashMap<>();
        MENU_PRINCIPAL.put(1, "Gestionar Categorías");
        MENU_PRINCIPAL.put(2, "Gestionar Productos");
        MENU_PRINCIPAL.put(3, "Reportes");
        MENU_PRINCIPAL.put(4, "Salir");
    }

    public static void menu() {

        boolean cerrar = false;
        int opcionElegida = -1;

        while (!cerrar) {
            imprimirEncabezado();
            System.out.println("\n                               > MENU PRINCIPAL <\n");
            opcionElegida = elegirOpcion((MENU_PRINCIPAL));

            switch (opcionElegida) {
                case 1: // Gestionar categorías
                    gestionarCategorias();
                    break;

                case 2: // Gestionar Productos

                    break;

                case 3: // Reportes

                    break;


                case 4:
                    cerrar = true;
                    break;

                default:

                    System.out.println("Error, opción invalida en switch.");
                    break;

            }

        }
    }
}
