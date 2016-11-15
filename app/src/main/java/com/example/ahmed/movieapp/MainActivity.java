package com.example.ahmed.movieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MoviesFragment.Callback{

    boolean mTwoPane;
    private static final String DETAILFRAGMENT_TAG = "DFTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.details_activity_frame) != null) {
            mTwoPane = true;
            if(savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.details_activity_frame, new DetailsFragment(),DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * this method adds an overflow menu which has
     * a setting button , this will be used later to
     * change the sorting method
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    /**
     * comment here
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.Settings_menu){
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(Movies movie) {
        if(mTwoPane){

            Bundle args = new Bundle();
            args.putParcelable("movie", movie);
            DetailsFragment df = new DetailsFragment();
            df.setArguments(args);
            Log.d("testt","tablet");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_activity_frame, df, DETAILFRAGMENT_TAG)
                    .commit();

        }
        else {
            Log.d("tablet","test tablet");
            Intent intent = new Intent(this,DetailsActivity.class);
            intent.putExtra("movie",movie);
            startActivity(intent);
        }
    }
}
