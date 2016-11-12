package com.example.ahmed.movieapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.ahmed.movieapp.data.MyContractClass.MovieEntry;
import com.example.ahmed.movieapp.data.MyContractClass.TrailerEntry;
import com.example.ahmed.movieapp.data.MyContractClass.ReviewEntry;
import com.example.ahmed.movieapp.data.MyContractClass.MovieWithTrailer;
import com.example.ahmed.movieapp.data.MyContractClass.MovieWithReview;

/**
 * Created by Ahmed on 11/11/2016.
 * this is my DB helper class
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    //data base version
    private static final int DATABASE_VERSION = 1;
    //the database file name
    static final String DATABASE_NAME = "movies.db";

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //statement to create movie table
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME +" ("+
                MovieEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_BACKDROP + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_VOTE+ " TEXT NOT NULL "
                +");";

        //statement to create trailer table
        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " ("+
                TrailerEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                TrailerEntry.COLUMN_SITE + " TEXT NOT NULL " + ");";

        //statement to create review table
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE "+ ReviewEntry.TABLE_NAME + " ("+
                ReviewEntry._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL " + ");";

        //create table that links movies with trailers
        final String SQL_CREATE_MOVIE_TRAILER= "CREATE TABLE "+ MovieWithTrailer.TABLE_NAME + " ("+
                MovieWithTrailer.MOVIE_ID + " INTEGER NOT NULL, "+
                MovieWithTrailer.TRAILER_ID + " INTEGER NOT NULL ,"+
                " FOREIGN KEY (" +MovieWithTrailer.MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME  + " (" + MovieEntry._ID + "), " +
                " FOREIGN KEY (" +MovieWithTrailer.TRAILER_ID + ") REFERENCES " +
                TrailerEntry.TABLE_NAME  + " (" + TrailerEntry._ID + "), " +
                " PRIMARY KEY ("+MovieWithTrailer.MOVIE_ID +" , "+MovieWithTrailer.TRAILER_ID+" ) ); ";

        //create table that links movies with reviews
        final String SQL_CREATE_MOVIE_REVIEW = "CREATE TABLE "+ MovieWithReview.TABLE_NAME + " ("+
                MovieWithReview.MOVIE_ID + " INTEGER NOT NULL, "+
                MovieWithReview.REVIEW_ID + " INTEGER NOT NULL ,"+
                " FOREIGN KEY (" +MovieWithReview.MOVIE_ID + ") REFERENCES " +
                MovieEntry.TABLE_NAME  + " (" + MovieEntry._ID + "), " +
                " FOREIGN KEY (" +MovieWithReview.REVIEW_ID + ") REFERENCES " +
                ReviewEntry.TABLE_NAME  + " (" + ReviewEntry._ID + "), " +
                " PRIMARY KEY ("+MovieWithReview.MOVIE_ID +" , "+MovieWithReview.REVIEW_ID+" ) ); ";
        ;


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TRAILER);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_REVIEW);
    }

    //this method gets called in case the database exists but different version is passed to the constructor
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieWithReview.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieWithTrailer.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}