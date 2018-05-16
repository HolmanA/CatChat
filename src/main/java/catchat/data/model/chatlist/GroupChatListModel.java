package catchat.data.model.chatlist;

import catchat.data.entities.chat.Chat;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.BaseGroupMeApiCommand;
import catchat.data.source.groupme.group.GetGroupChatsApiCommand;

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
    protected BaseGroupMeApiCommand<List<Chat>> getChatsCommand(BaseGroupMeApiCommand.Listener<List<Chat>> listener, int page, int pageSize) throws IOException {
        return new GetGroupChatsApiCommand(listener, page, pageSize);
    }
}
