package com.chan.samples.news.ui.articles;

import android.content.Context;
import android.view.View;

import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.ui.search.SearchNewsViewHolder;

/**
 * Created by chan on 2/1/18.
 */

public class ArticleViewHolder extends SearchNewsViewHolder {

    private ArticleAdapter.OnItemClickListener listener;

    public ArticleViewHolder(View itemView, Context context, ArticleAdapter.OnItemClickListener clickListener) {
        super(itemView, context, null);

        this.listener = clickListener;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(getAdapterPosition());
            }
        });
    }

    @Override
    public void bindView(Article article) {
        super.bindView(article);
    }


}
