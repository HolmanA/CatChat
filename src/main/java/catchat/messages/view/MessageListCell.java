package catchat.messages.view;

import catchat.data.entities.message.Message;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/17/18.
 */
public class MessageListCell extends ListCell<Message> {
    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);

        VBox box = new VBox();
        Label messageId = new Label();
        Label senderId = new Label();
        Label createdAt = new Label();
        Label text = new Label();

        if (!empty) {
            messageId.setText("Message ID: " + item.getId());
            senderId.setText("Sender ID: " + item.getSenderId());
            createdAt.setText("Created At: " + formatTime(item.getCreatedAt()));
            text.setText("Text: " + item.getText());

            box.getChildren().addAll(messageId, senderId, createdAt, text);
            setGraphic(box);
        } else {
            setGraphic(null);
        }
    }

    private String formatTime(long timeSeconds) {
        Date date = new Date(TimeUnit.SECONDS.toMillis(timeSeconds));
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a, M/d");
        return dateFormat.format(date);
    }
}
