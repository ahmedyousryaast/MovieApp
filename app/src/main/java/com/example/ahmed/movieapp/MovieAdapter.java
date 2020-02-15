package com.example.ahmed.movieapp;



import android.content.Context;
import android.graphics.Movie;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import java.util.List;

/**
 * this class will work as my custom adapter the view the posters
 * returned from the server into the main grid
 */
public class MovieAdapter extends ArrayAdapter<Movies> {
    /**
     * public constructor that takes the context and the layout file of the ImageView and the id of the image view
     * and an array list of images to work on
     */
    public MovieAdapter(Context context, int resource, int textViewResourceId, List<Movies> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    /**
     * in order to write a custom adapter , the getView method must be overridden.
     * in order make the adapter work on a specific type of views because
     * arrayAdapters operates on textViews by default.
     * inside the method we retrieve a certain movie object
     * then retrieve the ImageView by id and set it to its resource
     * using the picasso.
     * @param position
     * @param convertView
     * @param parent
     * @return convertView
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        /**
         * checks if the view that the adapter got is null which means that
         * this view hasn't been inflated yet so it will be inflated first
         */
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_poster, parent, false);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        /**
         * this is the base url that we will use and append to it the Poster
         * value in order to retrieve the image from the server
         */
        String Base_URL = "http://image.tmdb.org/t/p/w185";
        //getting the movie object using the getItem method which is defined in ArrayAdapter class
        Movies movie = getItem(position);
        //using the uri class the build the url of the poster
        Uri uri = Uri.parse(Base_URL).buildUpon().appendEncodedPath(movie.getPoster()).build();
        //creating instance of view holder class
        viewHolder.image =(ImageView) convertView.findViewById(R.id.grid_item_id);
        //using picasso to set the image to the poster
        Picasso.with(getContext()).load(uri.toString()).into(viewHolder.image);
        return convertView;
    }
    static class ViewHolder {
        ImageView image;
    }
}
