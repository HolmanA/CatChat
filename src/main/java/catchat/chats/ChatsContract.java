package catchat.chats;

import catchat.BasePresenter;
import catchat.BaseView;
import catchat.data.entities.chat.Chat;

import java.util.List;

/**
 * Created by andrew on 4/15/18.
 */
public interface ChatsContract {
    interface View extends BaseView<Presenter> {
        void showGroups(List<Chat> groups);
    }

    interface Presenter extends BasePresenter {
        void refreshGroups();
        void loadGroup(Chat group);
    }
}
