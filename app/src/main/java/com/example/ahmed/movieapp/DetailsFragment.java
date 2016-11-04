package com.example.ahmed.movieapp;


import android.graphics.Movie;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


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

    public DetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_details, container, false);
        //retrieving the object from the intent
        Movies movie = (Movies) getActivity().getIntent().getSerializableExtra("movie");
        //retrieving the views from the layout and add the details from the object to it
        styling(movie);
        // Inflate the layout for this fragment
        return rootView;
    }

    private void styling(Movies movie) {

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

    }

}
