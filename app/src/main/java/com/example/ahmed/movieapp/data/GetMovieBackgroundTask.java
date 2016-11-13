package com.example.ahmed.movieapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import com.example.ahmed.movieapp.Movies;

import java.util.ArrayList;
import com.example.ahmed.movieapp.data.MyContractClass.MovieEntry;
import com.example.ahmed.movieapp.Movies;

/**
 * Created by Ahmed on 11/12/2016.
 */
public class GetMovieBackgroundTask extends AsyncTask<Void,Void,ArrayList<Movies>>{
    Context mContext;
    SQLiteDatabase db;
    public GetMovieBackgroundTask(Context context){
        mContext = context;
    }
    @Override
    protected ArrayList<Movies> doInBackground(Void... voids) {
        ArrayList<Movies> result = new ArrayList<Movies>();
        MovieDbHelper helper = MovieDbHelper.getInstance(mContext);
        db = helper.getWritableDatabase();
        Cursor c = db.query(
                MovieEntry.TABLE_NAME
                , null
                , null
                , null
                , null
                , null
                , null
        );


        if (!(c.moveToFirst()) || c.getCount() == 0) {
            c.close();
            Log.d("test passed", "working perfectly fine");
            return null;
        } else {
            c.moveToFirst();
            do {
                String title = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_TITLE));
                String poster = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_POSTER));
                String movieID = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_MOVIE_ID));
                String vote = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_VOTE));
                String overView = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_OVERVIEW));
                String releaseDate = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_DATE));
                String backdrop = c.getString(c.getColumnIndexOrThrow(MovieEntry.COLUMN_BACKDROP));
                result.add(new Movies(poster,releaseDate,title,overView,backdrop,vote,movieID));

            } while (c.moveToNext());
            return result;
        }
    }
}
