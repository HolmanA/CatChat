package catchat.ui.messages.view;

import catchat.data.entities.profile.MemberProfile;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

/**
 * Created by andrew on 5/7/18.
 */
public class MemberListCell extends ListCell<MemberProfile> {
    public MemberListCell() {
        super();
        getStylesheets().add("/messages/css/member_list_cell.css");
        getStyleClass().add("container");
    }

    @Override
    protected void updateItem(MemberProfile item, boolean empty) {
        if (item != null && !empty) {
            Label name = new Label(item.getName());
            name.getStyleClass().add("name");
            setGraphic(name);
        } else {
            setGraphic(null);
        }
    }
}
