package dk.easv.presentation.model;

import dk.easv.entities.*;
import dk.easv.logic.LogicManager;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.util.List;

public class AppModel {

    LogicManager logic = new LogicManager();
    // Models of the data in the view
    private final ObservableList<User>  obsUsers = FXCollections.observableArrayList();
    private final ObservableList<Movie> obsTopMovieSeen = FXCollections.observableArrayList();
    private final ObservableList<Movie> obsTopMovieNotSeen = FXCollections.observableArrayList();
    private final ObservableList<UserSimilarity>  obsSimilarUsers = FXCollections.observableArrayList();
    private final ObservableList<TopMovie> obsTopMoviesSimilarUsers = FXCollections.observableArrayList();

    private final SimpleObjectProperty<User> obsLoggedInUser = new SimpleObjectProperty<>();
    private static User loggedInUser;

    public static void setLoggedInUser(User loggedInUser) {
        AppModel.loggedInUser = loggedInUser;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    private ObservableList<Movie> movies ;
    private ObservableList<Movie> getMovies() throws SQLException {
        List<Movie> m = logic.getAllMovies();
        return movies = FXCollections.observableArrayList(m);
    }
    public void search(String query) throws SQLException {
        movies.clear();
        movies.addAll(logic.searchMovies(query));
    }


    public void loadUsers(){
        obsUsers.clear();
        obsUsers.addAll(logic.getAllUsers());
    }

    public void loadData(User user) {
        obsTopMovieSeen.clear();
        obsTopMovieSeen.addAll(logic.getTopAverageRatedMovies(user));

        obsTopMovieNotSeen.clear();
        obsTopMovieNotSeen.addAll(logic.getTopAverageRatedMoviesUserDidNotSee(user));

        obsSimilarUsers.clear();
        obsSimilarUsers.addAll(logic.getTopSimilarUsers(user));

        obsTopMoviesSimilarUsers.clear();
        obsTopMoviesSimilarUsers.addAll(logic.getTopMoviesFromSimilarPeople(user));
    }

    public ObservableList<User> getObsUsers() {
        return obsUsers;
    }

    public ObservableList<Movie> getObsTopMovieSeen() {
        return obsTopMovieSeen;
    }

    public ObservableList<Movie> getObsTopMovieNotSeen() {
        return obsTopMovieNotSeen;
    }

    public ObservableList<UserSimilarity> getObsSimilarUsers() {
        return obsSimilarUsers;
    }

    public ObservableList<TopMovie> getObsTopMoviesSimilarUsers() {
        return obsTopMoviesSimilarUsers;
    }

    public User getObsLoggedInUser() {
        return obsLoggedInUser.get();
    }//TODO irrelevant method, to be deleted

    public SimpleObjectProperty<User> obsLoggedInUserProperty() {
        return obsLoggedInUser;
    }//TODO irrelevant method, to be deleted

    public void setObsLoggedInUser(User obsLoggedInUser) {
        this.obsLoggedInUser.set(obsLoggedInUser);
    }//TODO irrelevant method, to be deleted

    public boolean loginUserFromUsername(String userName) {
        User u = logic.getUser(userName);
        obsLoggedInUser.set(u);
        if (u==null)
            return false;
        else
            return true;
    }
    public User getUserByUsername(String username){
        return logic.getUser(username);
    }

    public String getMoviePicturePathByID(int id){
        return logic.getMoviePicturePathByID(id);
    }
}
