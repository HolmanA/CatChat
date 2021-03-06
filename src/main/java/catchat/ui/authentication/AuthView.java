package catchat.ui.authentication;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.Desktop;
import java.net.URI;

/**
 * Created by andrew on 3/26/18.
 */
public class AuthView extends VBox implements AuthContract.View {
    private AuthContract.Presenter presenter;
    private Label title;
    private Text prompt;
    private Button login;

    public AuthView() {
        super();
        getStylesheets().add("/authentication/css/auth_view.css");
        getStyleClass().add("container");

        title = new Label("Cat Chat Login");
        title.getStyleClass().add("title");

        String promptMessage = "By logging into your GroupMe account via the button below, you hereby grant Cat Chat ";
        promptMessage += "the right to access your GroupMe account information for the sole purpose of chatting with ";
        promptMessage += "other cats.";

        prompt = new Text(promptMessage);
        prompt.getStyleClass().add("prompt");
        prompt.setWrappingWidth(350);

        login = new Button("Login");
        login.getStyleClass().add("login");
        login.setOnMouseClicked(event -> presenter.authenticate());
        login.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                presenter.authenticate();
            }
        });

        getChildren().setAll(title, new Separator(), prompt, new Separator(), login);
    }

    @Override
    public void setPresenter(AuthContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void openWebPage(String url) {
        try {
            System.out.println("Opening Authentication Site in Default Browser...");
            // Open the provided URL using the system's default web browser
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public void showAuthenticating() {
        prompt.setText("Authenticating...");
        login.setVisible(false);
    }
}
