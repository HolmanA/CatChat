package catchat.model.userprofile;

import catchat.data.source.entities.profile.UserProfile;

/**
 * UserProfileContract
 *
 * @author Andrew Holman
 * @version 1.0
 * @since 1.0
 */
public interface UserProfileContract {
    interface Listener {
        void userProfileChanged();
    }

    interface Model {
        void subscribe(Listener listener);

        void unsubscribe(Listener listener);

        void unsubscribeAll();

        void loadUserProfile();

        UserProfile getUserProfile();
    }
}
