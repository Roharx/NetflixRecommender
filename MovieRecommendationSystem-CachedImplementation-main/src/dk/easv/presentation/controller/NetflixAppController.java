package dk.easv.presentation.controller;

import dk.easv.entities.Movie;
import dk.easv.entities.User;
import dk.easv.presentation.model.AppModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
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

    private int displayMovieAmount;
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
        displayMovieAmount = 40;
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
        //TODO edit to vertical scrollPane
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

    /**
     * Generates a row of movie icons, calculates the distance needed based on a preset.
     *
     * @param contentContainer AnchorPane where the icons should be displayed.
     * @param amount           The amount of icons that needs to be displayed in the row.
     * @param row              The row's number it needs to generate. (e.g.: 1 for the first row)
     * @param hasLabel         True if the display has label, it leaves extra vertical spacing for the icons.
     * @param moviesToDisplay  The movies that need the icons to be generated for.
     */
    private void displayMovieIcons(AnchorPane contentContainer, int amount, int row, boolean hasLabel, ObservableList<Movie> moviesToDisplay) {
        String url;
        double xSpacing;
        double ySpacing;
        for (int i = 0; i < amount; i++) {
            xSpacing = 20.0 + i * 290;
            if (hasLabel) {
                ySpacing = 50.0 + ((row - 1) * 250);
            } else {
                ySpacing = 180.0 + ((row - 1) * 175);
            }

            StackPane displayElement = new StackPane();
            Label movieTitle = new Label();
            ImageView moviePicture = new ImageView();


            url = Paths.get(appModel.getMoviePicturePathByID(moviesToDisplay.get(i).getId())).toUri().toString();

            if (url != "")
                moviePicture.setImage(new Image(url, displayElementWidth, displayElementHeight - 10, true, false));

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
            displayElement.setOnMouseClicked(e -> {
                clearContent(contentContainer);
                Label temp = new Label();
                String selectedTitle = displayElement.getChildren().get(1).toString();//2nd property of the displayElement, it is the movie title

                selectedTitle = selectedTitle.substring('\'' + 6, selectedTitle.length() - 1);//java is strange, so I had to add a +6 to the beginning

                temp.setText("Playing Movie: " + selectedTitle);

                temp.setStyle("-fx-padding: 20px;\n" +
                        "    -fx-text-fill: white;\n" +
                        "    -fx-font-size: 30px;\n" +
                        "    -fx-font-weight: bold;");

                contentContainer.getChildren().add(temp);

            });
            contentContainer.getChildren().add(displayElement);
            contentContainer.setPadding(new Insets(20,20,30,20));
        }

    }

    /**
     * Displays movies in bulk on a scroll pane, without size constraints
     *
     * @param scrollPane The scrollPane that needs to contain the movies.
     * @param movies     The movies that need to be displayed inside the ScrollPane.
     */
    private void displayManyMovies(ScrollPane scrollPane, ObservableList<Movie> movies) {

        int maxRow = (int) ((movies.size() / 3) + 0.9);
        ObservableList<Movie> rowContent = FXCollections.observableArrayList();

        AnchorPane contentContainer = new AnchorPane();

        for (int i = 0; i < maxRow; i++) {
            for (int j = i * 4; j < i * 4 + 4; j++) {
                if (j < movies.size())
                    rowContent.add(movies.get(j));

            }
            displayMovieIcons(contentContainer, rowContent.size(), i, false, rowContent);
            rowContent.clear();
        }
        scrollPane.setContent(contentContainer);

    }

    public void btnSearchPressed(ActionEvent actionEvent) {
        displayMovieAmount = 40;
        addScrollPane(anchorDisplay, appModel.search(txfSearch.getText()));
    }

    public void searchMovies(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            addScrollPane(anchorDisplay, appModel.search(txfSearch.getText()));
        }
    }

    public void logOut(ActionEvent actionEvent) {
        Stage stage = (Stage) anchorControlFrame.getScene().getWindow();
        stage.close();
    }

    public void getTopMoviesSeen(ActionEvent actionEvent) {
        displayMovieAmount = 40;
        ObservableList<Movie> moviesSeen = appModel.getObsTopMovieSeen();
        addScrollPane(anchorDisplay, moviesSeen);
    }

    public void getNewestMovies(ActionEvent actionEvent) {
        displayMovieAmount = 40;
        ObservableList<Movie> newestMovies = appModel.getNewestMovies();
        addScrollPane(anchorDisplay, newestMovies);
    }

    public void userMenuClicked(MouseEvent mouseEvent) {
        ctmUserMenu.show(anchorControlFrame, Side.LEFT, btnMenu.getLayoutX(), btnMenu.getLayoutY() + 40);
    }

    public void btnHomePressed(ActionEvent actionEvent) {
        displayMovieAmount = 40;
        clearContent(anchorDisplay);
        displayHomeContent(anchorDisplay, "Watch Again...", "Trending");
    }

    public void displayAllMovies(ActionEvent actionEvent) {
        displayAllMoviesScrollPane();
    }

    private void displayAllMoviesScrollPane() {
        clearContent(anchorDisplay);
        ObservableList<Movie> allMovies = appModel.getAllMovies();
        addScrollPane(anchorDisplay, allMovies);
    }

    private void addScrollPane(AnchorPane anchorPane, ObservableList<Movie> displayMovies) {
        clearContent(anchorPane);
        ScrollPane scrollPane = new ScrollPane();
        Button btnShowMore = new Button();

        btnShowMore.setText("Show More");

        scrollPane.setPrefWidth(anchorPane.getWidth());
        scrollPane.setPrefHeight(anchorPane.getHeight());
        btnShowMore.getStyleClass().add("show-more-button");
        AnchorPane.setTopAnchor(btnShowMore, anchorPane.getHeight() - 70);
        AnchorPane.setLeftAnchor(btnShowMore, (anchorPane.getWidth() / 2) - 100);

        anchorPane.getChildren().add(scrollPane);
        anchorPane.getChildren().add(btnShowMore);
        btnShowMore.setDisable(true);
        btnShowMore.setVisible(false);

        int moviesToDisplayAmount = displayMovies.size();

        scrollPane.setOnScroll(e -> {
            if (displayMovieAmount < moviesToDisplayAmount)
                if (scrollPane.getVvalue() == 1.0) {
                    btnShowMore.setDisable(false);
                    btnShowMore.setVisible(true);
                    scrollPane.setPrefHeight(anchorPane.getHeight() - 60);
                } else {
                    btnShowMore.setDisable(true);
                    btnShowMore.setVisible(false);
                }

        });

        btnShowMore.setOnAction(event -> {
            displayMovieAmount = displayMovieAmount + 40 > displayMovies.size() ? displayMovies.size() : displayMovieAmount + 40;
            addScrollPane(anchorPane, displayMovies);
        });

        ObservableList<Movie> test = FXCollections.observableArrayList();
        for (int i = 0; i < displayMovieAmount; i++)
            test.add(displayMovies.get(i));

        displayManyMovies(scrollPane, test);

    }
}
