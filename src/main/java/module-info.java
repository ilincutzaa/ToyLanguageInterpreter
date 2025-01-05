module interpreter.toylanguageinterpreter {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens interpreter.toylanguageinterpreter to javafx.fxml;
    exports interpreter.toylanguageinterpreter;
}