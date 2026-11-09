package org.example.tarea_fonda_ep2.controller;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.example.tarea_fonda_ep2.model.Bebida;
import org.example.tarea_fonda_ep2.model.BebidaAlcoholica;
import org.example.tarea_fonda_ep2.model.BebidaSinAlcohol;
import org.example.tarea_fonda_ep2.repository.BebidaRepository;
import org.example.tarea_fonda_ep2.repository.PersistenciaException;


public class mainController {
    //Constantes tipoChoice
    public final static String TIPO_ALCOHOLICA = "Bebida Alcoholica";
    public final static String TIPO_SIN_ALCOHOL = "Bebida Sin Alcohol";
    //Constantes AlertaLabel
    public final static String ALERTA_EXITO = "Registro exitoso";
    public final static String ALERTA_NO_VALIDO = "Datos no valido";
    public final static String ALERTA_ELIMINADO = "Registro eliminado";
    public final static String ALERTA_NO_SELECCIONADO = "Falta de seleccion";
    public final static String ALERTA_LECTURA = "Error al leer el archivo";
    public final static String ALERTA_ESCRITURA = "Error al escribir el archivo";
    public final static String ALERTA_DEFAULT = "Sin Alerta";




    //Espacio Alertas
    @FXML private Label alertaLabel;

    //Campos comunes
    @FXML private ChoiceBox <String> tipoChoice;
    @FXML private TextField nombreText;
    @FXML private TextField volumenText;
    @FXML private TextField stockText;


    //Campos bebida sin alcohol
    @FXML private TextField azucarText;

    //Bebida Alcoholica
    @FXML private TextField gradoText;
    @FXML private CheckBox certificadaCheck;
    @FXML private CheckBox ventaCheck;

    //Datos tabla
    @FXML private TableView<Bebida> tablaBebida;
    @FXML private TableColumn<Bebida, String> tipoColumn;
    @FXML private TableColumn<Bebida,String> nombreColumn;
    @FXML private TableColumn<Bebida,String> volumenColumn;
    @FXML private TableColumn<Bebida,String> stockColumn;
    @FXML private TableColumn<Bebida,String> precioColumn;

    private final BebidaRepository repository = new BebidaRepository();
    private ObservableList<Bebida> bebidas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        //Inicializa tipos de datos
        this.tipoChoice.getItems().add(TIPO_SIN_ALCOHOL);
        this.tipoChoice.getItems().add(TIPO_ALCOHOLICA);

        //Bloquea campos no seleccionados
        this.azucarText.setDisable(true);
        this.gradoText.setDisable(true);
        this.certificadaCheck.setDisable(true);
        this.ventaCheck.setDisable(true);

        this.configurarTableView();
        try{this.repository.cargar();
            this.actualizarTableview();
        }catch(Exception e)
        {this.setAlertaLabelMensaje(e.getMessage(), "Error al cargar inicial");}

