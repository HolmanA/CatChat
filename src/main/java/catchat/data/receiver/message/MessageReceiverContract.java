package catchat.data.receiver.message;

/**
 * MessageReceiverContract
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public interface MessageReceiverContract {
    interface Listener {
        void messageReceived(NotificationMessage message);
    }

    interface Receiver {
        void subscribe(Listener listener);

        void unsubscribe(Listener listener);

        void unsubscribeAll();

        void start();

        void stop();
    }
}
