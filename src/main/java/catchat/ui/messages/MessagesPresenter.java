package catchat.ui.messages;

import catchat.data.entities.message.Message;
import catchat.data.model.ModelContract;
import catchat.data.model.chat.ChatContract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by andrew on 4/16/18.
 */
public class MessagesPresenter implements MessagesContract.Presenter, ModelContract.Listener, ChatContract.Listener {
    private ModelContract.Model model;
    private MessagesContract.View view;

    public MessagesPresenter(ModelContract.Model model, MessagesContract.View view) {
        this.model = model;
        this.view = view;
        model.subscribe(this);
    }

    @Override
    public void start() {
        view.hideChatPane();
    }

    @Override
    public void reloadMessages() {
        view.clearMessages();
        model.getSelectedChatModel().reloadMessages();
    }

    @Override
    public void loadMoreMessages() {
        model.getSelectedChatModel().loadMoreMessages();
    }

    @Override
    public void sendMessage() {
        String text;
        if ((text = view.getMessageText()) != null && !text.equals("")) {
            model.getSelectedChatModel().sendMessage(text);
        }
    }

    @Override
    public void likeMessage(Message message) {
        view.clearMessages();
        model.getSelectedChatModel().likeMessage(message);
    }

    @Override
    public void unlikeMessage(Message message) {
        view.clearMessages();
        model.getSelectedChatModel().unlikeMessage(message);
    }

    @Override
    public void selectionChanged() {
        if (model.getSelectedChatModel() != null) {
            view.clearMessages();
            model.getSelectedChatModel().subscribe(this);
            model.getSelectedChatModel().reloadMessages();
            view.showChatPane();
        }
    }

    @Override
    public void chatChanged() {
        int size = view.getMessagesSize();
        view.clearMessages();
        List<Message> reverseList = new ArrayList<>(model.getSelectedChatModel().getMessages());
        Collections.reverse(reverseList);
        view.setMessages(reverseList);
        view.scrollMessagesTo(reverseList.size() - size);
    }

    @Override
    public void messageSent() {
        view.clearMessageText();
        reloadMessages();
    }
}
