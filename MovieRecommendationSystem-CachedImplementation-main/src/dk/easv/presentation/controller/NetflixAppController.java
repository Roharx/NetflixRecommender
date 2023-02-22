package dk.easv.presentation.controller;

import dk.easv.entities.Movie;
import dk.easv.entities.User;
import dk.easv.presentation.model.AppModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class NetflixAppController implements Initializable {
    @FXML
    public ContextMenu ctmUserMenu;
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

    private AppModel appModel;
    private User currentUser;
    private int displayElementWidth = 270, displayElementHeight = 150;
    private int paddingWidth = 20, paddingHeight = 20;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        appModel = new AppModel();
        currentUser = appModel.getLoggedInUser();
        appModel.loadData(currentUser);
        displayHomeContent(anchorDisplay, "Watch Again...", "Trending");
    }


    private ObservableList<Movie> getMovieRecommendations(int movieAmount) {
        ObservableList<Movie> allMoviesToBeSeen = appModel.getObsTopMovieNotSeen();
        ObservableList<Movie> amountMoviesToBeSeen = FXCollections.observableArrayList();

        for (int i = 0; i < movieAmount; i++) {
            amountMoviesToBeSeen.add(allMoviesToBeSeen.get(i));
        }

        return amountMoviesToBeSeen;
    }

    private ObservableList<Movie> getTopMoviesSeen(int movieAmount) {
        ObservableList<Movie> allMoviesToBeSeen = appModel.getObsTopMovieSeen();
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
        ObservableList<Movie> watchAgain = getTopMoviesSeen(4);

        AnchorPane.setLeftAnchor(lblTop, 5.0);
        AnchorPane.setTopAnchor(lblBottom, 225.0);
        AnchorPane.setLeftAnchor(lblBottom, 5.0);

        lblTop.setText(topLabel);
        lblBottom.setText(bottomLabel);

        lblTop.getStyleClass().add("display-text");
        lblBottom.getStyleClass().add("display-text");

        contentContainer.getChildren().add(lblTop);
        displayMovieIcons(contentContainer, 4, 1, true, watchAgain);
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
            Label rating = new Label();
            ImageView moviePicture = new ImageView();


            url = Paths.get(appModel.getMoviePicturePathByID(moviesToDisplay.get(i).getId())).toUri().toString();

            if (url != "")
                moviePicture.setImage(new Image(url, displayElementWidth, displayElementHeight - 10, true, false));

            rating.setText("");
            movieTitle.setText(moviesToDisplay.get(i).getTitle() + ", " + moviesToDisplay.get(i).getYear());
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
            displayElement.setOnMouseClicked(e ->{
                //on click, play movie or something
            });

            contentContainer.getChildren().add(displayElement);
        }

    }

    private void displayMoviesOnly(AnchorPane contentContainer, ObservableList<Movie> movies) {
        if (movies != null) {
            int maxRow = (int) ((movies.size() / 3) + 0.9) > 3 ? 3 : (int) ((movies.size() / 3) + 0.9);

            ObservableList<Movie> rowContent = FXCollections.observableArrayList();

            clearContent(contentContainer);

            for (int i = 0; i < maxRow; i++) {
                for (int j = i * 4; j < i * 4 + 4; j++) {
                    rowContent.add(movies.get(j));
                }
                displayMovieIcons(contentContainer, rowContent.size(), i, false, rowContent);
                rowContent.clear();
            }
        }
    }
    public void btnSearchPressed(ActionEvent actionEvent) {
        displayMoviesOnly(anchorDisplay, appModel.search(txfSearch.getText()));
    }

    public void searchMovies(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            displayMoviesOnly(anchorDisplay, appModel.search(txfSearch.getText()));
        }
    }

    public void logOut(ActionEvent actionEvent) {
        Stage stage = (Stage) anchorControlFrame.getScene().getWindow();
        stage.close();
    }

    public void getTopMoviesSeen(ActionEvent actionEvent) {
        ObservableList<Movie> moviesSeen = appModel.getObsTopMovieSeen();
        displayMoviesOnly(anchorDisplay, moviesSeen);
    }

    public void getNewestMovies(ActionEvent actionEvent) {
        ObservableList<Movie> newestMovies = appModel.getNewestMovies();
        displayMoviesOnly(anchorDisplay, newestMovies);

    }

    public void userMenuClicked(MouseEvent mouseEvent) {
        ctmUserMenu.show(anchorControlFrame, Side.LEFT, btnMenu.getLayoutX(), btnMenu.getLayoutY() + 40);
    }

    public void btnHomePressed(ActionEvent actionEvent) {
        clearContent(anchorDisplay);
        displayHomeContent(anchorDisplay, "Watch Again...", "Trending");
    }

    public void displayAllMovies(ActionEvent actionEvent) {

    }
}
