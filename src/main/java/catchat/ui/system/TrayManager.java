package catchat.ui.system;

import catchat.data.model.Model;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.receiver.message.MessageReceiverContract;
import catchat.data.receiver.message.NotificationMessage;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.net.URL;
import java.util.List;

/**
 * Created by andrew on 5/4/18.
 */
public class TrayManager implements MessageReceiverContract.Listener {
    private MessageReceiverContract.Receiver receiver;
    private TrayIcon trayIcon;

    public TrayManager(MessageReceiverContract.Receiver receiver) {
        this.receiver = receiver;
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            URL resourceUrl = getClass().getResource("/system/SystemTrayIcon.png");
            Image image = Toolkit.getDefaultToolkit().getImage(resourceUrl);
            trayIcon = new TrayIcon(image, "CatChat");
            trayIcon.setImageAutoSize(true);
            try {
                systemTray.add(trayIcon);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribeToMessageReceiver() {
        if (SystemTray.isSupported()) {
            receiver.subscribe(this);
        }
    }

    public void unsubscribeFromMessageReceiver() {
        receiver.unsubscribe(this);
    }

    @Override
    public void messageReceived(NotificationMessage message) {
        if (message != null) {
            trayIcon.displayMessage(message.getSenderName(), message.getMessageText(), TrayIcon.MessageType.INFO);
        }
    }
}
