package com.example.shreyanshjain.profile_ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.shreyanshjain.profile_ui.Adapters.Card1Adapter;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Drawer drawer = null;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Card1Data> card1Data = new ArrayList<>();
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppCompatImageView backImage;
    RoundedImage profileImage;
    AppBarLayout appBar;
    Toolbar toolbar;
    String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String url = "http://bydegreestest.agnitioworld.com/test/mobile_app.php";
        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            final Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code: +" + response);
                    } else {
                        String responseBody = response.body().string();
                        //result = responseBody;
                        Log.i("Response", responseBody);
                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    getData(responseBody);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            });
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolBar);
        //collapsingToolbarLayout.setTitle("Profile UI");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("My Profile");

        // Setting the margins of Recycler View while the toolbar is collapsed to remove the empty space in between the toolbar and recycler view
        appBar = findViewById(R.id.appBar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
                if(Math.abs(verticalOffset) == appBar.getTotalScrollRange()){
                    Log.d("appbar",""+recyclerView.getScaleY());
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) recyclerView.getLayoutParams(); // Redundant Code with line 119
                    layoutParams.setMargins(0,0,0,0);
                    recyclerView.setLayoutParams(layoutParams);
                    //Animate the scrolling
                }
                else if(Math.abs(verticalOffset) == 0){
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)recyclerView.getLayoutParams();
                    layoutParams.setMargins(0,80,0,0);
                    recyclerView.setLayoutParams(layoutParams);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //setAdapter(0); // TO set the adapter at the first position by default

        // Building a Navigation Drawer using a library
        drawer = new DrawerBuilder(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withActionBarDrawerToggleAnimated(true)
                .inflateMenu(R.menu.main)
                .withCloseOnClick(true)
                .withSavedInstance(savedInstanceState)
                .build();

        drawer.setOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                adapter = new Card1Adapter(card1Data, MainActivity.this,position);
////                    case 3 : Log.d("Option Selected","Image with Text" + position);
////                        card1Data = new ArrayList<>();
////                        for (int i = 0; i < 10; i++) {
////                            Card1Data card = new Card1Data("Heading", "Sub Heading", "Description");
////                            card1Data.add(card);
////                        }
////                        adapter = new Card1Adapter(card1Data, MainActivity.this);
////                        break;
//                recyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
                setAdapter(position);
                onBackPressed();
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = drawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void setAdapter(int position)
    {
        adapter = new Card1Adapter(card1Data, MainActivity.this, position);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    protected boolean isNetworkAvailable(){
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    public void getData(String jsonData) throws JSONException {

        //String jsonData = result;
        backImage = (AppCompatImageView)findViewById(R.id.backImage);
        profileImage = (RoundedImage)findViewById(R.id.singleCompLogo);
        JSONObject source = new JSONObject(jsonData);
        int code = source.getInt("code");
        if(code == 101)
        {// Make "result" a JSON object to fetch all other values from it
            JSONObject result = source.getJSONObject("result");
            String header = result.getString("top_heading");
            //int top = result.getInt("top_image");
            int top = 1;
            if(top==1) {
                String bg = result.getString("top_image_bg");
                //setImage(1,bg);
                Picasso.with(this)
                        .load(bg)
                        .into(backImage);
                String fg = result.getString("top_image_fg");
                //setImage(2,fg);
                Picasso.with(this)
                        .load(fg)
                        .into(profileImage);
                collapsingToolbarLayout.setTitle(header);
            }
            else{
                //set collapseToolbar to toolbar or supportActionBar
                //collapsingToolbarLayout.setVisibility(View.INVISIBLE);
                //setActionBar(toolbar);
                profileImage.setVisibility(View.INVISIBLE);
                //setSupportActionBar(toolbar);
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)recyclerView.getLayoutParams();
                layoutParams.setMargins(0,0,0,0);
                recyclerView.setLayoutParams(layoutParams);
            }

            JSONArray data = result.getJSONArray("data");
            for (int i = 0;i<data.length();i++)
            {
                String temp = data.getString(i);
                JSONObject jsonObject = new JSONObject(temp);
                String text1 = jsonObject.getString("text1");
                //Log.v("Title", title);
                String text2 = jsonObject.getString("text2");
                //Log.v("Desc", description);
                String text3 = jsonObject.getString("text3");
                //Log.v("url", url);
                String imageUrl = jsonObject.getString("image"); //Make the image circular
                Card1Data card1 = new Card1Data(text1,text2,text3,imageUrl);
                card1Data.add(card1);
            }
            getSupportActionBar().setTitle(header);
            setAdapter(result.getInt("view_type"));
        }
        else
        {
            Toast.makeText(MainActivity.this,source.getString("message"),Toast.LENGTH_LONG)
                    .show();
        }
    }

    /*public void setImage(int i,String url) {
        if(i == 1) {
            Picasso.with(this)
                    .load(url)
                    .into(backImage);
        }
        else{
            Picasso.with(this)
                    .load(url)
                    .into(profileImage);
        }

    }*/
}
