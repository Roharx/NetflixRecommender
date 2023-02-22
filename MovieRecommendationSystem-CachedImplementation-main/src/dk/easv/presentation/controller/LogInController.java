package dk.easv.presentation.controller;

import dk.easv.entities.User;
import dk.easv.presentation.model.AppModel;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.ToDoubleBiFunction;

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
        //TODO get it to display while app is "frozen/loading"

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
        //MinimizeWindow();
        primaryStage.show();

        primaryStage.widthProperty().addListener((o, oldValue, newValue)->{
            if(newValue.intValue() < 850.0) {
                primaryStage.setResizable(false);
                primaryStage.setWidth(850);
                primaryStage.setResizable(true);
            }
        });
    }

    private void MinimizeWindow(){
        Stage stage = (Stage) btnLogin.getScene().getWindow();
        stage.setIconified(true);
    }

    public void registerNewUser(ActionEvent actionEvent) {
        //TODO only if we have enough time, otherwise delete

    }


    //TODO delete if not used by the end of the project
    /*public void logIn(ActionEvent actionEvent) throws IOException {
        model.loadUsers();
        model.loginUserFromUsername(userId.getText());
        if(model.getObsLoggedInUser()!=null){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("dk/easv/presentation/controller/NetflixAppController.java"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Movie Recommendation System 0.01 Beta");
            stage.show();


            AppController controller = loader.getController();

           controller.setModel(model);



       } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Could not load App.fxml");
            alert.showAndWait();
        }

        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR, "Wrong username or password");
            alert.showAndWait();
        }
    }

    public void signUp(ActionEvent actionEvent) {
        System.out.println("Sign-Up");
    }*/

}
