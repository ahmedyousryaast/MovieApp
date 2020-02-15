package com.example.ahmed.movieapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmed.movieapp.Trailer;
import com.example.ahmed.movieapp.data.MyContractClass.TrailerEntry;
import com.example.ahmed.movieapp.data.MyContractClass.MovieWithTrailer;
import com.example.ahmed.movieapp.data.MyContractClass.MovieEntry;

import java.util.ArrayList;

/**
 * Created by Ahmed on 11/13/2016.
 */
public class GetTrailersBackgroundTask extends AsyncTask<String,Void,ArrayList<Trailer>> {
    Context mContext;
    SQLiteDatabase db;

    public GetTrailersBackgroundTask(Context context){
        mContext = context;
    }


    @Override
    protected ArrayList<Trailer> doInBackground(String... strings) {
        MovieDbHelper helper = MovieDbHelper.getInstance(mContext);
        db= helper.getWritableDatabase();


        String movieId = strings[0];
        Log.d("id",movieId);

        String movieIdInDB = getMovieIdInDB(movieId);

        Log.d("movie final id " ,""+ movieIdInDB);

        ArrayList<String>trailerIndexes = new ArrayList<String>();
        trailerIndexes = getTrailersIndexes(movieIdInDB);
        //movie has no trailers
        if(trailerIndexes == null){
            return null;
        }
        else{
            ArrayList<Trailer> result = new ArrayList<>();
            result = getTrailersList(trailerIndexes);
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



    private ArrayList<Trailer> getTrailersList(ArrayList<String> trailerIndexes) {

        ArrayList<Trailer> result = new ArrayList<Trailer>();
        String[] projection = {TrailerEntry.COLUMN_SITE ,TrailerEntry.COLUMN_KEY,TrailerEntry.COLUMN_NAME};
        String selection ="";


        for (int i = 0; i < trailerIndexes.size() - 1; i++) {
            selection += TrailerEntry._ID + " = ? OR ";
        }
        selection += TrailerEntry._ID + " = ? ";

        String [] selectionArgs = new String[trailerIndexes.size()];
        for(int i = 0 ; i < selectionArgs.length ;i++){
            selectionArgs[i] = trailerIndexes.get(i);
        }
        Cursor c = db.query(
                TrailerEntry.TABLE_NAME
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
                String name = c.getString( c.getColumnIndexOrThrow(TrailerEntry.COLUMN_NAME));
                String key = c.getString( c.getColumnIndexOrThrow(TrailerEntry.COLUMN_KEY)) ;
                String site = c.getString( c.getColumnIndexOrThrow(TrailerEntry.COLUMN_SITE)) ;
                result.add(new Trailer(name,key,site));
            } while (c.moveToNext());
        }
        c.close();
        return result;

    }


    private ArrayList<String> getTrailersIndexes(String movieId) {

        Log.d("movie id is ", movieId);
        ArrayList<String> result = new ArrayList<String>();
        String[] projection = {MovieWithTrailer.TRAILER_ID};
        String selection = MovieWithTrailer.MOVIE_ID +" = ? ";
        String[]selectionArgs ={movieId};
        Cursor c = db.query(
                MovieWithTrailer.TABLE_NAME
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
                result.add(c.getString( c.getColumnIndexOrThrow(MovieWithTrailer.TRAILER_ID)));
            } while (c.moveToNext());
        }
        return result;
        }
}
