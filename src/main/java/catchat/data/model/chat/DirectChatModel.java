package catchat.data.model.chat;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.BaseGroupMeApiCommand;
import catchat.data.source.groupme.direct.GetDirectMessagesApiCommand;
import catchat.data.source.groupme.direct.SendDirectMessageApiCommand;

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
    protected BaseGroupMeApiCommand<List<Message>> getMessagesCommand(BaseGroupMeApiCommand.Listener<List<Message>> listener, String chatId, String beforeId, String sinceId) throws IOException {
        return new GetDirectMessagesApiCommand(listener, chatId, beforeId, sinceId);
    }

    @Override
    protected BaseGroupMeApiCommand<Void> sendMessageCommand(BaseGroupMeApiCommand.Listener<Void> listener, String chatId, String messageId, String messageText) throws IOException {
        return new SendDirectMessageApiCommand(listener, chatId, messageId, messageText);
    }

}
