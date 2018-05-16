package com.chan.samples.news.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chan.samples.news.R;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.utils.Constants;
import com.chan.samples.news.utils.DateUtils;
import com.chan.samples.news.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chan on 1/19/18.
 */

public class SearchNewsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.imgNewThumbnail) public ImageView imgNewThumbnail;
    @BindView(R.id.progress_loading_circle) public ProgressBar progressBar;
    @BindView(R.id.tv_news_title) public TextView tvNewsTitle;
    @BindView(R.id.tv_date) public TextView tvDate;
    @BindView(R.id.tvChannel) public TextView tvChannel;

    private Context context;
    private SearchAdapter.OnArticleClickListener listener;

    public SearchNewsViewHolder(View itemView, Context context, final SearchAdapter.OnArticleClickListener clickListener) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        this.context = context;
        this.listener = clickListener;

        imgNewThumbnail.setClipToOutline(true);
        imgNewThumbnail.setBackground(ViewUtils.getRoundRectDrawable(Constants.SMALL_CARD_RADIUS));

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) listener.onItemClick(getAdapterPosition());
            }
        });
    }



    public void bindView(Article article){

        String title = article.getTitle();
        String timeStamp = article.getTimeStamp();
        String sourceName = "";

        if(article.getSource() != null){
            sourceName = article.getSource().getName();
        }


        if(title != null) tvNewsTitle.setText(title);
        if(timeStamp != null){
            timeStamp = DateUtils.format(timeStamp);
            tvDate.setText(timeStamp);
        }
        if(sourceName != null) tvChannel.setText(sourceName);

        Glide.with(context)
                .load(article.getUrlToImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(imgNewThumbnail);
    }
}
