package org.example.tarea_fonda_ep2.model;

import java.util.ArrayList;
import java.util.List;

public class GestorFonda {

    private final List<Bebida> bebidas;

    public GestorFonda() {
        this.bebidas = new ArrayList<>();
    }

    public void registrar(Bebida bebida) {
        bebidas.add(bebida);
        System.out.println(bebida.getNombre() + " (" + bebida.getClass().getSimpleName() + ") registrada correctamente.");
    }

    public List<Bebida> buscarPorNombre(String nombre) {
        List<Bebida> encontradas = new ArrayList<>();
        for (Bebida bebida : bebidas) {
            if (bebida.getNombre().equalsIgnoreCase(nombre)) {
                encontradas.add(bebida);
            }
        }
        return encontradas;
    }

    public void vender(String nombre, int unidades) {
        List<Bebida> encontradas = buscarPorNombre(nombre);
        if (encontradas.isEmpty()) {
            System.out.println("Venta rechazada: no existe la bebida " + nombre + ".");
            return;
        }
        Bebida bebida = encontradas.get(0);
        if (bebida instanceof ConsumoResponsable controlada) {
            if (controlada.tieneVentaRestringida()) {
                System.out.println("Venta rechazada: " + nombre + " tiene la venta restringida.");
                return;
            }
            if (controlada.superaLimite(unidades)) {
                System.out.println("Venta rechazada: " + unidades + " unidades de " + nombre
                        + " superan el limite de " + BebidaAlcoholica.LIMITE_UNIDADES_POR_CLIENTE + " por cliente.");
                return;
            }
        }
        double total = bebida.calcularPrecio() * unidades;
        System.out.println("Venta autorizada: " + unidades + " x " + bebida.getNombre() + " | Total: $" + String.format("%.0f", total));
    }

    public List<Bebida> obtenerTodas() {
        return new ArrayList<>(bebidas);
    }
}
