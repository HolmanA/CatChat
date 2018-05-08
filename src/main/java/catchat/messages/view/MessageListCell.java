package catchat.messages.view;

import catchat.data.entities.message.Message;
import catchat.messages.MessagesContract;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by andrew on 4/17/18.
 */
public class MessageListCell extends ListCell<Message> {
    private MessagesContract.Presenter presenter;

    public MessageListCell(MessagesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void updateItem(Message item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            Label senderName = new Label(item.getSenderName());
            senderName.setFont(Font.font(null, FontWeight.BOLD, 12));

            Label createdAt = new Label(formatTime(item.getCreatedAt()));
            createdAt.setFont(Font.font(null, FontPosture.ITALIC, 12));

            HBox senderBox = new HBox();
            senderBox.setSpacing(10);
            senderBox.setAlignment(Pos.CENTER_LEFT);
            senderBox.getChildren().addAll(senderName, createdAt);

            Text text = new Text(item.getText());
            text.setFont(Font.font(12));
            text.setWrappingWidth(800);

            Button like = new Button("like");
            like.setOnMouseClicked(event -> presenter.likeMessage(item));

            Button unlike = new Button("unlike");
            unlike.setOnMouseClicked(event -> presenter.unlikeMessage(item));

            Label likeCount = new Label(Integer.toString(item.getLikeCount()));
            likeCount.setFont(Font.font(null, FontPosture.ITALIC, 12));
            likeCount.setTextAlignment(TextAlignment.CENTER);

            HBox likeButtons = new HBox();
            likeButtons.setSpacing(5);
            likeButtons.setAlignment(Pos.CENTER_LEFT);
            likeButtons.getChildren().addAll(like, unlike, likeCount);

            VBox container = new VBox();
            container.setSpacing(5);
            container.setAlignment(Pos.CENTER_LEFT);
            container.getChildren().addAll(senderBox, text, likeButtons);
            setGraphic(container);
        } else {
            setGraphic(null);
        }
    }

    private String formatTime(long timeSeconds) {
        Date date = new Date(TimeUnit.SECONDS.toMillis(timeSeconds));
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a, M/d");
        return dateFormat.format(date);
    }
}
