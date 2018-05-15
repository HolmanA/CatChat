package catchat.data.model.chat;

import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.source.ApiCommand;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.group.GetGroupMessagesCommand;
import catchat.data.source.groupme.group.SendGroupMessageCommand;

import java.io.IOException;
import java.util.List;

/**
 * DirectChatModel
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public class GroupChatModel extends BaseChatModel {
    public GroupChatModel(ApiInvoker invoker, Chat chat) {
        super(invoker, chat);
    }

    @Override
    protected ApiCommand<List<Message>> getMessagesCommand(ApiCommand.Listener<List<Message>> listener, String chatId, String beforeId, String sinceId) throws IOException {
        return new GetGroupMessagesCommand(listener, chatId, beforeId, sinceId, "", 20);
    }

    @Override
    protected ApiCommand<Void> sendMessageCommand(ApiCommand.Listener<Void> listener, String chatId, String messageId, String messageText) throws IOException {
        return new SendGroupMessageCommand(listener, chatId, messageId, messageText);
    }

}
