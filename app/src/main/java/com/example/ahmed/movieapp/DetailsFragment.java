package com.example.ahmed.movieapp;


import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    //this arraylist holds the list of trailers
    ArrayList<Trailer> trailers;

    //this arraylist holds the list of reviews
    ArrayList<Review> reviews;

    //this movie object holds the movie returen from the intent
    Movies movie;

    final String BASE_URL = "https://www.youtube.com/watch";
    final String Query_param_v ="v";

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_details, container, false);
        //retrieving the object from the intent
        movie = (Movies) getActivity().getIntent().getParcelableExtra("movie");


        if(checkNetwork()) {
            try {
                getTrailersRivewsList();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //retrieving the views from the layout and add the details from the object to it
            styling();
        }
        else{
            Toast.makeText(getActivity(),"there is no network connection",Toast.LENGTH_SHORT).show();
        }

        return rootView;
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

    private void styling() {

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
        String movieTitle = "Title :"+movie.getTitle();
        title.setText(movieTitle);
        overview.setText("Overview :\n"+movie.getOverview());
        date.setText("Release Date :\n"+movie.getDate());
        vote.setText("Average Votes :\n"+movie.getVoteAvg());
        Picasso.with(getActivity()).load(posterURL).into(poster);
        Picasso.with(getActivity()).load(backdropURL).into(backdrop);
        displayTrailersList();
        displayRivewsList();

    }

    private void displayRivewsList() {
        View movieReviewRow;
        LinearLayout linear = (LinearLayout) rootView.findViewById(R.id.reviews_linear_layout);
        for(int i = 0 ; i < reviews.size();i++){
            movieReviewRow = LayoutInflater.from(getActivity()).inflate(R.layout.review_item,null);
            TextView text = (TextView) movieReviewRow.findViewById(R.id.review_item_id);
            text.setText(reviews.get(i).getContent());
            linear.addView(text);
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
            text.setOnClickListener(new View.OnClickListener() {
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
            linear.addView(text);
        }

    }

}
