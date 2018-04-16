package catchat.chats.view;

import catchat.data.entities.chat.Chat;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;

/**
 * Created by andrew on 4/16/18.
 */
public class ChatListCell extends ListCell<Chat> {
    @Override
    protected void updateItem(Chat item, boolean empty) {
        super.updateItem(item, empty);

        VBox box = new VBox();
        Label id = new Label();
        Label name = new Label();
        Label preview = new Label();

        if (!empty) {
            id.setText(item.getId());
            name.setText(item.getName());
            preview.setText(item.getPreview());

            box.getChildren().addAll(id, name, preview);
            setGraphic(box);
        }
    }
}
