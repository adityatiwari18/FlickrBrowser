package com.example.aditya.flickrbrowser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PhotodetailActivity extends AppCompatActivity {

    ImageView Photodetail;
    TextView Title;
    TextView Auther;
    TextView Tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photodetail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Photo photo = (Photo) intent.getSerializableExtra("PHOTO_TRANSFER");


        if(photo!=null){

            Title = (TextView) findViewById(R.id.photo_title);
            Title.setTypeface(Typeface.SANS_SERIF);
            Title.setText("Title : "+photo.getTitle());

            Tags = (TextView) findViewById(R.id.photo_tags);
            Title.setTypeface(Typeface.SANS_SERIF);
            Tags.setText("Tags : "+photo.getTags());

            Auther = (TextView) findViewById(R.id.photo_author);
            Auther.setTypeface(Typeface.DEFAULT_BOLD);
            Auther.setText("Auther : "+photo.getAuthor());

            Photodetail = (ImageView) findViewById(R.id.photo_image);
            Picasso
                    .get()
                    .load(photo.getLink())
                    .into(Photodetail);
        }

    }

}
