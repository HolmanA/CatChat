package catchat.chats;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.profile.Profile;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class ChatsView extends VBox implements ChatsContract.View {
    private ChatsContract.Presenter presenter;
    private Text chatList;
    private Button refreshButton;

    public ChatsView() {
        chatList = new Text();
        refreshButton = new RefreshButton();
        getChildren().addAll(chatList, refreshButton);
    }

    @Override
    public void setPresenter(ChatsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showGroups(List<Chat> groups) {
        String output = "";
        for (Chat item : groups) {
            output += "ID: " + item.getId() + "\n";
            output += "Name: " + item.getName() + "\n";
            output += "Preview: " + item.getPreview() + "\n";
            output += "Members:\n";
            for (Profile p : item.getMembers()) {
                output += "\t" + p.getName() + "\n";
            }
        }
        chatList.setText(output);
    }

    private class RefreshButton extends Button {
        RefreshButton() {
            setText("Refresh");
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    presenter.loadGroups();
                }
            });
        }
    }
}
