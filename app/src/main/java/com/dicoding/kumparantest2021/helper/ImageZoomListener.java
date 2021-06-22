package com.dicoding.kumparantest2021.helper;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.dicoding.kumparantest2021.activity.PhotoDetailActivity;

/**
 * Created by rr on 7/25/18.
 */

public class ImageZoomListener implements View.OnClickListener {
    private ImageView imageView;
    private String drawable;
    private Context context;
    private String title;
    private String info;

    public ImageZoomListener(Context context, ImageView imageView, String drawable, String title) {
        this.imageView = imageView;
        this.drawable = drawable;
        this.context = context;
        this.title = title;
    }

    @Override
    public void onClick(View v) {
        if(this.drawable == null) {
            return;
        }
        Intent intent = new Intent(context, PhotoDetailActivity.class);
        int[] screenLocation = new int[2];
        imageView.getLocationOnScreen(screenLocation);

        //Pass the image title and url to DetailsActivity
        intent.putExtra("left", screenLocation[0]).
                putExtra("top", screenLocation[1]).
                putExtra("width", imageView.getWidth()).
                putExtra("height", imageView.getHeight()).
                putExtra("title", title).
                putExtra("info", info).
                putExtra("image", drawable);

        context.startActivity(intent);
    }
}
