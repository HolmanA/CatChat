package catchat.messages;

import catchat.data.entities.chat.GroupChat;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.mockito.Mockito.verify;

/**
 * Created by andrew on 4/22/18.
 */
public class MessagesPresenterTest {
    @Mock
    private MessagesContract.View messagesView;

    private MessagesPresenter messagesPresenter;

    @Before
    public void setupMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void onChatLoaded_ValidGroupChat() {
        messagesPresenter = new MessagesPresenter(messagesView);
        GroupChat mockGroupChat = new GroupChat("123", "test", "text", new ArrayList<>());
        messagesPresenter.onChatLoaded(mockGroupChat);
        verify(messagesView).showChatDetails(mockGroupChat);
    }

    @Test
    public void onChatLoaded_NullChat() {
        messagesPresenter = new MessagesPresenter(messagesView);
        messagesPresenter.onChatLoaded(null);
    }
}
