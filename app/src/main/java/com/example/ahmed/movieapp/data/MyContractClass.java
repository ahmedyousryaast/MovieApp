package com.example.ahmed.movieapp.data;


import android.provider.BaseColumns;

/**
 * Created by Ahmed on 11/11/2016.
 * this class contains the attributes of my database tables
 * defined as constants inside inner classes that represents
 * each table
 */
public class MyContractClass {
    //private constructor to prevent anyone from accidentally instantiating the contract class
    //SOURCE : https://developer.android.com/training/basics/data-storage/databases.html
    private MyContractClass(){}
    //defines constants of trailers table
    public static final class TrailerEntry implements BaseColumns{
        //the table name
        public static final String TABLE_NAME = "trailer";
        //the key of the trailer
        public static final String COLUMN_KEY = "key";
        //name of the trailer
        public static final String COLUMN_NAME = "name";
        //site of the trailer
        public static final String COLUMN_SITE = "site";


    }

    //defines constants of review table
    public static final class ReviewEntry implements BaseColumns{
        //the table name
        public static final String TABLE_NAME = "review";
        //the Author name
        public static final String COLUMN_AUTHOR = "author";
        //the review
        public static final String COLUMN_CONTENT ="content";

    }

    //defines constants of movie table
    public static final class MovieEntry implements BaseColumns{
        //the table name
        public static final String TABLE_NAME = "movie";
        //stores the movie id from api
        public static final String COLUMN_MOVIE_ID="movie_id";
        //the poster of the movie
        public static final String COLUMN_POSTER = "poster";
        //the movie title
        public static final String COLUMN_TITLE = "title";
        //the release date
        public static final String COLUMN_DATE = "release_date";
        //overview of the movie
        public static final String COLUMN_OVERVIEW = "overview";
        //backdrop image
        public static final String COLUMN_BACKDROP = "backdrop";
        //the average vote
        public static final String COLUMN_VOTE = "vote";
    }

    /**
     * the movieWithTrailer class was added to help
     * on storing the movies trailers and movies reviews
     * applying the database normalization forms.
     * the first table "MovieWithTrailer" will help us link
     * trailers to a certain movie.
     * the second table "MovieWithReview" will help us link
     * reviews to a certain movie
     */
    public static final class MovieWithTrailer {
        //the table name
        public static final String TABLE_NAME = "movie_trailer";
        //id of the move
        public static final String MOVIE_ID ="movie_id";
        //id of the move
        public static final String TRAILER_ID ="trailer_id";
    }

    public static final class MovieWithReview{
        //the table name
        public static final String TABLE_NAME = "movie_review";
        //id of the move
        public static final String MOVIE_ID = "movie_id";
        //id of the move
        public static final String REVIEW_ID ="review_id";
    }

}
