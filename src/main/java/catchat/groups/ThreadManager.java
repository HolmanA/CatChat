package catchat.groups;

import catchat.data.auth.OAuthService;
import catchat.data.source.threads.GroupMeGroupsDS;
import catchat.data.source.threads.ThreadDataSource;

import java.util.List;

/**
 * Created by andrew on 4/14/18.
 */
public class ThreadManager {
    private OAuthService authService;
    private ThreadDataSource dataSource;

    public ThreadManager(OAuthService authService) {
        this.authService = authService;
        dataSource = new GroupMeGroupsDS();
        dataSource.setAuthenticationToken(authService.getAPIToken());
    }

    public void start() {
        dataSource.getThreads(1, 10, "", new ThreadDataSource.GetThreadsCallback() {
            @Override
            public void onThreadsLoaded(List<Thread> threads) {

            }

            @Override
            public void dataNotAvailable() {
                System.out.println("not available");
            }

            @Override
            public void notAuthorized() {

            }
        });
    }
}
