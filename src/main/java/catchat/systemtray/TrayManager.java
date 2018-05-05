package catchat.systemtray;

import catchat.data.entities.message.Message;
import catchat.data.receiver.message.MessageChangeEventBus;
import catchat.data.receiver.message.MessageChangeListener;
import java.awt.SystemTray;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.AWTException;

/**
 * Created by andrew on 5/4/18.
 */
public class TrayManager implements MessageChangeListener {
    private MessageChangeEventBus eventBus;
    private TrayIcon trayIcon;

    public TrayManager(MessageChangeEventBus eventBus) {
        this.eventBus = eventBus;
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            Image image = Toolkit.getDefaultToolkit().createImage("");
            trayIcon = new TrayIcon(image, "CatChat");
            try {
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribeToEventBus() {
        if (SystemTray.isSupported()) {
            eventBus.subscribe(this);
        }
    }

    public void unsubscribeFromEventBus() {
        eventBus.unsubscribe(this);
    }

    @Override
    public void changed(Message message) {
        if (message != null) {
            trayIcon.displayMessage(message.getSenderName(), message.getText(), TrayIcon.MessageType.INFO);
        }
    }
}
