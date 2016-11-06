package com.example.ahmed.movieapp;

/**
 * Created by Ahmed on 11/6/2016.
 */
public class Trailer {
    private String mName , mKey , mSite;

    public Trailer(String name, String key, String site) {
        mName = name;
        mKey = key;
        mSite = site;
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
}
