package com.example.aditya.flickrbrowser;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

class Recyclerviewlistener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "Recyclerviewlistener";

    interface OnRecyclerItemClickListner {
        void onPress(View view, int position);
        void onLongPress(View view, int position);
    }
    private OnRecyclerItemClickListner mlistener;
    private GestureDetectorCompat mGestureDetector;

    public Recyclerviewlistener(final RecyclerView recyclerView, Context context, final OnRecyclerItemClickListner mlistener) {
        this.mlistener = mlistener;
        this.mGestureDetector = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: Starts");

                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                        if(mlistener!=null && childView!=null)
                            mlistener.onPress(childView, recyclerView.getChildAdapterPosition(childView));
                return true;
            }


            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: Starts");

                View childView = recyclerView.findChildViewUnder(e.getX(),e.getY());
                if(childView!=null && mlistener!=null)
                    mlistener.onLongPress(childView, recyclerView.getChildAdapterPosition(childView));
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: ");
        if(mGestureDetector!=null) {
            Log.d(TAG, "onInterceptTouchEvent: true");
            boolean result = mGestureDetector.onTouchEvent(e);
            return result;
        }
        else
            return false;
    }
}