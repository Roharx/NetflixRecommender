package dk.easv.presentation.controller;

import dk.easv.presentation.model.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LogInController implements Initializable {
    @FXML
    private Button btnRecover,
            btnLogin,
            btnRegisterNewUser;
    @FXML private PasswordField passwordField;
    @FXML private TextField userId;
    private AppModel model;
    private Parent root;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        model = new AppModel();
    }

    public void logIn(ActionEvent actionEvent) throws IOException {

        btnLogin.setText("Logging in...");
        btnLogin.setDisable(true);


        model.setLoggedInUser(model.getUserByUsername(userId.getText()));

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("dk/easv/presentation/view/NetflixApp.fxml"));

        root = loader.load();

        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setTitle("NetflixApp");
        primaryStage.setScene(scene);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        btnLogin.setText("Login");
        btnLogin.setDisable(false);

        primaryStage.setWidth(1250);
        primaryStage.show();
    }

    private void MinimizeWindow(){
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setIconified(true);
    }

    public void registerNewUser(ActionEvent actionEvent) {
    }
}
