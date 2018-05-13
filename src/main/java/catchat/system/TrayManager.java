package catchat.system;

import catchat.data.DataMediator;
import catchat.data.entities.chat.Chat;
import catchat.data.entities.message.Message;
import catchat.data.entities.profile.Profile;
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
public class TrayManager implements DataMediator.Listener {
    private DataMediator dataMediator;
    private TrayIcon trayIcon;

    public TrayManager(DataMediator dataMediator) {
        this.dataMediator = dataMediator;
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

    public void subscribeToDataMediator() {
        if (SystemTray.isSupported()) {
            dataMediator.subscribe(this);
        }
    }

    public void unsubscribeFromDataMediator() {
        dataMediator.unsubscribe(this);
    }

    @Override
    public void onMessageReceived(NotificationMessage message) {
        if (message != null) {
            trayIcon.displayMessage(message.getSenderName(), message.getMessageText(), TrayIcon.MessageType.INFO);
        }
    }

    @Override
    public void onChatsLoaded(List<Chat> chats) {}
    @Override
    public void onChatLoaded(Chat chat) {}
    @Override
    public void onMessagesLoaded(List<Message> messages) {}
    @Override
    public void onMessageSent() {}
    @Override
    public void onMessageLiked() {}
    @Override
    public void onMessageUnliked() {}
    @Override
    public void onProfileLoaded(Profile profile) {}
}
