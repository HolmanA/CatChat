package catchat.model.userprofile;

import catchat.data.source.entities.profile.UserProfile;
import catchat.data.source.ApiInvoker;
import catchat.data.source.groupme.GetUserProfileApiCommand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * UserProfileModel
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public class UserProfileModel implements UserProfileContract.Model {
    private List<UserProfileContract.Listener> listeners;
    private ApiInvoker invoker;
    private UserProfile userProfile;

    public UserProfileModel(ApiInvoker invoker) {
        this.invoker = invoker;
        listeners = new ArrayList<>();
    }

    @Override
    public void loadUserProfile() {
        try {
            invoker.execute(new GetUserProfileApiCommand(result -> {
                userProfile = result;
                for (UserProfileContract.Listener listener : listeners) {
                    listener.userProfileChanged();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public UserProfile getUserProfile() {
        return userProfile;
    }

    @Override
    public void subscribe(UserProfileContract.Listener listener) {
        listeners.add(listener);
    }

    @Override
    public void unsubscribe(UserProfileContract.Listener listener) {
        listeners.remove(listener);
    }

    @Override
    public void unsubscribeAll() {
        listeners.clear();
    }
}
