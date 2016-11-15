package com.example.ahmed.movieapp;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ahmed.movieapp.data.ContainerClass;
import com.example.ahmed.movieapp.data.DatabaseBackgroundTask;
import com.example.ahmed.movieapp.data.GetReviewDB;
import com.example.ahmed.movieapp.data.GetTrailersBackgroundTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    /**
     * this value was added to solve a problem that i was having as i was using
     * the function findviewbyid with getActivity() to get the views in fragment_details
     * here is the solution
     * source i found : http://stackoverflow.com/questions/22428044/nullpointerexception-with-settext-in-a-fragment-android
     */
    View rootView;

    //this ArrayList holds the list of trailers
    ArrayList<Trailer> trailers;

    //this ArrayList holds the list of reviews
    ArrayList<Review> reviews;

    //this movie object holds the movie returns from the intent
    Movies movie;

    final String BASE_URL = "https://www.youtube.com/watch";
    final String Query_param_v ="v";

    public DetailsFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("movieObject",movie);
        outState.putParcelableArrayList("trailersArrayList",trailers);
        outState.putParcelableArrayList("reviewsArrayList",reviews);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle args = getArguments();
        if(args == null){
            return null;
        }

        //retrieving the object from the intent
//        Movies m = (Movies) getActivity().getIntent().getParcelableExtra("movie");
//        if (m == null) {
//            Log.d("log" , "m = null");
//            return null;
//        }

        movie = args.getParcelable("movie");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortMethod = sharedPreferences.getString(getString(R.string.sort_key),getString(R.string.sort_pop));
        if(sortMethod.equals("fav")){
            GetTrailersBackgroundTask trailersTask = new GetTrailersBackgroundTask(getActivity());
            GetReviewDB getReviewDB = new GetReviewDB(getActivity());
            try {
                Log.d("test","i will add movie");
                addMovieToview();
                Log.d("test","movie added");
                ArrayList<Trailer> trailersList = trailersTask.execute(movie.getId()).get();
                if(trailersList!= null){
                    trailers = trailersList;
                    displayTrailersList();
                }
                ArrayList<Review> reviewsList = getReviewDB.execute(movie.getId()).get();
                if(reviewsList!= null){
                    reviews = reviewsList;
                    displayRivewsList();
                }
                if(checkNetwork()){
                    favButtonBehavior();
                }
                else{
                    Button btn = (Button) rootView.findViewById(R.id.fav_button);
                    btn.setVisibility(btn.GONE);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else {
            if (savedInstanceState == null) {
                if (checkNetwork()) {
                    try {
                        getTrailersRivewsList();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //retrieving the views from the layout and add the details from the object to it
                    addMovieToview();
                    displayTrailersList();
                    displayRivewsList();
                    favButtonBehavior();
                } else {
                    Toast.makeText(getActivity(), "there is no network connection", Toast.LENGTH_SHORT).show();
                    addMovieToview();
                    removeWidgets();
                }
            } else {
                movie = savedInstanceState.getParcelable("movieObject");
                trailers = savedInstanceState.getParcelableArrayList("trailersArrayList");
                reviews = savedInstanceState.getParcelableArrayList("reviewsArrayList");
                addMovieToview();
                displayTrailersList();
                displayRivewsList();
                favButtonBehavior();
            }
        }
        return rootView;
    }

    private void removeWidgets() {
        Button btn = (Button) rootView.findViewById(R.id.fav_button);
        btn.setVisibility(btn.GONE);
        TextView text1 = (TextView) rootView.findViewById(R.id.reviews_label);
        text1.setVisibility(text1.GONE);
    }

    private void addMovieToview() {
        //base url to use when creating a uri object to get the images urls
        String Base_URL = "http://image.tmdb.org/t/p/w185";
        //uri that holds the poster url
        Uri uri1 = Uri.parse(Base_URL).buildUpon().appendEncodedPath(movie.getPoster()).build();
        //uri that holds the backdrop url
        Uri uri2 = Uri.parse(Base_URL).buildUpon().appendEncodedPath(movie.getBackdrop()).build();
        //putting those urls to strings
        String posterURL = uri1.toString();
        String backdropURL = uri2.toString();
        //retriving the views from the layout file to update them
        ImageView poster = (ImageView) rootView.findViewById(R.id.poster_details);
        ImageView backdrop = (ImageView) rootView.findViewById(R.id.backdrop_details);
        TextView title = (TextView) rootView.findViewById(R.id.title_details);
        TextView overview = (TextView) rootView.findViewById(R.id.overview_details);
        TextView date = (TextView) rootView.findViewById(R.id.release_date_details);
        TextView vote = (TextView)rootView.findViewById(R.id.votes_details);
        //updating the views
        String movieTitle = movie.getTitle();
        title.setText(movieTitle);
        overview.setText("Overview :\n"+movie.getOverview());
        date.setText("Release Date :\n"+movie.getDate());
        vote.setText("Average Votes :\n"+movie.getVoteAvg()+"/10");
        Picasso.with(getActivity()).load(posterURL).into(poster);
        Picasso.with(getActivity()).load(backdropURL).into(backdrop);
    }

    private void getTrailersRivewsList() throws ExecutionException, InterruptedException {
        TrailersBackgroundTask task1 = new TrailersBackgroundTask();
        trailers = task1.execute(movie.getId()).get();
        ReviewBackgroundTask task2 = new ReviewBackgroundTask();
        reviews = task2.execute(movie.getId()).get();
    }

    private boolean checkNetwork() {
        ConnectivityManager manager = (ConnectivityManager)getActivity().getSystemService(getActivity().getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        return networkinfo != null && networkinfo.isConnected();
    }

    private void favButtonBehavior() {


        final ContainerClass containerClass = new ContainerClass(movie,trailers,reviews);

        final Button btn = (Button) rootView.findViewById(R.id.fav_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseBackgroundTask task = new DatabaseBackgroundTask(getActivity());
                try {
                    if(task.execute(containerClass).get()){
                        Toast.makeText(getActivity(),"added to favorites",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(),"removed from favorites",Toast.LENGTH_LONG).show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void displayRivewsList() {
        View movieReviewRow;
        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.reviews_linear_layout);
        for(int i = 0 ; i < reviews.size();i++){
            movieReviewRow = LayoutInflater.from(getActivity()).inflate(R.layout.review_item,null);
            TextView author = (TextView) movieReviewRow.findViewById(R.id.author_name);
            TextView text = (TextView) movieReviewRow.findViewById(R.id.review_item_id);
            author.setText(reviews.get(i).getAuthor());
            text.setText(reviews.get(i).getContent());
            linear.addView(movieReviewRow);
        }
    }

    private void displayTrailersList() {
        View movieTrailerRow;
        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.trailers_linear_layout);
        for(int i = 0 ; i < trailers.size();i++){

            movieTrailerRow = LayoutInflater.from(getActivity()).inflate(R.layout.trailers_item,null);

            TextView text = (TextView) movieTrailerRow.findViewById(R.id.trailers_item_id);

            text.setText("Trailer "+(i+1));

            final int finalI = i;
            movieTrailerRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //https://developer.android.com/guide/components/intents-common.html#Browser
                    Uri path = Uri.parse(BASE_URL).buildUpon()
                            .appendQueryParameter(Query_param_v,trailers.get(finalI).getKey()).build();
                    Intent intent = new Intent(Intent.ACTION_VIEW, path);
                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(intent);
                    }

                }
            });

            linear.addView(movieTrailerRow);
        }

    }

}
