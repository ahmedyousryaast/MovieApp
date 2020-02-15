package com.example.ahmed.movieapp;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ahmed on 10/10/2016.
 * this class's job is to do all the long term operations
 * which are in this case connecting to API and getting the jason file
 */
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class MoviesBackgroundTask extends AsyncTask<String,Void,ArrayList<Movies>> {
    //TAG holds the class name and i use it when i test the app using log statements
    final String TAG = MoviesBackgroundTask.class.getSimpleName();

    /**
     * this method takes the json string as input and parse it
     * and creates movie objects accordingly and returns an arraylist of movies
     * which will be later used as the datasource for the adapter
     * @param jsonString
     * @return arrayList of movies
     * @throws JSONException
     */
    public ArrayList<Movies> parseJSON(String jsonString) throws JSONException{
        //name of json objects to be extracted from the json string
        final String FAV_LIST = "results";
        final String POSTER_PATH = "poster_path";
        final String RELEASE_DATE = "release_date";
        final String ORIGINAL_TITLE = "original_title";
        final String OVERVIEW = "overview";
        final String BACKDROP_PATH = "backdrop_path";
        final String VOTE_AVERAGE = "vote_average";
        final String MOVIE_ID = "id";
        //arraylist of movies which will be the result of parsing
        ArrayList<Movies> movieArrayList = new ArrayList<Movies>();
        //getting the main json object
        JSONObject moviesJsonObject = new JSONObject(jsonString);
        //getting the array of movies
        JSONArray resultsJsonArray = moviesJsonObject.getJSONArray(FAV_LIST);

        for(int i = 0 ; i < resultsJsonArray.length() ; i++){
            String poster,date,title,overview,backdrop,votes,id;

            //get one move json object
            JSONObject movie = resultsJsonArray.getJSONObject(i);
            //get movie poster path and assigning its value to a string poster
            poster = movie.getString(POSTER_PATH);
            //get movie Release date and assigning its value to a string date
            date = movie.getString(RELEASE_DATE);
            //get movie title and assigning its value to a string title
            title = movie.getString(ORIGINAL_TITLE);
            //get movie overview and assigning its value to a string overview
            overview = movie.getString(OVERVIEW);
            //get movie BackDrop and assigning its value to a string poster
            backdrop = movie.getString(BACKDROP_PATH);
            //get the average rate to present it to the user
            votes = movie.getString(VOTE_AVERAGE);
            //adding the movie id string in order to help us in the second stage
            id = movie.getString(MOVIE_ID);
            //create new movie object and adding it the arraylist
            movieArrayList.add(new Movies(poster,date,title,overview,backdrop,votes,id));
        }
        return movieArrayList;
    }

    /**
     * in order to extend the AsyncTask class the doInBackground
     * method must be overridden and i should add my own logic to it
     * in this case a network connection with the server will be established
     * to get the movies from the server.
     * @param params
     * @return
     */
    @Override
    protected ArrayList<Movies> doInBackground(String... params) {
        //http client used to handle connecting and downloading from server
        HttpURLConnection connection = null;
        //buffered reader is used to read from an input stream
        BufferedReader reader = null;
        //string that will hold json string returned from the server
        String jsonString = null;
        //declaring the parameters used to get the json file
        final String BASE_URL ="https://api.themoviedb.org/3/movie/";
        final String QUERY_API_KEY = "api_key";
        //using URI object to build our url
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(params[0])
                .appendQueryParameter(QUERY_API_KEY,BuildConfig.API_KEY)
                .build();
        try {
            //extracting the url built and put it in a url object
            URL url = new URL(uri.toString());
            //Obtain a new HttpURLConnection
            connection = (HttpURLConnection) url.openConnection();
            //setting the request method to get
            connection.setRequestMethod("GET");
            connection.connect();

            //input stream used to catch the stream of data from the server
            InputStream inputstream = connection.getInputStream();
            //checks if the stream is actually returning data
            if(inputstream == null){
                return null;
            }
            //a string buffer which will be used to save the json string onto it
            StringBuffer buffer = new StringBuffer();
            reader = new BufferedReader(new InputStreamReader(inputstream));

            //adding new line to the json
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            jsonString = buffer.toString();


        } catch (MalformedURLException e) {
            Log.d(TAG,"MalformedURLException");
            return null;
        } catch (IOException e) {
            Log.d(TAG,"IOException");
            return null;
        }
        /**
         * finally block added that will executes
         * anyway even if some error happens while
         * getting the data from the server
         * this block of code ensures that the
         * connection is closes as well as the BufferReader
         */ finally {
            if(connection!= null){
                connection.disconnect();
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.d(TAG,"IOException");
                }
            }
        }

        try {
            //a call to parseJson method which returns arrayList of Movies
            return parseJSON(jsonString);
        }
        catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }
}
