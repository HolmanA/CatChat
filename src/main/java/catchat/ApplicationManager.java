package catchat;

import catchat.data.auth.OAuthService;
import catchat.groups.ThreadManager;

/**
 * Created by andrew on 4/14/18.
 */
public class ApplicationManager {
    private OAuthService service;

    public ApplicationManager(OAuthService service) {
        this.service = service;
    }

    public void start() {
        System.out.println("Main Application Started");
        ThreadManager manager = new ThreadManager(service);
        manager.start();
    }
}
