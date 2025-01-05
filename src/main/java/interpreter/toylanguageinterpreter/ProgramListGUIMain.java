package interpreter.toylanguageinterpreter;

import interpreter.toylanguageinterpreter.Utils.MyException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;

public class ProgramListGUIMain extends Application {

    @Override
    public void start(Stage primaryStage){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(ProgramListGUIMain.class.getResource("ProgramListGUI.fxml"));
            String logFilePath = "D:\\Java projects\\ToyLanguageInterpreter\\src\\main\\java\\interpreter\\toylanguageinterpreter\\log.txt";
            try {
                FileWriter fileWriter = new FileWriter(logFilePath, false);
                fileWriter.close();
            } catch (IOException e) {
                throw new MyException(e.getMessage());
            }
            Scene scene = new Scene(fxmlLoader.load());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Program List");
            primaryStage.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) { launch(args); }
}
