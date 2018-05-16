package com.chan.samples.news.ui.main.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chan.samples.news.R;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.ui.main.MainActivity;
import com.chan.samples.news.ui.main.holders.NewsLoadingViewHolder;
import com.chan.samples.news.ui.main.holders.NewsViewHolder;

import java.util.List;


/**
 * Created by chan on 1/9/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int TYPE_LOADING = 0;
    private static final int TYPE_NEWS = 1;

    private Context context;
    List<List<ArticleResponse>> newsData;
    private OnItemClickListener listener;


    public NewsAdapter(Context context, List<List<ArticleResponse>> newsData) {
        this.context = context;
        this.newsData = newsData;
        this.listener = (OnItemClickListener) context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;

        if(viewType == TYPE_LOADING){
            view = LayoutInflater.from(context).inflate(R.layout.item_row_loading_news,parent,false);
            return new NewsLoadingViewHolder(view);
        }else{
            view = LayoutInflater.from(context).inflate(R.layout.item_row_news,parent,false);
            return new NewsViewHolder(view,listener);
        }
    }



    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == TYPE_LOADING){
            //loading view holder
            return;
        }

        ((NewsViewHolder)holder).bindView(position,newsData.get(position));

    }


    @Override
    public int getItemViewType(int position) {

        if(newsData.get(position) == null){
            return TYPE_LOADING;
        }

        return TYPE_NEWS;
    }


    @Override
    public int getItemCount() {
        return newsData.size();
    }


    public interface OnItemClickListener{
        void onSeeMoreClick(int position,int type);
        void onArticleItemClick(int responsePos,int articlePos);
        void onArticleItemClick(Article article);
    }


}
