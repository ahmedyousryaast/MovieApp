package com.example.ahmed.movieapp.data;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.ahmed.movieapp.Movies;
import com.example.ahmed.movieapp.Review;
import com.example.ahmed.movieapp.Trailer;

import java.util.ArrayList;

/**
 * Created by Ahmed on 11/11/2016.
 */
public class ContainerClass implements Parcelable{
    Movies mMovie;
    ArrayList<Trailer>mTrailers;
    ArrayList<Review>mReviews;

    public ContainerClass(Movies movie, ArrayList<Trailer> trailers, ArrayList<Review> reviews) {
        mMovie = movie;
        mTrailers = trailers;
        mReviews = reviews;
    }

    protected ContainerClass(Parcel in) {
        mMovie = in.readParcelable(Movies.class.getClassLoader());
        mTrailers = in.createTypedArrayList(Trailer.CREATOR);
        mReviews = in.createTypedArrayList(Review.CREATOR);
    }

    public static final Creator<ContainerClass> CREATOR = new Creator<ContainerClass>() {
        @Override
        public ContainerClass createFromParcel(Parcel in) {
            return new ContainerClass(in);
        }

        @Override
        public ContainerClass[] newArray(int size) {
            return new ContainerClass[size];
        }
    };

    public Movies getMovie() {
        return mMovie;
    }

    public void setMovie(Movies movie) {
        mMovie = movie;
    }

    public ArrayList<Trailer> getTrailers() {
        return mTrailers;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        mTrailers = trailers;
    }

    public ArrayList<Review> getReviews() {
        return mReviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        mReviews = reviews;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mMovie, i);
        parcel.writeTypedList(mTrailers);
        parcel.writeTypedList(mReviews);
    }
}
