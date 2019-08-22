package com.example.aditya.flickrbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Viewholder> {

    private List<Photo> mPhotoList;
    private Context mContext;


    public MyAdapter(List<Photo> photoList, Context context) {
        mPhotoList = photoList;
        mContext = context;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.browse, viewGroup, false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int i) {
        Photo photoitem = mPhotoList.get(i);

        viewholder.title.setText(photoitem.getTitle());
        Picasso.get().load(photoitem.getImage()).into(viewholder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return mPhotoList.size();
    }

    public Photo getPhoto(int position) {
        return mPhotoList.get(position);
    }


    static class Viewholder extends RecyclerView.ViewHolder {

        ImageView thumbnail;
        TextView title;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            this.title = (TextView) itemView.findViewById(R.id.title);
        }
    }
}
