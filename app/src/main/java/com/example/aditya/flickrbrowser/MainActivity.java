package com.example.aditya.flickrbrowser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements GetFlickrJsonData.OnDataAvailable, Recyclerviewlistener.OnRecyclerItemClickListner {

    private static final String TAG = "MainActivity";
    private MyAdapter mAdapter;
    final static String PHOTO_TRANSFER = "PHOTO_TRANSFER";
    RecyclerView mRecyclerView;
    private ProgressBar progressBar;
    public static String Keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: Starts");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(new Recyclerviewlistener(mRecyclerView, this, this));
        Log.d(TAG, "onCreate: Stops");
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "onResume: Starts");
        super.onResume();
        progressBar.setVisibility(View.VISIBLE);
        GetFlickrJsonData getFlickrJsonData2 = new GetFlickrJsonData(this, "https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true);
        getFlickrJsonData2.executeOnSameThread(Keyword);
        Log.d(TAG, "onResume: Ends");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.searchmenu).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                progressBar.setVisibility(View.VISIBLE);
                GetFlickrJsonData getFlickrJsonData = new GetFlickrJsonData(MainActivity.this, "https://www.flickr.com/services/feeds/photos_public.gne", "en-us", true);
                Keyword="";
                Keyword=s;
                getFlickrJsonData.executeOnSameThread(Keyword);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                finish();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataAvailable(List<Photo> data, DownloadStatus status) {
        if (status == DownloadStatus.OK) {
            mAdapter = new MyAdapter(data, getApplicationContext());
            mRecyclerView.setAdapter(mAdapter);
            progressBar.setVisibility(View.INVISIBLE);

        } else
            Log.e(TAG, "onDataComplete: Error in Downloading data");
    }


    @Override
    public void onPress(View view, int position) {
        Log.d(TAG, "onPress: Starts");
        Intent intent;
        intent = new Intent(this, PhotodetailActivity.class);
        intent.putExtra(PHOTO_TRANSFER, mAdapter.getPhoto(position));
        startActivity(intent);
    }

    @Override
    public void onLongPress(View view, int position) {
        Log.d(TAG, "onLongPress: Starts");
    }


}
