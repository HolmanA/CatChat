package catchat.error;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Created by andrew on 5/6/18.
 */
public class ErrorBox extends VBox {
    public ErrorBox(String message) {
        Text title = new Text("An Error Occurred");
        title.setFont(Font.font(null, FontWeight.BOLD, 18));

        Separator separator1 = new Separator();
        Separator separator2 = new Separator();
        separator1.setMaxWidth(380);
        separator2.setMaxWidth(380);

        Text messageText = new Text(message);
        messageText.setFont(Font.font(null, FontWeight.NORMAL, 14));
        messageText.setWrappingWidth(380);

        Text prompt = new Text("Please report this error and re-open Cat Chat");
        prompt.setFont(Font.font(null, FontPosture.ITALIC, 12));

        Button close = new Button("Close");
        close.setOnMouseClicked(event -> {
            Platform.exit();
            System.exit(0);
        });

        getChildren().addAll(title, separator1, messageText, prompt, separator2, close);
        setAlignment(Pos.CENTER);
    }
}
