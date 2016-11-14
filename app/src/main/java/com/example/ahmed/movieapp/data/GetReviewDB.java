package com.example.ahmed.movieapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmed.movieapp.Review;
import com.example.ahmed.movieapp.data.MyContractClass.ReviewEntry;
import com.example.ahmed.movieapp.data.MyContractClass.MovieWithReview;
import com.example.ahmed.movieapp.data.MyContractClass.MovieEntry;

import java.util.ArrayList;

/**
 * Created by Ahmed on 11/14/2016.
 */
public class GetReviewDB extends AsyncTask<String,Void,ArrayList<Review>>{
    Context mContext;
    SQLiteDatabase db;

    public GetReviewDB(Context context){
        mContext = context;
    }


    @Override
    protected ArrayList<Review> doInBackground(String... strings) {
        MovieDbHelper helper = MovieDbHelper.getInstance(mContext);
        db= helper.getWritableDatabase();
        String movieId = strings[0];
        String movieIdInDB = getMovieIdInDB(movieId);
        ArrayList<String>reviewIndexes = new ArrayList<String>();
        reviewIndexes = getReviewIndexes(movieIdInDB);

        //movie has no reviews
        if(reviewIndexes == null){
            Log.d("didnt crash","didn't crash");
            return null;
        }
        else{
            ArrayList<Review> result = new ArrayList<>();
            result = getReviewsList(reviewIndexes);
            return result;

        }
    }
    //getting the movie id from the database
    private String getMovieIdInDB(String movieId) {
        String[] projection = {MovieEntry._ID};
        String selection = MovieEntry.COLUMN_MOVIE_ID + " = ? ";
        String[] selectionArgs = {movieId};

        Cursor c = db.query(
                MovieEntry.TABLE_NAME
                ,projection
                ,selection
                ,selectionArgs
                ,null
                ,null
                ,null
        );
        c.moveToFirst();
        String id = c.getString( c.getColumnIndexOrThrow(MovieEntry._ID));
        return id;
    }



    private ArrayList<Review> getReviewsList(ArrayList<String> reviewIndexes) {

        ArrayList<Review> result = new ArrayList<Review>();
        String[] projection = {ReviewEntry.COLUMN_CONTENT , ReviewEntry.COLUMN_AUTHOR};
        String selection ="";


        for (int i = 0; i < reviewIndexes.size() - 1; i++) {
            selection += ReviewEntry._ID + " = ? OR ";
        }
        selection += ReviewEntry._ID + " = ? ";

        String [] selectionArgs = new String[reviewIndexes.size()];
        for(int i = 0 ; i < selectionArgs.length ;i++){
            selectionArgs[i] = reviewIndexes.get(i);
        }
        Cursor c = db.query(
                ReviewEntry.TABLE_NAME
                ,projection
                ,selection
                ,selectionArgs
                ,null
                ,null
                ,null
        );
        if (!(c.moveToFirst()) || c.getCount() == 0) {
            c.close();
            Log.d("test", "No trailers for the movie");
            return null;
        } else {
            c.moveToFirst();
            do {
                String author = c.getString( c.getColumnIndexOrThrow(ReviewEntry.COLUMN_AUTHOR));
                String content = c.getString( c.getColumnIndexOrThrow(ReviewEntry.COLUMN_CONTENT)) ;
                result.add(new Review(author,content));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }


    private ArrayList<String> getReviewIndexes(String movieId) {

        Log.d("movie id is ", movieId);
        ArrayList<String> result = new ArrayList<String>();
        String[] projection = {MovieWithReview.REVIEW_ID};
        String selection = MovieWithReview.MOVIE_ID +" = ? ";
        String[]selectionArgs ={movieId};
        Cursor c = db.query(
                MovieWithReview.TABLE_NAME
                ,projection
                ,selection
                ,selectionArgs
                ,null
                ,null
                ,null
        );

        if (!(c.moveToFirst()) || c.getCount() == 0) {
            c.close();
            Log.d("test", "No trailers for the movie");
            return null;
        } else {
            c.moveToFirst();
            do {
                result.add(c.getString( c.getColumnIndexOrThrow(MovieWithReview.REVIEW_ID)));
            } while (c.moveToNext());
        }
        return result;
    }
}
