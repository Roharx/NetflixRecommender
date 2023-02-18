package dk.easv.dataaccess;

import dk.easv.entities.Movie;
import dk.easv.entities.Rating;
import dk.easv.entities.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAccessManager {
    private HashMap<Integer, User> users = new HashMap<>();
    private HashMap<Integer, Movie> movies = new HashMap<>();
    private List<Rating> ratings = new ArrayList<>();

    // Loads all data from disk and stores in memory
    // For performance, data is only updated if updateCacheFromDisk() is called
    public DataAccessManager() {
        updateCacheFromDisk();
    }

    public Map<Integer, User> getAllUsers() {
        return users;
    }

    public Map<Integer, Movie> getAllMovies() {
        return movies;
    }

    public List<Rating> getAllRatings(){
        return ratings;
    }


    public void updateCacheFromDisk(){
        loadAllRatings();
    }
    public String getMoviePicturePathByID(int id){return searchMoviePicturePathByID(id);}

    private void loadAllMovies() {
        try {
            List<String> movieLines = Files.readAllLines(Path.of("MovieRecommendationSystem-CachedImplementation-main/data/movie_titles.txt"));
            for (String movieLine : movieLines) {
                String[] split = movieLine.split(",");
                Movie movie = new Movie(Integer.parseInt(split[0]), split[2], Integer.parseInt(split[1]));
                movies.put(movie.getId(), movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllUsers() {
        try {
            List<String> userLines = Files.readAllLines(Path.of("MovieRecommendationSystem-CachedImplementation-main/data/users.txt"));
            for (String userLine : userLines) {
                String[] split = userLine.split(",");
                User user = new User(Integer.parseInt(split[0]), split[1]);
                users.put(user.getId(), user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads all ratings, users and movies must be loaded first
    // Users holds a list of ratings and movies holds a list of ratings
    private void loadAllRatings() {
        loadAllMovies();
        loadAllUsers();
        try {
            List<String> ratingLines = Files.readAllLines(Path.of("MovieRecommendationSystem-CachedImplementation-main/data/ratings.txt"));
            for (String ratingLine : ratingLines) {
                String[] split = ratingLine.split(",");
                int movieId = Integer.parseInt(split[0]);
                int userId = Integer.parseInt(split[1]);
                int rating = Integer.parseInt(split[2]);
                Rating ratingObj = new Rating(users.get(userId), movies.get(movieId), rating);
                ratings.add(ratingObj);
                users.get(userId).getRatings().add(ratingObj);
                movies.get(movieId).getRatings().add(ratingObj);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String searchMoviePicturePathByID(int id){
        String picturePath = "";

        try {
            List<String> moviesAndPictures = Files.readAllLines(Path.of("MovieRecommendationSystem-CachedImplementation-main/data/movies_pictures.txt"));
            for (String line : moviesAndPictures) {
                String[] lineContent = line.split(",");
                if(id == Integer.parseInt(lineContent[0]))
                    picturePath = lineContent[1];
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return picturePath;
    }
    private List<Movie> getNewestMovies(){
        int maxYear = maxYear();
        List<Movie> newestMovies = new ArrayList<>();
        try {
            List<String> allMovies = Files.readAllLines(Path.of("MovieRecommendationSystem-CachedImplementation-main/data/movie_titles.txt"));
            for (String line : allMovies) {
                String[] lineContent = line.split(",");
                if(Integer.parseInt(lineContent[1]) == maxYear)
                    newestMovies.add(new Movie(Integer.parseInt(lineContent[0]), lineContent[2], Integer.parseInt(lineContent[1])));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return newestMovies;
    }
    private int maxYear(){
        int currentMaxYear = 0;




        return currentMaxYear;
    }

}
