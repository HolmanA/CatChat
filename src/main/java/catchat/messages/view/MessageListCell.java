package catchat.messages.view;

import catchat.data.entities.message.Message;
import catchat.messages.MessagesContract;

import javafx.geometry.Orientation;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/17/18.
 */
public class MessageListCell extends ListCell<Message> {
    private MessagesContract.Presenter presenter;

    public MessageListCell(MessagesContract.Presenter presenter) {
        super();
        this.presenter = presenter;
        getStylesheets().add("/messages/css/message_list_cell.css");
        getStyleClass().add("container");
    }

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            Node messageComponent = initializeMessageComponent(item);
            Node likeComponent = initializeLikeComponent(item);

            HBox innerContainer = new HBox();
            innerContainer.getStyleClass().add("inner-container");
            innerContainer.getChildren().addAll(messageComponent, new Separator(Orientation.VERTICAL), likeComponent);
            HBox.setHgrow(messageComponent, Priority.ALWAYS);

            try {
                Node imageComponent = initializeImageComponent(item);
                innerContainer.getChildren().add(0, imageComponent);
                innerContainer.getChildren().add(1, new Separator((Orientation.VERTICAL)));
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }

            VBox borderContainer = new VBox();
            borderContainer.getChildren().addAll(new Separator(Orientation.HORIZONTAL), innerContainer);

            setGraphic(borderContainer);
        } else {
            setGraphic(null);
        }
    }

    private Node initializeImageComponent(Message item) throws IllegalArgumentException {
        ImageView imageView = new ImageView(new Image(item.getSenderAvatar(), true));
        imageView.getStyleClass().add("sender-avatar");
        imageView.setFitWidth(70);
        imageView.setFitHeight(70);
        imageView.setCache(true);
        imageView.setCacheHint(CacheHint.SPEED);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);

        VBox container = new VBox();
        container.getStyleClass().add("sender-avatar-container");
        container.getChildren().add(imageView);
        return container;
    }

    private Node initializeMessageComponent(Message item) {
        Label senderName = new Label(item.getSenderName());
        senderName.getStyleClass().add("sender-name");

        Label createdAt = new Label(formatTime(item.getCreatedAt()));
        createdAt.getStyleClass().add("created-at");

        HBox senderContainer = new HBox();
        senderContainer.getStyleClass().add("sender-container");
        senderContainer.getChildren().addAll(senderName, createdAt);

        Text messageText = new Text(item.getText());
        messageText.getStyleClass().add("message-text");
        messageText.setWrappingWidth(500);

        VBox messageContainer = new VBox();
        messageContainer.getStyleClass().add("message-container");
        messageContainer.getChildren().addAll(senderContainer, messageText);
        return messageContainer;
    }

    private Node initializeLikeComponent(Message item) {
        Label likeCount = new Label(Integer.toString(item.getLikeCount()));
        likeCount.getStyleClass().add("like-count");

        Button like = new Button("\uD83D\uDC4D");
        like.getStyleClass().add("like");
        like.setOnMouseClicked(event -> presenter.likeMessage(item));

        Button unlike = new Button("\uD83D\uDC4E");
        like.getStyleClass().add("unlike");
        unlike.setOnMouseClicked(event -> presenter.unlikeMessage(item));

        HBox likeButtonContainer = new HBox();
        likeButtonContainer.getStyleClass().add("like-button-container");
        likeButtonContainer.getChildren().addAll(like, unlike);

        VBox likeContainer = new VBox();
        likeContainer.getStyleClass().add("like-container");
        likeContainer.getChildren().addAll(likeCount, likeButtonContainer);
        return likeContainer;
    }

    private String formatTime(long timeSeconds) {
        Date date = new Date(TimeUnit.SECONDS.toMillis(timeSeconds));
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a, M/d");
        return dateFormat.format(date);
    }
}
