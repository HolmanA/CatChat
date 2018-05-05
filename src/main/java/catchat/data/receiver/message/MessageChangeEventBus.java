package catchat.data.receiver.message;

import catchat.data.entities.message.Message;
import catchat.data.source.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/29/18.
 */
public class MessageChangeEventBus implements DataSource.SendMessageCallback,
        MessageReceiver.MessageReceivedCallback {
    private List<MessageChangeListener> listeners;

    public MessageChangeEventBus() {
        listeners = new ArrayList<>();
    }

    public void subscribe(MessageChangeListener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(MessageChangeListener listener) {
        listeners.remove(listener);
    }

    public void unsubscribeAll() {
        listeners.clear();
    }

    @Override
    public void unknownResponseCode(String response) {

    }

    @Override
    public void onMessageSent() {
        alertAll(null);
    }

    @Override
    public void onMessageReceived(Message message) {
        alertAll(message);
    }

    private void alertAll(Message message) {
        for (MessageChangeListener listener : listeners) {
            listener.changed(message);
        }
    }
}
