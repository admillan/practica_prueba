package org.example.tarea_fonda_ep2.model;

public abstract class Bebida {

    private String nombre;
    private int volumenML;
    private int stock;

    public Bebida(String nombre, int volumenML, int stock) {
        setNombre(nombre);
        setVolumenML(volumenML);
        setStock(stock);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo ni vacio.");
        }
        this.nombre = nombre.trim();
    }

    public int getVolumenML() {
        return volumenML;
    }

    public void setVolumenML(int volumenML) {
        if (volumenML < 100 || volumenML > 3000) {
            throw new IllegalArgumentException("El volumen debe estar entre 100 y 3000 ml.");
        }
        this.volumenML = volumenML;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        if (stock <= 0) {
            throw new IllegalArgumentException("El stock debe ser mayor que cero.");
        }
        this.stock = stock;
    }

    public abstract double calcularPrecio();

    public abstract String getTipo();

    @Override
    public String toString() {
        return "Nombre: " + nombre + " | Volumen: " + volumenML + " ml";
    }

    public abstract String obtenerDetalle();

    public static String pesos(int monto) {
        return String.format("$%,d", monto).replace(',', '.');
    }
}
