package catchat.chats;

import catchat.chats.view.ChatListCell;
import catchat.data.entities.chat.Chat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public class ChatsView extends VBox implements ChatsContract.View {
    private ChatsContract.Presenter presenter;
    private ListView<Chat> chatList;
    private Text pageNumber;

    public ChatsView() {
        chatList = new ListView<>();
        chatList.setOnMouseClicked((event) -> presenter.loadChat(chatList.getSelectionModel().getSelectedItem()));

        pageNumber = new Text();
        Button prevPage = new PrevButton();
        Button nextPage = new NextButton();
        Button refreshButton = new RefreshButton();
        HBox pageButtons = new HBox();
        pageButtons.getChildren().addAll(prevPage, nextPage);
        getChildren().addAll(chatList, pageNumber, pageButtons, refreshButton);
    }

    @Override
    public void setPresenter(ChatsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showGroups(List<Chat> groups) {
        ObservableList<Chat> obsChatList = FXCollections.observableArrayList(groups);
        chatList.setItems(obsChatList);
        chatList.setCellFactory(param -> new ChatListCell());
    }

    @Override
    public void setPageNumber(int number) {
        pageNumber.setText(Integer.toString(number));
    }

    private class RefreshButton extends Button {
        RefreshButton() {
            setText("Refresh");
            setOnMouseClicked(event -> presenter.refreshChats());
        }
    }

    private class NextButton extends Button {
        NextButton() {
            setText("Next Page");
            setOnMouseClicked(event -> presenter.nextPage());
        }
    }

    private class PrevButton extends Button {
        PrevButton() {
            setText("Prev Page");
            setOnMouseClicked(event -> presenter.prevPage());
        }
    }
}
