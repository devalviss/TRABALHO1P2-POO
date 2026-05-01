module SistemaEscolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens controller to javafx.fxml;
    opens main to javafx.graphics, javafx.fxml;
    
    exports main;
    exports controller;
    exports model;
}