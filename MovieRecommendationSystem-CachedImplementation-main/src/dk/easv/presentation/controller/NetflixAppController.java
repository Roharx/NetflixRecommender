package dk.easv.presentation.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NetflixAppController implements Initializable {
    @FXML
    private AnchorPane anchorControlFrame;
    @FXML
    private Button btnHome,btnSearch,btnNewestMovie,btnHistory,btnMyListMovie,btnMenu;
    @FXML
    private TextField txfSearch;
    @FXML
    private ImageView imageViewUser;
    @FXML
    private AnchorPane anchorDisplay;
    @FXML
    private HBox hBoxContinue;
    @FXML
    private ImageView imageView1,imageView2,imageView3,imageView4;
    @FXML
    private HBox hBoxTrending;
    @FXML
    private ImageView imageView5,imageView6,imageView7,imageView8;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
