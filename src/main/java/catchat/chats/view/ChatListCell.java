package catchat.chats.view;

import catchat.data.entities.chat.Chat;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created by andrew on 4/16/18.
 */
public class ChatListCell extends ListCell<Chat> {
    @Override
    protected void updateItem(Chat item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            VBox box = new VBox();
            Label name = new Label(item.getName());
            Text preview = new Text(item.getPreview());
            preview.setWrappingWidth(200);

            box.getChildren().addAll(name, preview);
            setGraphic(box);
        } else {
            setGraphic(null);
        }
    }
}
