package catchat.error;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created by andrew on 5/6/18.
 */
public class ErrorBox extends VBox {
    public ErrorBox(String message) {
        super();
        getStylesheets().add("/error/css/error_box.css");
        getStyleClass().add("container");

        Text title = new Text("An Error Occurred");
        getStyleClass().add("title");

        Separator separator1 = new Separator();
        Separator separator2 = new Separator();

        Text messageText = new Text(message);
        getStyleClass().add("message-text");
        messageText.setWrappingWidth(380);

        Text prompt = new Text("Please report this error and re-open Cat Chat");
        getStyleClass().add("prompt");

        Button close = new Button("Close");
        getStyleClass().add("close");
        close.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });

        getChildren().addAll(title, separator1, messageText, prompt, separator2, close);
    }
}
