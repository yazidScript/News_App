package com.dicoding.kumparantest2021.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.dicoding.kumparantest2021.Http;
import com.dicoding.kumparantest2021.R;
import com.dicoding.kumparantest2021.adapter.AlbumAdapter;
import com.dicoding.kumparantest2021.adapter.CommentAdapter;
import com.dicoding.kumparantest2021.helper.AppHelper;
import com.dicoding.kumparantest2021.helper.Config;
import com.dicoding.kumparantest2021.model.AlbumModel;
import com.dicoding.kumparantest2021.model.CommentModel;
import com.dicoding.kumparantest2021.model.PostModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private ImageView ivBack;
    private TextView tvUserName, tvUserEmail, tvUserCompany, tvUserAddress;

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rvListAlbum;

    private ArrayList<AlbumModel> mList = new ArrayList<>();
    private AlbumAdapter mAdapter;

    private int U_ID;
    private PostModel pModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        binding();

        pModel = getIntent().getExtras().getParcelable("extra_user");
        if (/*bundle*/ pModel != null) {

            U_ID = pModel.getUSER_ID();

            tvUserEmail.setText(pModel.getUSER_EMAIL());
            tvUserAddress.setText(pModel.getUSER_ADDRESS());
            tvUserName.setText(pModel.getUSER_NAME());
            tvUserCompany.setText(pModel.getUSER_COMPANY_NAME());
        }
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.post(new Runnable() {
            private void doNothing(){

            }
            @Override
            public void run() {
                getAlbumList();
            }
        });
        rvListAlbum.setHasFixedSize(true);
        rvListAlbum.setLayoutManager(new LinearLayoutManager(this));

    }

    public void show(){
        mAdapter = new AlbumAdapter(UserDetailActivity.this, mList);
        rvListAlbum.setAdapter(mAdapter);
    }

    public void getAlbumList(){
        swipeRefresh.setRefreshing(true);
        AndroidNetworking.get(Config.BASE_URL + "albums?userId=" + U_ID)
                .setPriority(Priority.LOW)
                .setOkHttpClient(((Http) getApplication()).getOkHttpClient())
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
                                AlbumModel item = AppHelper.mapAlbumModel(jsonObject);
                                mList.add(item);
                                show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(UserDetailActivity.this, Config.TOAST_AN_ERROR, Toast.LENGTH_SHORT).show();
                        Log.d("ZEE", "onError: " + anError.getErrorBody());
                        Log.d("ZEE", "onError: " + anError.getLocalizedMessage());
                        Log.d("ZEE", "onError: " + anError.getErrorDetail());
                        Log.d("ZEE", "onError: " + anError.getResponse());
                        Log.d("ZEE", "onError: " + anError.getErrorCode());
                    }
                });



    }

    private void binding() {
        tvUserName = findViewById(R.id.tvUserName);
        tvUserCompany = findViewById(R.id.tvUserCompany);
        tvUserAddress = findViewById(R.id.tvUserAddress);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            private void doNothing() {

            }
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvListAlbum = findViewById(R.id.rvListAlbum);
        swipeRefresh = findViewById(R.id.swipeRefresh);
    }

    @Override
    public void onRefresh() {
        getAlbumList();
    }
}