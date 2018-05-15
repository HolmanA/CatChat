package catchat;

import catchat.ui.ApplicationStage;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ApplicationStage stage = new ApplicationStage();
        stage.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
