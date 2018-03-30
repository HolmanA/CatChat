package catchat.authentication;

import javafx.stage.Stage;

import java.awt.*;
import java.net.URI;

/**
 * Created by andrew on 3/26/18.
 */
public class AuthStage extends Stage implements AuthContract.View {
    private AuthContract.Presenter presenter;

    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void openWebPage(String url) {
        try {
            // Open the provided URL using the system's default web browser
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void closeWebPage() {
        hide();
    }
}
