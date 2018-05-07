package catchat.authentication;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.awt.Desktop;
import java.net.URI;

/**
 * Created by andrew on 3/26/18.
 */
public class AuthView extends VBox implements AuthContract.View {
    private AuthContract.Presenter presenter;
    private Text prompt;
    private Button login;
    private HBox hideDialogBox;
    private Label hideDialogLabel;
    private CheckBox hideDialog;

    public AuthView() {
        super(5);
        prompt = new Text("Cat Chat Login");
        login = new Button("Login");
        login.setOnMouseClicked(event -> presenter.authenticate());
        hideDialog = new CheckBox();
        hideDialog.setOnMouseClicked(event -> presenter.hideOnStart(hideDialog.isSelected()));
        hideDialogLabel = new Label("Check this box to hide this dialog on startup");
        hideDialogBox = new HBox();
        hideDialogBox.getChildren().addAll(hideDialog, hideDialogLabel);
        getChildren().setAll(prompt, login, hideDialogBox);
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
        hideDialogBox.setVisible(false);
    }
}
