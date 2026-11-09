module org.example.tarea_fonda_ep {
    requires javafx.fxml;
    requires com.google.gson;
    requires javafx.controls;
    requires javafx.graphics;
    requires java.desktop;

    opens org.example.tarea_fonda_ep2.model to com.google.gson;
    opens org.example.tarea_fonda_ep2 to javafx.graphics;
    opens org.example.tarea_fonda_ep2.controller to javafx.fxml;

    exports org.example.tarea_fonda_ep2;
}

