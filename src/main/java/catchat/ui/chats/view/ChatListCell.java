package catchat.ui.chats.view;

import catchat.data.entities.chat.Chat;
import catchat.ui.chats.ChatsContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 * Created by andrew on 4/16/18.
 */
public class ChatListCell extends ListCell<Chat> {
    private static final Logger log = LoggerFactory.getLogger(ChatListCell.class);
    private ChatsContract.Presenter presenter;
    private VBox innerContainer;

    public ChatListCell(ChatsContract.Presenter presenter) {
        super();
        this.presenter = presenter;
        getStylesheets().add("/chats/css/chats_list_cell.css");
        getStyleClass().add("container");
        log.debug("Style Class set to: {}", getStyleClass());
    }

    @Override
    protected void updateItem(Chat item, boolean empty) {
        super.updateItem(item, empty);

        if (!empty) {
            ToggleButton toggle = new ToggleButton();
            toggle.getStyleClass().add("toggle");
            toggle.setVisible(false);
            toggle.selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if (!oldValue && newValue) {
                    updateSelected(true);
                } else {
                    updateSelected(false);
                }
            }));

            selectedProperty().addListener(((observable, oldValue, newValue) -> {
                if (!newValue) {
                    toggle.setSelected(false);
                }
            }));

            setOnMouseClicked(event -> {
                log.debug("Cell clicked: {}: {}", getIndex(), item.getName());
                toggle.setSelected(!toggle.isSelected());
                presenter.selectChat(item);
            });

            Label name = new Label(item.getName());
            name.getStyleClass().add("name");

            int snippetLength = 50;
            String snippet = item.getPreview();
            snippet = (snippet.length() > snippetLength) ? snippet.substring(0, snippetLength - 3) + "..." : snippet;

            Text preview = new Text(snippet);
            preview.getStyleClass().add("preview");
            preview.setWrappingWidth(200);

            innerContainer = new VBox();
            innerContainer.getStyleClass().add("inner-container");
            innerContainer.getChildren().addAll(name, preview);

            getChildren().addAll(toggle, innerContainer);
            setGraphic(innerContainer);
            setDisable(false);
        } else {
            setOnMouseClicked(event -> {});
            setGraphic(null);
            setDisable(true);
        }
    }
}
