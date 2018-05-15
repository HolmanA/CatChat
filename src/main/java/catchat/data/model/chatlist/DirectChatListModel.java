package catchat.data.model.chatlist;

import catchat.data.entities.chat.Chat;
import catchat.data.source.ApiCommand;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.direct.GetDirectChatsCommand;

import java.io.IOException;
import java.util.List;

/**
 * DirectChatListModel
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public class DirectChatListModel extends BaseChatListModel {
    public DirectChatListModel(ApiInvoker invoker) {
        super(invoker);
    }

    @Override
    protected ApiCommand<List<Chat>> getChatsCommand(ApiCommand.Listener<List<Chat>> listener, int page, int pageSize) throws IOException {
        return new GetDirectChatsCommand(listener, page, pageSize);
    }
}
