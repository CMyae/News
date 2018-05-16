package com.chan.samples.news.ui.main.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.chan.samples.news.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chan on 1/18/18.
 */

public class NewsLoadingViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.progress_loading_bar) public ProgressBar progressBar;

    public NewsLoadingViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

}
