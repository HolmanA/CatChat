package catchat.data.receiver.message;

import catchat.data.entities.message.Message;

/**
 * Created by andrew on 4/29/18.
 */
public interface MessageChangeListener {
    void changed(Message message);
}
