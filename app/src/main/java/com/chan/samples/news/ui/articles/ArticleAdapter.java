package com.chan.samples.news.ui.articles;

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
 * Created by chan on 2/1/18.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_ARTICLE = 1;

    private Context context;
    private List<Article> articles;
    private OnItemClickListener listener;


    public ArticleAdapter(Context context, List<Article> articles,OnItemClickListener listener) {
        this.context = context;
        this.articles = articles;
        this.listener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == TYPE_LOADING){
            view = LayoutInflater.from(context).inflate(R.layout.item_row_loading_news,parent,false);
            return new NewsLoadingViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.item_row_article,parent,false);
            return new ArticleViewHolder(view,context,listener);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_LOADING){
            return;
        }

        ((ArticleViewHolder)holder).bindView(articles.get(position));
    }


    @Override
    public int getItemCount() {
        return articles.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(articles.get(position) == null){
            return TYPE_LOADING;
        }
        return TYPE_ARTICLE;
    }


    public interface OnItemClickListener{
        void onItemClicked(int position);
    }
}
