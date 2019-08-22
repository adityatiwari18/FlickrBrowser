package com.example.aditya.flickrbrowser;

import android.app.MediaRouteButton;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

class GetFlickrJsonData implements GetRawData.OnDownloadComplete {
    private static final String TAG = "GetFlickrJsonData";

    private List<Photo> mPhotoList = null;
    private String mBaseURL;
    private String mLanguage;
    private Boolean mMatchAll;

    private final OnDataAvailable mCallback;

    interface OnDataAvailable {
        void onDataAvailable(List<Photo> data, DownloadStatus status);
    }

    public GetFlickrJsonData(OnDataAvailable callback, String baseURL, String language, Boolean matchAll) {
        mBaseURL = baseURL;
        mLanguage = language;
        mMatchAll = matchAll;
        mCallback = callback;
    }

    void executeOnSameThread(String SearchCriteria) {
        Log.d(TAG, "executeOnSameThread Starts");

        String DestinationUri = createUri(SearchCriteria, mLanguage, mMatchAll);
        GetRawData getRawData = new GetRawData(this);
        getRawData.execute(DestinationUri);


        Log.d(TAG, "executeOnSameThread: Ends");

    }

    private String createUri(String SearchCriteria, String language, Boolean MatchAll) {

        return Uri.parse(mBaseURL).buildUpon()
                .appendQueryParameter("tags", SearchCriteria)
                .appendQueryParameter("tagmode", MatchAll ? "ALL" : "ANY")
                .appendQueryParameter("lang", language)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString();
    }

    @Override
    public void onDownloadComplete(String data, DownloadStatus status) {

        if (status == DownloadStatus.OK) {
            mPhotoList= new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONArray ItemArray = jsonObject.getJSONArray("items");

                for (int i = 0; i < ItemArray.length(); i++) {
                    JSONObject jsonphoto = ItemArray.getJSONObject(i);
                    String title = jsonphoto.getString("title");
                    String author = jsonphoto.getString("author");
                    String author_id = jsonphoto.getString("author_id");
                    String tags = jsonphoto.getString("tags");

                    JSONObject jsonmedia = jsonphoto.getJSONObject("media");
                    String photourl = jsonmedia.getString("m");
                    String Link = photourl.replaceFirst("_m.", "_b.");

                    Photo photoObject = new Photo(title,author,author_id,Link,tags,photourl);
                    mPhotoList.add(photoObject);

                }
            } catch (JSONException e) {
                Log.e(TAG, "onDownloadComplete: JSON error" + e.getMessage());
                status=DownloadStatus.FAILED_OR_EMPTY;
            }
            if(mCallback!=null){
                status=DownloadStatus.OK;
                mCallback.onDataAvailable(mPhotoList, status);
            }

        }
    }
}
