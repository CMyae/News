package com.chan.samples.news.ui.edit.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chan.samples.news.R;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.ui.edit.holders.ChannelViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chan on 2/20/18.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelViewHolder> {

    private Context context;
    private List<Bookmark> channels;
    private boolean isEditable;

    public ChannelAdapter(Context context, List<Bookmark> channels) {
        this.context = context;
        this.channels = channels;
    }


    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_row_channel,parent,false);
        return new ChannelViewHolder(v,channels);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        holder.bindView(channels.get(position),isEditable);
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }


    public void setEditable(boolean edit){
        isEditable = edit;
        notifyDataSetChanged();
    }


    public List<Bookmark> getSelectedChannel(){
        List<Bookmark> bookmarks = new ArrayList<>();
        for(Bookmark b : channels){
            if(b.getStatus() != Bookmark.UNCHECKED){
                bookmarks.add(b);
            }
        }
        return bookmarks;
    }


    public void setPreviousBookmark(){

    }

}
