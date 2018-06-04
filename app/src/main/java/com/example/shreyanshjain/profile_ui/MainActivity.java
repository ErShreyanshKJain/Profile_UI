package com.example.shreyanshjain.profile_ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shreyanshjain.profile_ui.Adapters.Card1Adapter;
import com.example.shreyanshjain.profile_ui.Adapters.CardAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

//    Nav Drawer
    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private ImageView nav_image;
    private TextView nav_text;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;

    ArrayList<String> list;
    ArrayAdapter<String> arrayAdapter;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Card1Data> card1Data = new ArrayList<>();
    CollapsingToolbarLayout collapsingToolbarLayout;
    AppCompatImageView backImage;
    RoundedImage profileImage;
    AppBarLayout appBar;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        list = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);

        swipeRefreshLayout = findViewById(R.id.swipe);
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.GREEN);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000);
            }
        });

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

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Profile UI");
        getSupportActionBar().setTitle("com.cricket.au");
//        mToolbar.setBackgroundColor(R.color.graylight);

        drawerLayout = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(MainActivity.this,drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View headerView = navigationView.getHeaderView(0);

        nav_text = headerView.findViewById(R.id.nav_text);
        nav_image = headerView.findViewById(R.id.nav_image);

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
                    layoutParams.setMargins(0,100,0,0);
                    recyclerView.setLayoutParams(layoutParams);
                }
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setAdapter(0); // TO set the adapter at the first position by default

    }

    public void setAdapter()
    {
        adapter = new Card1Adapter(card1Data, MainActivity.this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setAdapter(int pos)
    {
        adapter = new CardAdapter(card1Data,MainActivity.this,pos);
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void getData(String jsonData) throws JSONException {

        //appBarLayout = (AppBarLayout)findViewById(R.id.appBarLayout);
        backImage = (AppCompatImageView)findViewById(R.id.backImage);
        profileImage = (RoundedImage)findViewById(R.id.singleCompLogo);
        JSONParsing jsonParsing = new JSONParsing(jsonData);
        //JSONObject source = new JSONObject(jsonData);
        int code = jsonParsing.getCode();                                  //source.getInt("code");
        if(code == 101)
        {// Make "result" a JSON object to fetch all other values from it
            //JSONObject result = source.getJSONObject("result");
            //String header = result.getString("top_heading");
            //int top = jsonParsing.getTop();                                                  //result.getInt("top_image");
            int top = 1;
            if(top==1) {
                //String bg = result.getString("top_image_bg");
                //setImage(1,bg);
                Picasso.with(this)
                        .load(jsonParsing.getBg())
                        .into(backImage);
                //String fg = result.getString("top_image_fg");
                //setImage(2,fg);
                Picasso.with(this)
                        .load(jsonParsing.getFg())
                        .into(profileImage);
                collapsingToolbarLayout.setTitle(jsonParsing.getHeader());
            }
            else{
                //set collapseToolbar to toolbar or supportActionBar
                /*collapsingToolbarLayout.setVisibility(View.INVISIBLE);
                CoordinatorLayout.LayoutParams lay = (CoordinatorLayout.LayoutParams)appBar.getLayoutParams();
                lay.height = 140;
                appBar.setLayoutParams(lay);
                */
                //toolbar.setTitle(header);
                //appBar.setBackgroundColor(getResources().getColor(R.color.endblue));

                appBar.setExpanded(false);
                recyclerView.setNestedScrollingEnabled(false);

                /*appBar.setVisibility(View.INVISIBLE);
                appBarLayout.setVisibility(View.VISIBLE);
                */
                //Set the gravity of the Collapsing Toolbar Title in the switch below
                /*switch (result.get("Collapsed_Title_Gravity")) {
                    case "Right" : collapsingToolbarLayout.setCollapsedTitleGravity();
                }*/
                profileImage.setVisibility(View.INVISIBLE);
                setSupportActionBar(mToolbar);
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)recyclerView.getLayoutParams();
                layoutParams.setMargins(0,0,0,0);
                recyclerView.setLayoutParams(layoutParams);
            }

            JSONArray data = jsonParsing.getData();                               //result.getJSONArray("data");
            for (int i = 0;i<data.length();i++)
            {
                jsonParsing.setDetails(data.getString(i));
                /*String temp = data.getString(i);
                JSONObject jsonObject = new JSONObject(temp);
                String text1 = jsonObject.getString("text1");
                //Log.v("Title", title);
                String text2 = jsonObject.getString("text2");
                //Log.v("Desc", description);
                String text3 = jsonObject.getString("text3");
                //Log.v("url", url);
                String imageUrl = jsonObject.getString("image");*/ //Make the image circular
                Card1Data card1 = jsonParsing.getTexts();                                          /*new Card1Data(text1,text2,text3,imageUrl);*/
                card1Data.add(card1);
            }
            //getSupportActionBar().setTitle(header);
            //int type = jsonParsing.getType();                                              //result.getInt("view_type");
            int type = 2;
            if (type == 2)
                setAdapter();
            else
                setAdapter(type);
        }
        else
        {
            Toast.makeText(MainActivity.this,jsonParsing.getSource().getString("message"),Toast.LENGTH_LONG)
                 .show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_food);
        android.widget.SearchView searchView = (android.widget.SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new android.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                arrayAdapter.getFilter().filter(s);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);


    }
}
