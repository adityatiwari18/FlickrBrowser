package com.example.aditya.flickrbrowser;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

enum DownloadStatus {IDLE, PROCESSING, NOT_INITIALISED, FAILED_OR_EMPTY, OK}

class GetRawData extends AsyncTask<String, Void, String> {

    private static final String TAG = "GetRawData";
    private DownloadStatus Status;
    private OnDownloadComplete mCallback;

    interface OnDownloadComplete{
        void onDownloadComplete(String data , DownloadStatus status);
    }

    public GetRawData(OnDownloadComplete callback) {
        this.Status = DownloadStatus.IDLE;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(String s) {
//        Log.d(TAG, "onPostExecute: Parameter = "+s);
        if(mCallback!=null)
            mCallback.onDownloadComplete(s, Status);
    }

    @Override
    protected String doInBackground(String... strings) {
        Log.d(TAG, "doInBackground: Starts");
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        if (strings == null) {
            Status = DownloadStatus.NOT_INITIALISED;
        }

        try {
            Status = DownloadStatus.PROCESSING;
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            int response = connection.getResponseCode();

            Log.d(TAG, "doInBackground: Response code was " + response);

            StringBuilder result = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

//            String line;
//            while(null!=(line = reader.readLine())){
            for (String line = reader.readLine(); line != null; line = reader.readLine()) {
                result.append(line).append("\n");
            }
            Status = DownloadStatus.OK;
            return result.toString();

        } catch (MalformedURLException e) {
            Log.e(TAG, "doInBackground: Invalid URL" + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "doInBackground: Null point exception"+e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "doInBackground: Error In Reading Data" + e.getMessage());
        } catch (SecurityException e) {
            Log.e(TAG, "doInBackground: Needs Permission" + e.getMessage());
        }finally{
            if(connection!=null)
                connection.disconnect();
            if(reader!=null){
                try {
                    reader.close();
                }catch (IOException e) {
                    Log.e(TAG, "doInBackground: Error in closing" + e.getMessage());
                }
            }
        }
        return null;
    }
}
