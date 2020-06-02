package grammar;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//Class Grammar, it opens the application.
public class Grammar extends Application {

    private static final Stage stage = new Stage();

    //Method start, it stats the application
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("view/GrammarApp.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    //Method main, launch app.
    public static void main(String[] args) {
        launch(args);
    }

    //Method closeStage, it closes the app.
    public static void closeStage() {
        stage.close();
    }
}
