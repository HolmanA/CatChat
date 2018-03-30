package catchat;

import catchat.authentication.AuthenticationManager;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        AuthenticationManager authManager = new AuthenticationManager();
        authManager.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
