package com.example.ahmed.movieapp;

import android.net.Uri;
import android.os.AsyncTask;
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
 * Created by Ahmed on 11/6/2016.
 */
public class TrailersBackgroundTask extends AsyncTask<String,Void,ArrayList<Trailer>> {
    //TAG holds the class name and i use it when i test the app using log statements
    final String TAG = MoviesBackgroundTask.class.getSimpleName();

    public ArrayList<Trailer> parseJSON(String jsonString) throws JSONException{
        //name of json objects to be extracted from the json string
        final String TRAILERS_LIST = "results";
        final String TRAILER_NAME = "name";
        final String TRAILER_KEY = "key";
        final String TRAILER_SITE = "site";
        //arraylist of trailers which will be the result of parsing
        ArrayList<Trailer> trailerArrayList = new ArrayList<Trailer>();
        //getting the main json object
        JSONObject trailersJsonObject = new JSONObject(jsonString);
        //getting the array of movies
        JSONArray resultsJsonArray = trailersJsonObject.getJSONArray(TRAILERS_LIST);

        for(int i = 0 ; i < resultsJsonArray.length() ; i++){
            String name,key,site;

            //get one trailer json object
            JSONObject trailer = resultsJsonArray.getJSONObject(i);
            //get the name of the trailer
            name = trailer.getString(TRAILER_NAME);
            //get the key of the youtube video
            key = trailer.getString(TRAILER_KEY);
            //get the site's name used to privew the video
            site = trailer.getString(TRAILER_SITE);
            trailerArrayList.add(new Trailer(name,key,site));
        }
        return trailerArrayList;
    }

    @Override
    protected ArrayList<Trailer> doInBackground(String... strings) {
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
                .appendPath(strings[0])
                .appendPath("videos")
                .appendQueryParameter(QUERY_API_KEY,BuildConfig.API_KEY)
                .build();
        Log.d("Tag",uri.toString());

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
            //a call to parseJson method which returns arrayList of Trailers
            return parseJSON(jsonString);
        }
        catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        return null;

    }
}
