package org.example.tarea_fonda_ep2.model;

public interface ConsumoResponsable {

    boolean tieneVentaRestringida();

    void restringirVenta();

    boolean superaLimite(int unidades);
}
