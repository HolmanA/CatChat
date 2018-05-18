package catchat.ui.messages;

import catchat.data.source.entities.chat.Chat;
import catchat.data.source.entities.message.Message;
import catchat.model.ModelContract;
import catchat.model.chat.ChatContract;

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
            text = text.trim();
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
    public void sameChatSelected() {
        if (view.chatPainVisible()) {
            view.hideChatPane();
        } else {
            view.showChatPane();
        }
    }

    @Override
    public void chatChanged() {
        ChatContract.Model chatModel = model.getSelectedChatModel();
        List<Message> reverseList = new ArrayList<>(chatModel.getMessages());
        Collections.reverse(reverseList);
        Chat chat = chatModel.getChat();

        int prevListSize = view.getMessagesSize();
        view.clearMessages();
        view.setMessages(reverseList);
        view.scrollMessagesTo(reverseList.size() - prevListSize);
        view.setChatTitle(chat.getName());
    }

    @Override
    public void messageSent() {
        view.clearMessageText();
        reloadMessages();
    }
}
