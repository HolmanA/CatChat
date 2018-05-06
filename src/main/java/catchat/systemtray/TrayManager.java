package catchat.systemtray;

import catchat.data.entities.message.Message;
import catchat.data.MessageEventBus;

import javax.imageio.ImageIO;
import java.awt.SystemTray;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.AWTException;
import java.io.IOException;

/**
 * Created by andrew on 5/4/18.
 */
public class TrayManager implements MessageEventBus.Listener {
    private MessageEventBus eventBus;
    private TrayIcon trayIcon;

    public TrayManager(MessageEventBus eventBus) {
        this.eventBus = eventBus;
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/SystemTrayIcon.png"));
                trayIcon = new TrayIcon(image, "CatChat");
                trayIcon.setImageAutoSize(true);
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
