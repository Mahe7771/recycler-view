package com.android.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Maheswari_V on 1/7/2017.
 */

public class VerticalViewHolder extends RecyclerView.ViewHolder {
    public TextView imgDescriptorTagView, imgDescriptorValueView, emptyView ;

    public VerticalViewHolder(View view) {
        super(view);
        imgDescriptorTagView = (TextView) view.findViewById(R.id.textViewHeading);
        imgDescriptorValueView = (TextView) view.findViewById(R.id.txtView);
        //emptyView = (TextView) view.findViewById(R.id.empty_view);
    }
}