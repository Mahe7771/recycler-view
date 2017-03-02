package com.android.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Maheswari_V on 1/7/2017.
 */

public class VerticalViewHolder extends RecyclerView.ViewHolder {
    public TextView workOwner, workLogTime ;
    public ImageView workLogImage;

    public VerticalViewHolder(View view) {
        super(view);
        workOwner = (TextView) view.findViewById(R.id.workOwner);
        workLogTime = (TextView) view.findViewById(R.id.workLogTime);
        workLogImage = (ImageView) view.findViewById(R.id.workLogImage);

    }
}