        bebidas.setAll(this.repository.getList());
    }

    @FXML public void onSeleccionarTipo() {
        String tipoBebida = this.tipoChoice.getValue();
        switch(tipoBebida){
            case TIPO_SIN_ALCOHOL:
                this.azucarText.setDisable(false);
                this.gradoText.setDisable(true);
                this.certificadaCheck.setDisable(true);
                this.ventaCheck.setDisable(true);
                break;

            case TIPO_ALCOHOLICA:
                this.azucarText.setDisable(true);
                this.gradoText.setDisable(false);
                this.certificadaCheck.setDisable(false);
                this.ventaCheck.setDisable(false);
                break;

            default:
                this.azucarText.setDisable(true);
                this.gradoText.setDisable(true);
                this.certificadaCheck.setDisable(true);
                this.ventaCheck.setDisable(true);
                this.setAlertaLabelMensaje(ALERTA_NO_SELECCIONADO,"Campo seleccionar tipo");
        }
    }

    @FXML
    public void onGuardar(){
        Bebida bebida;
        try {
            String tipo = this.tipoChoice.getValue();
            String nombre = this.nombreText.getText();
            int volumenMl = Integer.parseInt(this.volumenText.getText());
            int stock = Integer.parseInt(this.stockText.getText());
            switch(tipo){
                case TIPO_SIN_ALCOHOL:
                    int azucarPorLitro = Integer.parseInt(this.azucarText.getText());
                    if(azucarPorLitro <= 0){
                        this.setAlertaLabelMensaje(ALERTA_NO_VALIDO,"Campo azucar por litro debe ser mayor que cero.");
                        return;
                    }
                    bebida = new BebidaSinAlcohol(nombre,volumenMl,stock, azucarPorLitro);
                    break;
                case TIPO_ALCOHOLICA:
                    double gradosDeAlcohol = Double.parseDouble(this.gradoText.getText());
                    if(gradosDeAlcohol < 0.5 || gradosDeAlcohol > 45){
                        this.setAlertaLabelMensaje(ALERTA_NO_VALIDO, "Los grados de alcohol deben estar entre 0,5 y 45.");
                }
                    boolean certificada = this.certificadaCheck.isSelected();
                    boolean ventaRestringida = this.ventaCheck.isSelected();
                    bebida = new BebidaAlcoholica(nombre,volumenMl,stock, gradosDeAlcohol, certificada);
                    if(ventaRestringida){
                        ((BebidaAlcoholica) bebida).restringirVenta();
                    }
                    break;
                default:
                    this.setAlertaLabelMensaje(ALERTA_NO_SELECCIONADO,"Campo tipo de Bebida");
                    return;
            }

            this.setAlertaLabelMensaje(ALERTA_EXITO,"Bebida - "+bebida.getNombre());

        }catch (IllegalArgumentException e){
            this.setAlertaLabelMensaje(ALERTA_NO_VALIDO, e.getMessage());

        }

    }
    private void configurarTableView(){
        this.tipoColumn.setCellValueFactory(celda -> new ReadOnlyStringWrapper(celda.getValue().getTipo()));
        this.nombreColumn.setCellValueFactory(celda -> new ReadOnlyStringWrapper(celda.getValue().getNombre()));
        this.volumenColumn.setCellValueFactory(celda -> new ReadOnlyStringWrapper(String.valueOf(celda.getValue().getVolumenML())));
        this.stockColumn.setCellValueFactory(celda -> new ReadOnlyStringWrapper(String.valueOf(celda.getValue().getStock())));
        this.precioColumn.setCellValueFactory(celda -> new ReadOnlyStringWrapper(Bebida.pesos((int) Math.round(celda.getValue().calcularPrecio()))));
        this.tablaBebida.setItems(this.bebidas);
    }

    private void actualizarTableview(){
        bebidas.setAll(repository.getList());
    }



    @FXML
    public void onLimpiar(){
        this.tipoChoice.setValue("");
        this.nombreText.setText("");
        this.volumenText.setText("");
        this.stockText.setText("");
        this.azucarText.setText("");
        this.gradoText.setText("");
        this.certificadaCheck.setSelected(false);
        this.ventaCheck.setSelected(false);
        this.setAlertaLabelMensaje(ALERTA_DEFAULT,"");
    }

    private void setAlertaLabelMensaje(String tipo,String mensaje){
        if (mensaje.trim().isBlank()){
            mensaje = tipo;
        }else{
            mensaje = tipo + ": "+mensaje;
        }
        switch (tipo){
            case ALERTA_EXITO:
            case ALERTA_ELIMINADO:
                this.alertaLabel.setVisible(true);
                this.alertaLabel.setTextFill(Color.GREEN);
                break;

            case ALERTA_NO_VALIDO:
            case ALERTA_NO_SELECCIONADO:
            case ALERTA_LECTURA:
            case ALERTA_ESCRITURA:
                this.alertaLabel.setVisible(true);
                this.alertaLabel.setTextFill(Color.RED);
                break;

            default:
                this.alertaLabel.setVisible(false);
                this.alertaLabel.setTextFill(Color.BLACK);


        }
        this.alertaLabel.setText(mensaje);


    }
    public void onEliminar() {
        Bebida seleccionada = this.tablaBebida.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            this.setAlertaLabelMensaje(ALERTA_NO_SELECCIONADO, "Seleccione una bebida");
            return;
        } try{
            if(!this.repository.eliminar(seleccionada)){
                this.setAlertaLabelMensaje(ALERTA_NO_SELECCIONADO, "No se encuentra lo solicitado en el registro");
                this.actualizarTableview();
                return;
            }

        }catch (PersistenciaException e) {
            this.setAlertaLabelMensaje(e.getMessage(), "No se pueden guardar los cambios");
            return;
        }
        this.actualizarTableview();
        this.setAlertaLabelMensaje(ALERTA_EXITO, "Bebida eliminada exitosamente");
    }

}
