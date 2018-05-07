package catchat.error;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created by andrew on 5/6/18.
 */
public class ErrorBox extends VBox {
    public ErrorBox(String message) {
        Text title = new Text("An Error Occurred");
        Text messageText = new Text(message);
        Text prompt = new Text("Please report this error and re-open Cat Chat");
        Button close = new Button("Close");
        close.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });
        getChildren().addAll(title, messageText, prompt, close);
        setAlignment(Pos.CENTER);
    }
}
