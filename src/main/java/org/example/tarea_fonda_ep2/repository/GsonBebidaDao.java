package org.example.tarea_fonda_ep2.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.tarea_fonda_ep2.model.Bebida;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GsonBebidaDao <T extends Bebida> implements BebidaDao<T> {
    private final Path archivo;
    private final Type tipoLista;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public GsonBebidaDao(String archivo, Class<T> clase) {
        this.archivo = Path.of(archivo);
        this.tipoLista = TypeToken.getParameterized(List.class, clase).getType();
    }

    @Override
    public List<T> leer() {
        if (Files.notExists(this.archivo)) {
            return new ArrayList<>();
        }
        try (Reader lector = Files.newBufferedReader(archivo, StandardCharsets.UTF_8)) {
        } catch (IOException e){
        }
        return List.of();
    }

    @Override
    public void guardar(List<T> bebidas) {
    }
}