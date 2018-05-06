package catchat.data;

import catchat.data.entities.message.Message;
import catchat.data.receiver.message.MessageReceiver;
import catchat.data.source.DataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrew on 4/29/18.
 */
public class MessageEventBus implements DataSource.SendMessageCallback,
        MessageReceiver.MessageReceivedCallback {
    public interface Listener {
        void changed(Message message);
    }

    private List<Listener> listeners;

    public MessageEventBus() {
        listeners = new ArrayList<>();
    }

    public void subscribe(Listener listener) {
        listeners.add(listener);
    }

    public void unsubscribe(Listener listener) {
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
        for (Listener listener : listeners) {
            listener.changed(message);
        }
    }
}
