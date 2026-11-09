package org.example.tarea_fonda_ep2.repository;

import org.example.tarea_fonda_ep2.model.Bebida;
import org.example.tarea_fonda_ep2.model.BebidaAlcoholica;
import org.example.tarea_fonda_ep2.model.BebidaSinAlcohol;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class BebidaRepository {

    public static final String ARCHIVO_ALCOHOLICA = "alcoholicas.json";
    private static final String ARCHIVO_ALCOHOLICA_ANTERIOR = "interores.json";
    public static final String ARCHIVO_SIN_ALCOHOL = "sin alcohol.json";
    private final BebidaDao<BebidaAlcoholica> bebidaAlcoholicaDao;
    private final BebidaDao<BebidaSinAlcohol> bebidaSinAlcoholDao;
    private final List<BebidaAlcoholica> bebidaConAlcoholList = new ArrayList<>();
    private final List<BebidaSinAlcohol>  bebidaSinAlcoholList  = new ArrayList<>();

    public BebidaRepository(){
        this(new GsonBebidaDao<>(archivoInterioresExistente(), BebidaAlcoholica.class),new GsonBebidaDao<>(ARCHIVO_SIN_ALCOHOL, BebidaSinAlcohol.class));
    }

    private static String archivoInterioresExistente() {
        if (Files.exists(Paths.get(ARCHIVO_ALCOHOLICA))) {
            return ARCHIVO_ALCOHOLICA;
        }
        return Files.exists(Paths.get(ARCHIVO_ALCOHOLICA_ANTERIOR))
                ? ARCHIVO_ALCOHOLICA_ANTERIOR
                : ARCHIVO_ALCOHOLICA;
    }

    private BebidaRepository(GsonBebidaDao<BebidaAlcoholica> bebidaAlcoholicaDao, GsonBebidaDao<BebidaSinAlcohol> bebidaSinAlcoholDao){
        this.bebidaSinAlcoholDao = bebidaSinAlcoholDao;
        this.bebidaAlcoholicaDao = bebidaAlcoholicaDao;
    }

    public void cargar(){
        List<BebidaAlcoholica> bebidaConAlcohol = this.bebidaAlcoholicaDao.leer();
        List<BebidaSinAlcohol> bebidaSinAlcohol = this.bebidaSinAlcoholDao.leer();
        this.bebidaSinAlcoholList.clear();
        this.bebidaSinAlcoholList.addAll(bebidaSinAlcohol);

        // exterior
        this.bebidaConAlcoholList.clear();
        this.bebidaConAlcoholList.addAll(bebidaConAlcohol);
    }

    public List<Bebida> getList(){
        List<Bebida> bebidas = new ArrayList<>(this.bebidaConAlcoholList);
        bebidas.addAll(this.bebidaSinAlcoholList);

        return bebidas;
    }

    public void guardar(Bebida bebida) {
        if (bebida instanceof BebidaAlcoholica alcoholica) {
            this.bebidaConAlcoholList.add(alcoholica);
            try {
                bebidaAlcoholicaDao.guardar(this.bebidaConAlcoholList);
            } catch (PersistenciaException e) {
                this.bebidaConAlcoholList.remove(bebidaConAlcoholList.size() - 1);
                throw e;
            }

        } else if (bebida instanceof BebidaSinAlcohol exterior) {
            this.bebidaSinAlcoholList.add(exterior);
            try {
                bebidaSinAlcoholDao.guardar(this.bebidaSinAlcoholList);
            } catch (PersistenciaException e) {
                this.bebidaSinAlcoholList.remove(bebidaSinAlcoholList.size() - 1);
                throw e;
            }
        } else {
            throw new IllegalArgumentException("Tipo de bebida no soportado:");
        }
    }

    public boolean eliminar(Bebida bebida) {
        if (bebida instanceof BebidaAlcoholica alcoholica ) {
            return this.eliminarDe(bebidaConAlcoholList, bebidaAlcoholicaDao, bebida);

        }else if (bebida instanceof BebidaSinAlcohol sinAlcohol) {
            return this.eliminarDe(bebidaSinAlcoholList, bebidaSinAlcoholDao, bebida);
        }
        return false;
    }

    private <T extends  Bebida> boolean eliminarDe(List<T> lista, BebidaDao<T> bebidaDao, Bebida bebida) {
        int posicion = this.indiceDe(lista, bebida);
        if (posicion < 0) {
            return false;
        }
        T eliminada = lista.remove(posicion);
        try{
            bebidaDao.guardar(lista);
        }catch (PersistenciaException e){
            lista.add(posicion, eliminada);
            throw e;
        }
        return true;
    }

    private  static int indiceDe(List<? extends  Bebida> lista, Bebida bebida){
        for (int i = 0; i < lista.size(); i++) {
            if(lista.get(i)== bebida ){
                return i;
            }
        }
        return -1;
    }
}
