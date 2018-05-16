package com.chan.samples.news.ui.edit.holders;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chan.samples.news.R;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.utils.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chan on 2/21/18.
 */

public class ChannelViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvChannelName) TextView tvChannelName;
    @BindView(R.id.chkChannel) CheckBox chkChannel;

    private List<Bookmark> channels;

    public ChannelViewHolder(View itemView, final List<Bookmark> bookmarks) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.channels = bookmarks;

        chkChannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bookmark bookmark = channels.get(getAdapterPosition());
                if(bookmark == null) return;
                boolean isChecked = chkChannel.isChecked();
                if(isChecked){
                    bookmark.setStatus(Bookmark.CHECKED);
                }else{
                    bookmark.setStatus(Bookmark.UNCHECKED);
                }
            }
        });

    }

    public void bindView(Bookmark channel,boolean isEditable){

        if(isEditable){
            chkChannel.setEnabled(true);
        }else{
            chkChannel.setEnabled(false);
        }

        String channelName = Util.getCapitalizeName(channel.getName());
        tvChannelName.setText(channelName);

        if(channel.getStatus() == Bookmark.BOOKMARKED){
            chkChannel.setVisibility(View.VISIBLE);
            chkChannel.setChecked(true);
        }else{
            if (isEditable) {
                chkChannel.setVisibility(View.VISIBLE);
                chkChannel.setChecked(false);
            } else {
                chkChannel.setVisibility(View.GONE);
            }
        }
    }

}
