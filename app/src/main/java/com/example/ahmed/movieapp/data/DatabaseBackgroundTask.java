package com.example.ahmed.movieapp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import com.example.ahmed.movieapp.data.MyContractClass.MovieEntry;
import com.example.ahmed.movieapp.data.MyContractClass.TrailerEntry;
import com.example.ahmed.movieapp.data.MyContractClass.ReviewEntry;
import com.example.ahmed.movieapp.data.MyContractClass.MovieWithReview;
import com.example.ahmed.movieapp.data.MyContractClass.MovieWithTrailer;


import java.util.ArrayList;

/**
 * Created by Ahmed on 11/11/2016.
 */
public class DatabaseBackgroundTask extends AsyncTask<ContainerClass,Void,Boolean>{
    private Context mContext;
    SQLiteDatabase db;
    //constructor that takes context to helpin creating database helper object
    public DatabaseBackgroundTask(Context context){
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(ContainerClass... containerClasses) {
        //taking a container class that was passes as parameter and create a container class
        //that will have the same values but just to make the code readable
        ContainerClass container = containerClasses[0];
        //new helper object
        MovieDbHelper helper = MovieDbHelper.getInstance(mContext);
        db= helper.getWritableDatabase();

        //check if the movie was already marked as favourite
        if(isInserted(container.getMovie().getId())){
            removeFromFav(container);
            return false;

        }else {
            long newRowId = insertMovieIntoDataBase(container);
            ArrayList<Long> trailersIndexes = insertTrailersIntoDataBase(container);
            ArrayList<Long> reviewsIndexes = insertReviewsIntoDataBase(container);


            /**
             * inserting data into movie with trailers table
             */
            for (int i = 0; i < trailersIndexes.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieWithTrailer.MOVIE_ID, newRowId);
                contentValues.put(MovieWithTrailer.TRAILER_ID, trailersIndexes.get(i));
                db.insert(MovieWithTrailer.TABLE_NAME, null, contentValues);
            }

            /**
             * inserting data into movie with reviews table
             */
            for (int i = 0; i < reviewsIndexes.size(); i++) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(MovieWithReview.MOVIE_ID, newRowId);
                contentValues.put(MovieWithReview.REVIEW_ID, reviewsIndexes.get(i));

                db.insert(MovieWithReview.TABLE_NAME, null, contentValues);
            }
            return true;
        }
    }

    private ArrayList<Long> insertReviewsIntoDataBase(ContainerClass container) {
        ArrayList<Long> reviewsIndexes = new ArrayList<Long>();
        for (int i = 0; i < container.getReviews().size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(ReviewEntry.COLUMN_AUTHOR, container.getReviews().get(i).getAuthor());
            contentValues.put(ReviewEntry.COLUMN_CONTENT, container.getReviews().get(i).getContent());
            reviewsIndexes.add(db.insert(ReviewEntry.TABLE_NAME, null, contentValues));
        }
        return reviewsIndexes;
    }

    private ArrayList<Long> insertTrailersIntoDataBase(ContainerClass container) {
        ArrayList<Long> trailersIndexes = new ArrayList<Long>();
        for (int i = 0; i < container.getTrailers().size(); i++) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(TrailerEntry.COLUMN_KEY, container.getTrailers().get(i).getKey());
            contentValues.put(TrailerEntry.COLUMN_NAME, container.getTrailers().get(i).getName());
            contentValues.put(TrailerEntry.COLUMN_SITE, container.getTrailers().get(i).getSite());
            trailersIndexes.add(db.insert(TrailerEntry.TABLE_NAME, null, contentValues));
        }
        return trailersIndexes;
    }

    private long insertMovieIntoDataBase(ContainerClass container) {
        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_MOVIE_ID, container.getMovie().getId());
        cv.put(MovieEntry.COLUMN_POSTER, container.getMovie().getPoster());
        cv.put(MovieEntry.COLUMN_TITLE, container.getMovie().getTitle());
        cv.put(MovieEntry.COLUMN_DATE, container.getMovie().getDate());
        cv.put(MovieEntry.COLUMN_OVERVIEW, container.getMovie().getOverview());
        cv.put(MovieEntry.COLUMN_BACKDROP, container.getMovie().getBackdrop());
        cv.put(MovieEntry.COLUMN_VOTE, container.getMovie().getVoteAvg());

        return db.insert(MovieEntry.TABLE_NAME, null, cv);
    }

    private void removeFromFav(ContainerClass containerClass) {
        //movie id from the database
        long movieId = getMovieIdInDB(containerClass);
        Log.d("my testing passed "," the id of the movie is "+movieId);
        ArrayList<Long>trailersIndexes = getTrailersID(containerClass);

        ArrayList<Long>reviewsIndexes = getReviewsID(containerClass);

        if(reviewsIndexes!=null){
            removeMovieWithReviews(movieId);
            removeReviews(reviewsIndexes);
        }
        if(trailersIndexes != null){
            removeMovieWithTrailer(movieId);
            removeTrailers(trailersIndexes);
        }
        removeMovie(movieId);

//        Cursor myC = db.query(
//                MovieWithTrailer.TABLE_NAME,
//                null,
//                null,
//                null,
//                null,
//                null,
//                null
//        );
//        if(!(myC.moveToFirst()) || myC.getCount() ==0){
//            Log.d("emty","empty");
//        }
//        else {
//            myC.moveToFirst();
//            do{
//                String movieIDD = myC.getString( myC.getColumnIndexOrThrow(MovieWithTrailer.MOVIE_ID));
//                Log.d("author" ,""+movieIDD);
//            }while (myC.moveToNext());
//        }


    }

    private void removeMovie(long movieId) {
        db.delete(MovieEntry.TABLE_NAME,MovieEntry._ID + " = " +movieId,null);
    }

    private void removeTrailers(ArrayList<Long> trailersIndexes) {
        for (int i = 0; i < trailersIndexes.size();i++){
            db.delete(TrailerEntry.TABLE_NAME,TrailerEntry._ID + " = " +trailersIndexes.get(i),null);
        }
    }

    private void removeReviews(ArrayList<Long> reviewsIndexes) {
        for (int i = 0; i < reviewsIndexes.size();i++){
            db.delete(ReviewEntry.TABLE_NAME, ReviewEntry._ID + " = "+ reviewsIndexes.get(i),null);
        }
    }

    private void removeMovieWithReviews(long movieId) {
        db.delete(MovieWithReview.TABLE_NAME,MovieWithReview.MOVIE_ID + " = "+movieId,null);
    }

    private void removeMovieWithTrailer(long movieId) {
        db.delete(MovieWithTrailer.TABLE_NAME,MovieWithTrailer.MOVIE_ID +" = "+movieId,null);
    }

    private ArrayList<Long> getReviewsID(ContainerClass containerClass) {
        ArrayList<Long> results = new ArrayList<Long>();
        String selection = "";

        if(containerClass.getReviews().size() == 0) {
            return null;
        }
        else {
            for (int i = 0; i < containerClass.getReviews().size() - 1; i++) {
                selection += ReviewEntry.COLUMN_AUTHOR + " = ? OR ";
            }
            selection += ReviewEntry.COLUMN_AUTHOR + " = ?";


            String[] selectionArgs = new String[containerClass.getReviews().size()];
            for (int i = 0; i < selectionArgs.length; i++) {
                selectionArgs[i] = containerClass.getReviews().get(i).getAuthor();
            }
            String[] projection = {ReviewEntry._ID};
            Cursor c = db.query(
                    ReviewEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            c.moveToFirst();
            do {
                results.add(c.getLong(c.getColumnIndexOrThrow(ReviewEntry._ID)));
            } while (c.moveToNext());
            return results;

        }
    }

    /**
     * this method query the trailers table and returns all the trailer indexes
     * @param containerClass
     * @return
     */
    private ArrayList<Long> getTrailersID(ContainerClass containerClass) {
        ArrayList<Long> results = new ArrayList<Long>();
        String selection = "";
        if(containerClass.getTrailers().size() == 0) {
            return null;
        }
        else {
            for (int i = 0; i < containerClass.getTrailers().size() - 1; i++) {
                selection += TrailerEntry.COLUMN_KEY + " = ? OR ";
            }
            selection += TrailerEntry.COLUMN_KEY + " = ?";
            Log.d("iterator test ", selection);

//        String selection = TrailerEntry.COLUMN_KEY + " = ?";

            String[] selectionArgs = new String[containerClass.getTrailers().size()];
            Log.d("here0", "here0");
            for (int i = 0; i < selectionArgs.length; i++) {
                selectionArgs[i] = containerClass.getTrailers().get(i).getKey();
                Log.d("selection arg is ", selectionArgs[i]);
            }
            String[] projection = {TrailerEntry._ID};
            Cursor c = db.query(
                    TrailerEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
            c.moveToFirst();
            do {
                results.add(c.getLong(c.getColumnIndexOrThrow(TrailerEntry._ID)));
            } while (c.moveToNext());
            return results;
        }
    }

    /**
     * this methods job is to return the movie id from the dataase
     * @param containerClass
     * @return
     */
    private long getMovieIdInDB(ContainerClass containerClass) {
        //getting the movie id
        String selection = MovieEntry.COLUMN_MOVIE_ID + " = ?";

        String[] projection = {MovieEntry._ID};

        String[] selectionArgs ={ containerClass.getMovie().getId() };

        Cursor c = db.query(
                MovieEntry.TABLE_NAME, //table of the query
                projection,//all columns
                selection,//columns for where clause
                selectionArgs, //values for the where clause
                null, //no grouping
                null, // no filter
                null //no sorting order
        );

        c.moveToFirst();
        return c.getLong(c.getColumnIndexOrThrow(MovieEntry._ID));
    }

    //check if movie already exists
    private boolean isInserted(String id) {

        //source : https://developer.android.com/training/basics/data-storage/databases.html#DefineContract
        String selection = MovieEntry.COLUMN_MOVIE_ID + " = ?";

        String[] selectionArgs = { id };

        Cursor c = db.query(
                MovieEntry.TABLE_NAME, //table of the query
                null,//all columns
                selection,//columns for where clause
                selectionArgs, //values for the where clause
                null, //no grouping
                null, // no filter
                null //no sorting order
        );

        if(c.getCount() <= 0){
            c.close();
            return false;
        }
        c.close();
        return true;
    }
}
