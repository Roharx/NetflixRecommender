package dk.easv.presentation.controller;

import dk.easv.entities.Movie;
import dk.easv.entities.User;
import dk.easv.presentation.model.AppModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    private ObservableList<Movie> allMovies;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appModel = new AppModel();
        currentUser = appModel.getLoggedInUser();
        appModel.loadData(currentUser);
        displayHomeContent(anchorDisplay,"Recommendations","Trending");

        txfSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<Movie> searchResults = FXCollections.observableArrayList();

                for (Movie m : searchResults) {
                    System.out.println(m.getTitle());
                }
            }
        });
    }


    private ObservableList<Movie> getMovieRecommendations(int movieAmount) {
        ObservableList<Movie> allMoviesToBeSeen = appModel.getObsTopMovieNotSeen();
        ObservableList<Movie> amountMoviesToBeSeen = FXCollections.observableArrayList();

        for (int i = 0; i < movieAmount; i++) {
            amountMoviesToBeSeen.add(allMoviesToBeSeen.get(i));
        }

        return amountMoviesToBeSeen;
    }

    private void displayHomeContent(AnchorPane contentContainer, String topLabel, String bottomLabel) {
        clearContent(contentContainer);

        Label lblTop = new Label();
        Label lblBottom = new Label();
        ObservableList<Movie> recommendations = getMovieRecommendations(4);

        AnchorPane.setLeftAnchor(lblTop, 5.0);
        AnchorPane.setTopAnchor(lblBottom, 225.0);
        AnchorPane.setLeftAnchor(lblBottom, 5.0);

        lblTop.setText(topLabel);
        lblBottom.setText(bottomLabel);

        lblTop.getStyleClass().add("display-text");
        lblBottom.getStyleClass().add("display-text");

        contentContainer.getChildren().add(lblTop);
        displayMovieIcons(contentContainer, 4, 1, true, recommendations);
        contentContainer.getChildren().add(lblBottom);
        displayMovieIcons(contentContainer, 4, 2, true, recommendations);
    }

    private void clearContent(AnchorPane contentContainer) {
        contentContainer.getChildren().clear();
    }

    private void displayMovieIcons(AnchorPane contentContainer, int amount, int row, boolean hasLabel, ObservableList<Movie> moviesToDisplay) {
        String url;
        double xSpacing;
        double ySpacing;

        for (int i = 0; i < amount; i++) {
            xSpacing = 20.0 + i * 290;
            if (hasLabel) {
                ySpacing = 50.0 + ((row - 1) * 250);
            } else {
                ySpacing = 150.0 + ((row - 1) * 175);
            }

            StackPane displayElement = new StackPane();
            Label movieTitle = new Label();
            ImageView moviePicture = new ImageView();

            url = appModel.getMoviePicturePathByID(moviesToDisplay.get(i).getId());

            if (url != "")
                try {
                    moviePicture.setImage(new Image(url, displayElementWidth, displayElementHeight - 10, true, false));
                } catch (Exception e) {
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

            AnchorPane.setTopAnchor(displayElement, ySpacing);
            AnchorPane.setLeftAnchor(displayElement, xSpacing);


            displayElement.setOnMouseEntered(e -> {
                displayElement.setViewOrder(-1.0);
            });
            displayElement.setOnMouseExited(e -> {
                displayElement.setViewOrder(0);
            });

            contentContainer.getChildren().add(displayElement);
        }

    }

    private void displayMoviesOnly(AnchorPane contentContainer, ObservableList<Movie> searchResults) {
        if (searchResults != null){
            int maxRow = (int) ((searchResults.size() / 4) + 0.9);
            maxRow = maxRow > 4 ? 4 : maxRow;
            int amount = searchResults.size() > 12 ? 12 : searchResults.size();
            ObservableList<Movie> rowContent = FXCollections.observableArrayList();

            clearContent(contentContainer);

            for (int i = 0; i < maxRow; i++) {
                for (int j = i; j < i + 4; j++) {
                    rowContent.add(searchResults.get(j));
                }
                displayMovieIcons(contentContainer, rowContent.size(), i, false, rowContent);
                rowContent.clear();
            }
        }
    }

    private ObservableList<Movie> getNewestMovies(){
        ObservableList<Movie> newestMovies = FXCollections.observableArrayList();



        return newestMovies;
    }


   /* public void logOut(ActionEvent actionEvent) {

        Stage stage  = (Stage) scenePane.getScene().getWindow();
        stage.close();

    }*/


}
