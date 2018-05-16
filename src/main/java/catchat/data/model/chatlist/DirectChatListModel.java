package catchat.data.model.chatlist;

import catchat.data.entities.chat.Chat;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.BaseGroupMeApiCommand;
import catchat.data.source.groupme.direct.GetDirectChatsApiCommand;

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
    protected BaseGroupMeApiCommand<List<Chat>> getChatsCommand(BaseGroupMeApiCommand.Listener<List<Chat>> listener, int page, int pageSize) throws IOException {
        return new GetDirectChatsApiCommand(listener, page, pageSize);
    }
}
