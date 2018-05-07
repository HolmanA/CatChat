package catchat.authentication;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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
        super(5);
        title = new Label("Cat Chat Login");
        title.setFont(Font.font(null, FontWeight.BOLD, 18));

        String promptMessage = "By logging into your GroupMe account via the button below, you hereby grant Cat Chat ";
        promptMessage += "the right to access your GroupMe account information for the sole purpose of chatting with ";
        promptMessage += "other cats.";
        prompt = new Text(promptMessage);
        prompt.setFont(Font.font(null, FontWeight.NORMAL, 14));
        prompt.setTextAlignment(TextAlignment.CENTER);
        prompt.setWrappingWidth(350);

        login = new Button("Login");
        login.setOnMouseClicked(event -> presenter.authenticate());

        getChildren().setAll(title, prompt, login);
        setAlignment(Pos.CENTER);
        setPadding(new Insets(20));
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
