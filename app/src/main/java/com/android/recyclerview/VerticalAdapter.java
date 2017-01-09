package com.android.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Maheswari_V on 1/7/2017.
 */

public class VerticalAdapter extends RecyclerView.Adapter<VerticalViewHolder> {

    private List<ImageDescriptor> verticalList;
    private Context context;

    public VerticalAdapter(Context context,List<ImageDescriptor> verticalList) {
        this.verticalList = verticalList;
        this.context = context;

    }

    @Override
    public VerticalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vertical_item_view, parent, false);

        return new VerticalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VerticalViewHolder holder, final int position) {
        ImageDescriptor imgDescriptor = verticalList.get(position);
        holder.imgDescriptorTagView.setText(imgDescriptor.getTagName());
        holder.imgDescriptorValueView.setText(imgDescriptor.getTagValue());
        //holder.emptyView.setText("Click on an image below to view details");
    }

    @Override
    public int getItemCount() {
        return verticalList.size();
    }
}
