package com.dicoding.kumparantest2021.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.dicoding.kumparantest2021.Http;
import com.dicoding.kumparantest2021.R;
import com.dicoding.kumparantest2021.adapter.PostAdapter;
import com.dicoding.kumparantest2021.helper.AppHelper;
import com.dicoding.kumparantest2021.helper.Config;
import com.dicoding.kumparantest2021.model.PostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PostActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvListPost;

    private ArrayList<PostModel> mList = new ArrayList<>();
    private PostAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        binding();

        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.post(new Runnable() {
            private void doNothing(){

            }
            @Override
            public void run() {
                getPostList();

            }
        });
        rvListPost.setHasFixedSize(true);
        rvListPost.setLayoutManager(new LinearLayoutManager(this));


    }

    public void show(){
        mAdapter = new PostAdapter(PostActivity.this, mList);
        rvListPost.setAdapter(mAdapter);
    }


    public void getPostList(){
        swipeRefresh.setRefreshing(true);
        AndroidNetworking.get(Config.BASE_URL + "posts")
                .setTag("Test")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONArray(new JSONArrayRequestListener() {
                    @Override
                    public void onResponse(JSONArray response) {
                        swipeRefresh.setRefreshing(false);
                        if (mAdapter != null) {
                            mAdapter.clearData();
                            mAdapter.notifyDataSetChanged();
                        }
                        if (mList != null)  mList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                final PostModel item = new PostModel();
                                item.setPOST_ID(jsonObject.optInt("id"));
                                item.setPOST_TITLE(jsonObject.optString("title"));
                                item.setPOST_BODY(jsonObject.optString("body"));
                                item.setUSER_ID(jsonObject.getInt("userId"));

                                AndroidNetworking.get(Config.BASE_URL + "users/" + item.getUSER_ID())
                                        .setPriority(Priority.LOW)
                                        .setTag("Test")
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    item.setUSER_NAME(response.optString("name"));
                                                    item.setUSER_EMAIL(response.optString("email"));
                                                    JSONObject company = response.getJSONObject("company");
                                                    item.setUSER_COMPANY_NAME(company.optString("name"));
                                                    mList.add(item);
                                                    show();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                swipeRefresh.setRefreshing(false);
                                                Toast.makeText(PostActivity.this, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
                                                Log.d("ZEE", "onError: " + anError.getErrorBody());
                                                Log.d("ZEE", "onError: " + anError.getLocalizedMessage());
                                                Log.d("ZEE", "onError: " + anError.getErrorDetail());
                                                Log.d("ZEE", "onError: " + anError.getResponse());
                                                Log.d("ZEE", "onError: " + anError.getErrorCode());
                                            }
                                        });

//                                item.setUSER_ID(jsonObject.optInt("userId"));
//                                mList.add(item);
//                                show();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(PostActivity.this, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
                        Log.d("ZEE", "onError: " + anError.getErrorBody());
                        Log.d("ZEE", "onError: " + anError.getLocalizedMessage());
                        Log.d("ZEE", "onError: " + anError.getErrorDetail());
                        Log.d("ZEE", "onError: " + anError.getResponse());
                        Log.d("ZEE", "onError: " + anError.getErrorCode());
                    }
                });



    }


    @Override
    public void onRefresh() {
        getPostList();

    }

    private void binding(){
        rvListPost = findViewById(R.id.rvListPost);
        swipeRefresh = findViewById(R.id.swipeRefresh);
    }
}