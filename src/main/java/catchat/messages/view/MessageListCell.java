package catchat.messages.view;

import catchat.data.entities.message.Message;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

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
        Label text = new Label();

        if (!empty) {
            messageId.setText("Message ID: " + item.getId());
            senderId.setText("Sender ID: " + item.getSenderId());
            text.setText("Text: " + item.getText());

            box.getChildren().addAll(messageId, senderId, text);
            setGraphic(box);
        } else {
            setGraphic(null);
        }
    }
}
