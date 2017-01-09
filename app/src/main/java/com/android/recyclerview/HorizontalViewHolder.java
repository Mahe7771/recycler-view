package com.android.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Maheswari_V on 1/8/2017.
 */

public class HorizontalViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgView;

        public HorizontalViewHolder(View view) {
            super(view);
            imgView = (ImageView) view.findViewById(R.id.imgView);
        }
}


