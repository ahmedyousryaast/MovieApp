package com.example.ahmed.movieapp;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Ahmed on 10/9/2016.
 * this class represents a movies and its details
 */
public class Movies implements Parcelable{

    private String mPoster,mDate,mTitle,mOverview,mBackdrop,mVoteAvg , mId;

    /**
     * this is a constructor used to create movie objects.
     * the constructor parameters are those who will be
     * displayed in the details activity for the user
     * @param mPoster hold the poster path
     * @param mDate holds the release date of the movie
     * @param mTitle holds the move title
     * @param mOverview holds an overview of the movie
     * @param mBackdrop holds the path of a backdrop image
     * @param mVoteAvg holds the average vote rate
     * @param mId holds the id of the movie
     */
    public Movies(String mPoster, String mDate, String mTitle, String mOverview , String mBackdrop , String mVoteAvg , String mId){
        this.mPoster = mPoster;
        this.mDate = mDate;
        this.mTitle = mTitle;
        this.mOverview = mOverview;
        this.mBackdrop = mBackdrop;
        this.mVoteAvg = mVoteAvg;
        this.mId = mId;
    }

    protected Movies(Parcel in) {
        mPoster = in.readString();
        mDate = in.readString();
        mTitle = in.readString();
        mOverview = in.readString();
        mBackdrop = in.readString();
        mVoteAvg = in.readString();
        mId = in.readString();
    }

    /**
     *  this function takes in a parcel as parameter
     *  and writes the content of the object into that parcel
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPoster);
        dest.writeString(mDate);
        dest.writeString(mTitle);
        dest.writeString(mOverview);
        dest.writeString(mBackdrop);
        dest.writeString(mVoteAvg);
        dest.writeString(mId);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    //interface that must be implemented , source : https://developer.android.com/reference/android/os/Parcelable.Creator.html
    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        //create a parcel and create object from it
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    //returns the value of mPoster
    public String getPoster() {
        return mPoster;
    }

    //changes the value of mPoster
    public void setPoster(String poster) {
        mPoster = poster;
    }

    //returns the value of mDate
    public String getDate() {
        return mDate;
    }

    //sets the value of mDate
    public void setDate(String date) {
        mDate = date;
    }

    //returns the value of mTitle
    public String getTitle() {
        return mTitle;
    }

    //sets the value of mTitle
    public void setTitle(String title) {
        mTitle = title;
    }

    //returns the value of mOverview
    public String getOverview() {
        return mOverview;
    }

    //sets the value of mOverview
    public void setOverview(String overview) {
        mOverview = overview;
    }

    //returns the value of mBackdrop
    public String getBackdrop() {
        return mBackdrop;
    }

    //sets the value of mBackdrop
    public void setBackdrop(String backdrop) {
        mBackdrop = backdrop;
    }

    //returns the value of mVoteAvg
    public String getVoteAvg() {
        return mVoteAvg;
    }

    //sets the value of mVoteAvg
    public void setVoteAvg(String voteAvg) {
        mVoteAvg = voteAvg;
    }

    //returns the value of mId
    public String getId() {
        return mId;
    }

    //sets the value of mId
    public void setId(String id) {
        mId = id;
    }

    @Override
    public String toString() {
        return "Movies{" +
                "mPoster='" + mPoster + '\'' +
                ", mDate='" + mDate + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mBackdrop='" + mBackdrop + '\'' +
                ", mVoteAvg='" + mVoteAvg + '\'' +
                ", mId='" + mId + '\'' +
                '}';
    }
}