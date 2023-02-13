package dk.easv.presentation.controller;

import dk.easv.presentation.model.AppModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class NetflixAppController implements Initializable {


    @FXML
    private AnchorPane scenePane, anchorControlFrame;
    @FXML
    private Button btnHome, btnSearch, btnNewestMovie, btnHistory, btnMyListMovie, btnMenu;
    @FXML
    private TextField txfSearch;
    @FXML
    private ImageView imageViewUser;
    @FXML
    private AnchorPane anchorDisplay;
    @FXML
    private HBox hBoxContinue;
    @FXML
    private ImageView imageView1, imageView2, imageView3, imageView4;
    @FXML
    private HBox hBoxTrending;
    @FXML
    private ImageView imageView5, imageView6, imageView7, imageView8;

    private AppModel appModel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appModel = new AppModel();
        txfSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                try {
                    appModel.search(newValue);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

    }

    public void searchMovies(ActionEvent actionEvent) {
        // TODO search on button click
    }

    public void logOut(ActionEvent actionEvent) {

        Stage stage  = (Stage) scenePane.getScene().getWindow();
        stage.close();

    }




























    /*private void searchFilter() {
        FilteredList<Movie> filteredData = new FilteredList<>(moviesList,e-> true);
        txfSearch.setOnKeyReleased(event -> {

            txfSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate((Predicate<? super Movie >) cust-> {
                    if (newValue == null) {
                        return true;
                    }
                    String toLowerCaseFilter = newValue.toLowerCase();
                    if (cust.getTitle().toLowerCase().contains(toLowerCaseFilter)) {
                        return true;
                    }
                    return false;
                });

            });
            final SortedList<Movie> movies = new SortedList<>(filteredData);
            //movies.comparatorProperty().bind();
        });
    }*/

}
