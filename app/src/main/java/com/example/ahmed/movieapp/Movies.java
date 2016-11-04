package com.example.ahmed.movieapp;


import java.io.Serializable;

/**
 * Created by Ahmed on 10/9/2016.
 * this class represents a movies and its details
 */
public class Movies implements Serializable{

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
    Movies(String mPoster, String mDate, String mTitle, String mOverview , String mBackdrop , String mVoteAvg , String mId){
        this.mPoster = mPoster;
        this.mDate = mDate;
        this.mTitle = mTitle;
        this.mOverview = mOverview;
        this.mBackdrop = mBackdrop;
        this.mVoteAvg = mVoteAvg;
        this.mId = mId;
    }

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