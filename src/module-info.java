module SistemaEscolar {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Exporta os pacotes para que o JavaFX consiga ver as classes
    exports main;
    exports controller;
    exports model;

    // Abre os pacotes para reflexão (necessário para o FXML e o Launch)
    opens main to javafx.graphics, javafx.fxml;
    opens controller to javafx.fxml;
}