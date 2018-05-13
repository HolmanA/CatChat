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
    public ChatListCell() {
        super();
        getStylesheets().add("/chats/css/chats_list_cell.css");
        getStyleClass().add("container");
    }

    @Override
    protected void updateItem(Chat item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            Label name = new Label(item.getName());
            name.getStyleClass().add("name");

            int snippetLength = 50;
            String snippet = item.getPreview();
            snippet = (snippet.length() > snippetLength) ? snippet.substring(0, snippetLength - 3) + "..." : snippet;

            Text preview = new Text(snippet);
            preview.getStyleClass().add("preview");
            preview.setWrappingWidth(200);

            VBox innerContainer = new VBox();
            innerContainer.getStyleClass().add("inner-container");
            innerContainer.getChildren().addAll(name, preview);


            setGraphic(innerContainer);
        } else {
            setGraphic(null);
        }
    }
}
