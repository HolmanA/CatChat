package catchat.data.model.chat;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.ApiCommand;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.direct.GetDirectMessagesCommand;
import catchat.data.source.groupme.direct.SendDirectMessageCommand;

import java.io.IOException;
import java.util.List;

/**
 * DirectChatModel
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public class DirectChatModel extends BaseChatModel {
    public DirectChatModel(ApiInvoker invoker, Chat chat) {
        super(invoker, chat);
    }

    @Override
    protected ApiCommand<List<Message>> getMessagesCommand(ApiCommand.Listener<List<Message>> listener, String chatId, String beforeId, String sinceId) throws IOException {
        return new GetDirectMessagesCommand(listener, chatId, beforeId, sinceId);
    }

    @Override
    protected ApiCommand<Void> sendMessageCommand(ApiCommand.Listener<Void> listener, String chatId, String messageId, String messageText) throws IOException {
        return new SendDirectMessageCommand(listener, chatId, messageId, messageText);
    }

}
