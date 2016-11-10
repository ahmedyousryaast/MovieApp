package com.example.ahmed.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 11/7/2016.
 */
public class Review implements Parcelable{
    String mAuthor, mContent;

    public Review(String author, String content) {
        mAuthor = author;
        mContent = content;
    }

    protected Review(Parcel in) {
        mAuthor = in.readString();
        mContent = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public String getAuthor() {

        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }

    @Override
    public String toString() {
        return "Review{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mContent='" + mContent + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
    }
}
