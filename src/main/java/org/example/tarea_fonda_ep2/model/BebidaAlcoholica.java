package org.example.tarea_fonda_ep2.model;

public class BebidaAlcoholica extends Bebida implements ConsumoResponsable {

    public static final int LIMITE_UNIDADES_POR_CLIENTE = 3;

    private static final double PRECIO_BASE = 3500;
    private static final double RECARGO_SIN_CERTIFICAR = 0.20;

    private double gradosAlcohol;
    private boolean certificada;

    private boolean ventaRestringida;

    public BebidaAlcoholica(String nombre, int volumenML, int stock, double gradosAlcohol, boolean certificada) {
        super(nombre, volumenML, stock);
        setGradosAlcohol(gradosAlcohol);
        setCertificada(certificada);
    }

    public double getGradosAlcohol() {
        return gradosAlcohol;
    }

    public void setGradosAlcohol(double gradosAlcohol) {
        if (gradosAlcohol < 0.5 || gradosAlcohol > 45) {
            throw new IllegalArgumentException("Los grados de alcohol deben estar entre 0,5 y 45.");
        }
        this.gradosAlcohol = gradosAlcohol;
    }

    public boolean isCertificada() {
        return certificada;
    }

    public void setCertificada(boolean certificada) {
        this.certificada = certificada;
    }

    @Override
    public double calcularPrecio() {
        return certificada ? PRECIO_BASE : PRECIO_BASE * (1 + RECARGO_SIN_CERTIFICAR);
    }

    @Override
    public String obtenerDetalle() {
        return "Tipo: Bebida Alcoholica | Nombre: " + getNombre()
                + " | Volumen: " + getVolumenML() + " ml"
                + " | Stock: " + getStock()
                + " | Grados: " + gradosAlcohol
                + " | Certificada: " + (certificada ? "Si" : "No")
                + "\n  Venta restringida: " + (ventaRestringida ? "Si" : "No")
                + " | Precio: $" + String.format("%.0f", calcularPrecio());
    }

    @Override
    public boolean tieneVentaRestringida() {
        return ventaRestringida;
    }

    @Override
    public void restringirVenta() {
        ventaRestringida = true;
    }

    @Override
    public boolean superaLimite(int unidades) {
        return unidades > LIMITE_UNIDADES_POR_CLIENTE;
    }
    @Override
    public String getTipo() {
        return "Alcoholica";
    }
}
