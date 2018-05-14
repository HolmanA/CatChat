package catchat.ui.authentication;

import catchat.ui.BasePresenter;
import catchat.ui.BaseView;

/**
 * Created by andrew on 3/25/18.
 */
public interface AuthContract {
    interface View extends BaseView<Presenter> {
        void openWebPage(String url);
        void showAuthenticating();
    }

    interface Presenter extends BasePresenter {
        void authenticate();
    }
}
