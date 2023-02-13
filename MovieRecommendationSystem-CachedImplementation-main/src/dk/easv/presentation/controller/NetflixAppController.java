package dk.easv.presentation.controller;

import dk.easv.entities.Movie;
import dk.easv.entities.User;
import dk.easv.presentation.model.AppModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class NetflixAppController implements Initializable {
    @FXML
    private AnchorPane anchorControlFrame,
            anchorDisplay;
    @FXML
    private Button btnHome,
            btnSearch,
            btnNewestMovie,
            btnHistory,
            btnMyListMovie,
            btnMenu;
    @FXML
    private TextField txfSearch;
    @FXML
    private ImageView imageViewUser;


    private AppModel appModel;
    private User currentUser;
    private int displayElementWidth = 270, displayElementHeight = 150;
    private int paddingWidth = 20, paddingHeight = 20;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appModel = new AppModel();
        currentUser = appModel.getLoggedInUser();
        appModel.loadData(currentUser);
        displayContent(anchorDisplay);
    }

    private ObservableList<Movie> getMovieRecommendations(int movieAmount) {
        ObservableList<Movie> allMoviesToBeSeen = appModel.getObsTopMovieNotSeen();
        ObservableList<Movie> amountMoviesToBeSeen = FXCollections.observableArrayList();

        for (int i = 0; i < movieAmount; i++) {
            amountMoviesToBeSeen.add(allMoviesToBeSeen.get(i));
        }

        return amountMoviesToBeSeen;
    }

    private void displayContent(AnchorPane contentContainer) {
        Label lblTop = new Label();
        Label lblBottom = new Label();
        ObservableList<Movie> recommendations = getMovieRecommendations(4);

        AnchorPane.setLeftAnchor(lblTop, 5.0);
        AnchorPane.setTopAnchor(lblBottom, 225.0);
        AnchorPane.setLeftAnchor(lblBottom, 5.0);

        lblTop.setText("Recommendations");
        lblBottom.setText("Trending");

        lblTop.getStyleClass().add("display-text");
        lblBottom.getStyleClass().add("display-text");

        contentContainer.getChildren().add(lblTop);
        displayMovies(contentContainer, 4, 1, recommendations);
        contentContainer.getChildren().add(lblBottom);
        displayMovies(contentContainer, 4, 2, recommendations);
    }

    private void displayMovies(AnchorPane contentContainer, int amount, int row, ObservableList<Movie> moviesToDisplay) {
        String url;

        for (int i = 0; i < amount; i++) {
            StackPane displayElement = new StackPane();
            Label movieTitle = new Label();
            ImageView moviePicture = new ImageView();

            url = appModel.getMoviePicturePathByID(moviesToDisplay.get(i).getId());

            if (url != "")
                try{
                    moviePicture.setImage(new Image(url, displayElementWidth, displayElementHeight - 10, true, false));
                }
                catch (Exception e){
                    //TODO if have enough time, display text for wrong path for picture otherwise leave empty
            }

            movieTitle.setText(moviesToDisplay.get(i).getTitle());
            movieTitle.getStyleClass().add("movie-title");
            moviePicture.getStyleClass().add("movie-picture");

            displayElement.setMinSize(displayElementWidth, displayElementHeight);
            displayElement.getChildren().add(moviePicture);
            displayElement.getChildren().add(movieTitle);
            displayElement.getStyleClass().add("display-element");
            displayElement.setAlignment(Pos.BOTTOM_CENTER);

            AnchorPane.setTopAnchor(displayElement, (50.0 + (row - 1) * 250));
            AnchorPane.setLeftAnchor(displayElement, (20.0 + i * 290));


            displayElement.setOnMouseEntered(e -> {
                displayElement.setViewOrder(-1.0);
            });
            displayElement.setOnMouseExited(e -> {
                displayElement.setViewOrder(0);
            });

            contentContainer.getChildren().add(displayElement);
        }

    }

}
