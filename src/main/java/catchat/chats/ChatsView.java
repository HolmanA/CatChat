package catchat.chats;

import catchat.chats.view.ChatListCell;
import catchat.data.entities.chat.Chat;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Callback;

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
        chatList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                presenter.loadChat(chatList.getSelectionModel().getSelectedItem());
            }
        });

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
        chatList.setCellFactory(new Callback<ListView<Chat>, ListCell<Chat>>() {
            @Override
            public ListCell<Chat> call(ListView<Chat> param) {
                return new ChatListCell();
            }
        });
    }

    @Override
    public void setPageNumber(int number) {
        pageNumber.setText(Integer.toString(number));
    }

    private class RefreshButton extends Button {
        RefreshButton() {
            setText("Refresh");
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    presenter.refreshChats();
                }
            });
        }
    }

    private class NextButton extends Button {
        NextButton() {
            setText("Next Page");
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    presenter.nextPage();
                }
            });
        }
    }

    private class PrevButton extends Button {
        PrevButton() {
            setText("Prev Page");
            setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    presenter.prevPage();
                }
            });
        }
    }
}
