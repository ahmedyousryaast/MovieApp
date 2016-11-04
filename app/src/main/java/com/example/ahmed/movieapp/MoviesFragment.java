package com.example.ahmed.movieapp;


import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {
    //arrayList to hold the list of movies
    ArrayList<Movies> movies;

    //constructor to create fragments
    public MoviesFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();
        /**
         * while testing the app , the app used to crash when there is
         * no available network , so a network check runs first before
         * running the AsyncTask to get the data from the server
         */
        if(checkNetwork()) {
            displayGrid();
        }
        else{
            Toast.makeText(getActivity(),"there is no network connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void displayGrid() {
        MoviesBackgroundTask task = new MoviesBackgroundTask();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortMethod = sharedPreferences.getString(getString(R.string.sort_key),getString(R.string.sort_pop));
        try {
            movies = task.execute(sortMethod).get();
            MovieAdapter adapter = new MovieAdapter(getActivity(),R.layout.grid_item_poster,R.id.grid_item_id,movies);
            GridView grid = (GridView) getActivity().findViewById(R.id.grid_view_id);
            grid.setAdapter(adapter);

            grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //showDetails(position);
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * A method that check for an available network connection
     * to a avoid the exception thrown by the app when testing it
     * without an available network connection.
     * @return boolean that indicates the state of network
     */
    private boolean checkNetwork() {
        ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(getActivity().getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isConnected();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movies, container, false);
    }

}
