package com.example.ahmed.movieapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.ahmed.movieapp.data.GetMovieBackgroundTask;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 *
 * reminder : add comments to this class
 */
public class MoviesFragment extends Fragment {
    View rootView;
    //arrayList to hold the list of movies
    ArrayList<Movies> movies;

    //constructor to create fragments
    public MoviesFragment() {
    }

    /**
     * i override this fragment lifecycle method
     * in order to avoid losing movies data in case of
     * screen rotation and to enhance my app performance
     * and avoid making more network calls
     * @param outState
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("OnSaveInstance called"," check");
        if(movies != null){
            outState.putParcelableArrayList("moviesArrayList", movies);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        displayGrid();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d("onCreate"," i am here");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("onCreateView","here");
        rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        if(savedInstanceState == null || ! savedInstanceState.containsKey("moviesArrayList")){
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

        }else {
            movies = savedInstanceState.getParcelableArrayList("moviesArrayList");
            createComponents();

        }
        return rootView;
    }

    private void createComponents() {
        Log.d("createCoponentsCalled"," check");
        MovieAdapter adapter = new MovieAdapter(getActivity(),R.layout.grid_item_poster,R.id.grid_item_id,movies);
        GridView grid = (GridView) rootView.findViewById(R.id.grid_view_id);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetails(position);
            }
        });
    }

    /**
     * this method's job is to display the movie posters.
     *
     */
    private void displayGrid() {
        Log.d("test ","ondisplaygrid");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortMethod = sharedPreferences.getString(getString(R.string.sort_key),getString(R.string.sort_pop));
        if(sortMethod.equals("fav")){
            GetMovieBackgroundTask task = new GetMovieBackgroundTask(getActivity());
            try {
                movies = task.execute().get();
                if(movies == null){
                    Toast.makeText(getActivity(),"Your Favorites are empty",Toast.LENGTH_LONG).show();
                    GridView gridView = (GridView) rootView.findViewById(R.id.grid_view_id);
                    MovieAdapter adapter = (MovieAdapter) gridView.getAdapter();
                    if(adapter != null){
                        adapter.clear();
                    }
                }else{
                    createComponents();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                if(checkNetwork()) {
                    MoviesBackgroundTask task = new MoviesBackgroundTask();
                    movies = task.execute(sortMethod).get();
                    createComponents();
                }
                else {
                    Toast.makeText(getActivity(),"there is no network connection",Toast.LENGTH_SHORT).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    private void showDetails(int position) {
//        Intent intent = new Intent(getActivity(),DetailsActivity.class);
//        intent.putExtra("movie",movies.get(position));
//        startActivity(intent);
        ((Callback)getActivity()).onItemSelected(movies.get(position));

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

    public interface Callback{
        void onItemSelected(Movies movie);
    }
}
