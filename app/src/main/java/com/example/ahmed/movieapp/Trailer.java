package com.example.ahmed.movieapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 11/6/2016.
 */
public class Trailer implements Parcelable{
    private String mName , mKey , mSite;

    public Trailer(String name, String key, String site) {
        mName = name;
        mKey = key;
        mSite = site;
    }

    /**
     * constructor to create object from a parcel
     * the order that statements are written in is important
     * (it must match the same order that the content was encoded in)
     * @param parcel
     */
    public Trailer(Parcel parcel) {
        mName = parcel.readString();
        mKey = parcel.readString();
        mSite = parcel.readString();
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        mKey = key;
    }

    public String getSite() {
        return mSite;
    }

    public void setSite(String site) {
        mSite = site;
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "mName='" + mName + '\'' +
                ", mKey='" + mKey + '\'' +
                ", mSite='" + mSite + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * this method takes in a parcel as a parameter
     * and writes the content of the class to it
     * @param parcel
     * @param i
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mKey);
        parcel.writeString(mSite);
    }
    //interface that must be implemented , source : https://developer.android.com/reference/android/os/Parcelable.Creator.html
    public static final Parcelable.Creator<Trailer> CREATOR = new Creator<Trailer>() {
        //create parcel and create object from it
        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
