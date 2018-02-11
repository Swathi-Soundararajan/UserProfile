package com.example.user.userprofile;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String URL="https://test-api.nevaventures.com/";
    private ShimmerFrameLayout mShimmerViewContainer;
    private RecyclerView recyclerView;
    private List<UserList> userLists;
    boolean noconn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setIcon(R.drawable.ic_group);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        if(!isNetworkAvailable()){
            noconn=true;
             new AlertDialog.Builder(this)
                    .setTitle("Closing the App")
                    .setMessage("No Internet Connection,check your settings")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                     .show();
        }
        recyclerView = (RecyclerView) findViewById(R.id.recycview);
        recyclerView.setHasFixedSize(true);
        if(recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
        userLists = new ArrayList<>();
        DownloadTask task= new DownloadTask();
        task.execute(URL);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public class DownloadTask extends AsyncTask<String, String, String> {
        protected String doInBackground(String... strings) {
            StringBuilder res = new StringBuilder();
            java.net.URL url;
            HttpURLConnection urlConnection=null;
            try {
                    url = new URL(URL);
                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);

                    int data = reader.read();
                    while (data != -1) {
                        char current = (char) data;
                        res.append(current);
                        data = reader.read();
                    }
                    return res.toString();
            } catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            super.onPostExecute(res);
            try {
                if(noconn!=true) {
                    JSONObject jsonObject = new JSONObject(res);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    HashSet<String> myMap = new HashSet<String>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        //String name = object.getString("id");
                        if (object.has("id") && object.has("name") && object.has("image")) {
                            if (myMap.add(object.getString("name"))) {
                                Log.e("UserName", object.getString("name"));
                                Log.e("Id", object.getInt("id") + " ");
                                UserList list = new UserList(object.getString("name").trim(), object.getString("skills").toLowerCase().trim(), object.getString("image"));
                                userLists.add(list);
                            } else {
                                Log.e("Item ", "Already present" + object.getInt("id"));
                            }

                        }
                    }
                }

                UserAdapter adapter = new UserAdapter(userLists,getApplicationContext());
                recyclerView.setAdapter(adapter);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
}
