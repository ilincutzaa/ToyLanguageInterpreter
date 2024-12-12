module interpreter.toylanguageinterpreter {
    requires javafx.controls;
    requires javafx.fxml;


    opens interpreter.toylanguageinterpreter to javafx.fxml;
    exports interpreter.toylanguageinterpreter;
}