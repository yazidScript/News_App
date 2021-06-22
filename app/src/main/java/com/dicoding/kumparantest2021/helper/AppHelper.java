package com.dicoding.kumparantest2021.helper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dicoding.kumparantest2021.activity.DetailPostActivity;
import com.dicoding.kumparantest2021.model.CommentModel;
import com.dicoding.kumparantest2021.model.PostModel;

import org.json.JSONObject;

public final class AppHelper {
    public static PostModel mapPostModel(JSONObject rowData) {
        final PostModel item = new PostModel();
        item.setPOST_ID(rowData.optInt("id"));
        item.setPOST_TITLE(rowData.optString("title"));
        item.setPOST_BODY(rowData.optString("body"));
        item.setUSER_ID(rowData.optInt("userId"));

        return item;
    }

    public static PostModel mapUserModel(JSONObject rowData) {
        PostModel item = new PostModel();
        item.setUSER_NAME(rowData.optString("name"));
//        item.setUSER_COMPANY_NAME(rowData.optString("name"));

        return item;
    }

    public static void goToPostDetail(Context context, PostModel rowData) {
        Bundle bundle = new Bundle();

        bundle.putString("id", String.valueOf(rowData.getPOST_ID()));
        bundle.putString("title", rowData.getPOST_TITLE().toUpperCase());
        bundle.putString("body", rowData.getPOST_BODY().toUpperCase());
        bundle.putString("id", String.valueOf(rowData.getUSER_ID()));
        bundle.putString("email", rowData.getUSER_EMAIL().toUpperCase());
        bundle.putString("address", rowData.getUSER_ADDRESS());
        bundle.putString("name", rowData.getUSER_NAME().toUpperCase());
        bundle.putString("companyName", rowData.getUSER_COMPANY_NAME().toUpperCase());

        Intent i = new Intent(context, DetailPostActivity.class);
        i.putExtra("extra_post", rowData);
        context.startActivity(i);
    }

    public static CommentModel mapCommentModel(JSONObject rowData) {
        CommentModel item = new CommentModel();
        item.setCOMMENT_ID(rowData.optInt("id"));
        item.setCOMMENT_NAME(rowData.optString("name"));
        item.setCOMMENT_BODY(rowData.optString("body"));
        item.setPOST_ID(rowData.optInt("postId"));

        return item;
    }
}
