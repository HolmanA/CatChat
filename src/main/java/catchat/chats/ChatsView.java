package catchat.chats;

import catchat.chats.view.ChatListCell;
import catchat.data.entities.chat.Chat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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
        super(5);
        chatList = new ListView<>();
        chatList.setOnMouseClicked(event -> presenter.loadChat(chatList.getSelectionModel().getSelectedItem()));

        pageNumber = new Text();
        Button prevPage = new Button("Prev Page");
        prevPage.setOnMouseClicked(event -> presenter.prevPage());
        Button nextPage = new Button("Next Page");
        nextPage.setOnMouseClicked(event -> presenter.nextPage());
        Button refreshButton = new Button("Refresh");
        refreshButton.setOnMouseClicked(event -> presenter.refreshChats());
        HBox pageButtons = new HBox(5);
        pageButtons.getChildren().addAll(prevPage, nextPage);
        pageButtons.setAlignment(Pos.CENTER);
        HBox.setHgrow(prevPage, Priority.SOMETIMES);
        HBox.setHgrow(nextPage, Priority.SOMETIMES);

        getChildren().addAll(chatList, pageNumber, pageButtons, refreshButton);
        VBox.setVgrow(chatList, Priority.ALWAYS);
        setAlignment(Pos.CENTER);
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
}
