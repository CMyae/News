package com.chan.samples.news.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chan.samples.news.R;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.ui.main.holders.NewsLoadingViewHolder;

import java.util.List;

/**
 * Created by chan on 1/19/18.
 */

public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_NEWS = 1;

    private Context context;
    private List<Article> articles;
    private OnArticleClickListener listener;

    public SearchAdapter(Context context,List<Article> searchArticles,OnArticleClickListener listener) {
        this.context = context;
        articles = searchArticles;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if(viewType == TYPE_LOADING){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_loading_news,parent,false);
            return new NewsLoadingViewHolder(view);
        }else{
             view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_search_news,parent,false);
            return new SearchNewsViewHolder(view,context,listener);
        }

    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == TYPE_LOADING) return;

        ((SearchNewsViewHolder)holder).bindView(articles.get(position));
    }



    @Override
    public int getItemCount() {
        return articles.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(articles.get(position) == null){
            return TYPE_LOADING;
        }else{
            return TYPE_NEWS;
        }
    }


    interface OnArticleClickListener{
        void onItemClick(int position);
    }
}
