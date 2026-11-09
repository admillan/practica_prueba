package org.example.tarea_fonda_ep2.repository;

import org.example.tarea_fonda_ep2.model.Bebida;
import java.util.List;

public interface BebidaDao<T extends Bebida> {
    List<T> leer();
    void guardar(List<T> bebidas);
}

