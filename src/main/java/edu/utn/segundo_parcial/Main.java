package edu.utn.segundo_parcial;

import java.util.HashMap;
import java.util.Map;
import static edu.utn.segundo_parcial.model.menu.MenuPrincipal.menu;

public class Main {

    private static final Map<Integer, String> MENU_PRODUCTOS;

    static {
        MENU_PRODUCTOS = new HashMap<>();
        MENU_PRODUCTOS.put(1, "Alta de productos");
        MENU_PRODUCTOS.put(2, "Baja lógica de productos");
        MENU_PRODUCTOS.put(3, "Modificación de productos");
        MENU_PRODUCTOS.put(4, "Listado de productos");
        MENU_PRODUCTOS.put(5, "Volver al menú principal");
    }


    public static void main(String[] args) {

        menu();








    }
}