package catchat.data.model.chatlist;

import catchat.data.entities.chat.Chat;
import catchat.data.source.ApiCommand;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.group.GetGroupChatsCommand;

import java.io.IOException;
import java.util.List;

/**
 * GroupChatListModel
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public class GroupChatListModel extends BaseChatListModel {
    public GroupChatListModel(ApiInvoker invoker) {
        super(invoker);
    }

    @Override
    protected ApiCommand<List<Chat>> getChatsCommand(ApiCommand.Listener<List<Chat>> listener, int page, int pageSize) throws IOException {
        return new GetGroupChatsCommand(listener, page, pageSize);
    }
}
