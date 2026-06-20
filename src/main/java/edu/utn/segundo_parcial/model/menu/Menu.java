package edu.utn.segundo_parcial.model.menu;

import java.util.Map;
import java.util.Scanner;

public abstract class Menu {

    public static void imprimirEncabezado() {
        System.out.println("\n\n\n\n\n");
        System.out.println("________________________________________________________________________________");
        System.out.println("|                           ~~ SISTEMA DE GESTIÓN ~~                           |");
        System.out.println("--------------------------------------------------------------------------------");
    }

    static int elegirOpcion(Map<Integer, String> opciones) {
        Scanner scanner = new Scanner(System.in);
        int opcion = -1;
        boolean esValida = false;

        while (!esValida) {
            // Mostrar el menú de opciones
            for (Map.Entry<Integer, String> entrada : opciones.entrySet()) {
                System.out.println(entrada.getKey() + ". " + entrada.getValue());
            }
            System.out.print("Seleccione una opción: ");

            try {
                String input = scanner.nextLine();
                opcion = Integer.parseInt(input);

                // Verificar si la opción existe en el Map
                if (opciones.containsKey(opcion)) {
                    esValida = true;
                } else {
                    System.out.println("Error: La opción " + opcion + " no es válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Debe ingresar un número válido. Intente nuevamente.");
            }
        }

        return opcion;
    }
}
