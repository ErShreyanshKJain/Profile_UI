package com.example.shreyanshjain.profile_ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class DownloadTask extends AsyncTask<String,Void,String> {

    MainActivity object;
    List<Card1Data> card1Data = new ArrayList<>();
    //ProgressDialog progressDialog;
    int pos;
    @Override
    protected String doInBackground(String... strings) {
        String result = "";

        URL url;

        HttpURLConnection urlConnection = null;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            Log.i("Comments", result);
            //pos = object.getData(result);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (JSONException e) {
            e.printStackTrace();
        }*/
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        object.setAdapter(pos);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(!object.isNetworkAvailable())
        {
            /*alertUserAboutError();
            newsHeadlines = db.getDetails();
        */}
        else
        {

        }
    }

}
