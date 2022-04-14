package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConfigController {
    @FXML
    protected GridPane generalGridPane;

    @FXML
    protected AnchorPane generalAnchorPane;

    @FXML
    private TextField hostTextField;

    @FXML
    private TextField portTextField;

    @FXML
    private TextField userTextField;

    @FXML
    private PasswordField passwordPasswordField;

    @FXML
    private TextField databaseTextField;

    @FXML
    private Button connectButton;

    @FXML
    private Label issueLabel;

    @FXML
    void onClickConnectButton() throws Exception {
        issueLabel.setVisible(false);

        DBConfig.host = hostTextField.getText();
        DBConfig.port = portTextField.getText();
        DBConfig.user = userTextField.getText();
        DBConfig.password = passwordPasswordField.getText();
        DBConfig.database = databaseTextField.getText();

        connect();
    }

    private void connect() throws Exception {
        DBHandler dbHandler = new DBHandler();
        if(dbHandler.testConnection()) {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

            connectButton.getScene().getWindow().hide();
            stage.setScene(new Scene(root, 1035, 542));
            stage.setTitle("Экзаменационная сессия");
            stage.setResizable(false);
            stage.show();
        } else {
            issueLabel.setVisible(true);
        }
    }
}
