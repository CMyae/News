package com.chan.samples.news.ui.main.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chan.samples.news.R;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.ui.views.SquareImageView;
import com.chan.samples.news.utils.DateUtils;

/**
 * Created by chan on 1/9/18.
 */

public class NewsGridAdapter extends BaseAdapter {

    private Context context;
    private ArticleResponse latestNews;

    public NewsGridAdapter(Context context, ArticleResponse latestNews) {
        this.context = context;
        this.latestNews = latestNews;
    }

    @Override
    public int getCount() {
        return latestNews.getArticles().size();
    }

    @Override
    public Object getItem(int position) {
        return latestNews.getArticles().get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder vHolder;

        if(convertView == null){

            vHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_row_grid_news,null);

            vHolder.imgNewThumbnail = convertView.findViewById(R.id.imgNewThumbnail);
            vHolder.tvLatestNewsTitle = convertView.findViewById(R.id.tv_latest_news_title);
            vHolder.tvDate = convertView.findViewById(R.id.tv_date);
            vHolder.progressBar = convertView.findViewById(R.id.progressBar);

            convertView.setTag(vHolder);

        }else{
            vHolder = (ViewHolder) convertView.getTag();
        }


        bindData(vHolder,position);

        return convertView;
    }



    public void bindData(final ViewHolder vHolder, int position){

        Article article = latestNews.getArticles().get(position);



        String title = article.getTitle();
        String timeStamp = article.getTimeStamp();

        if(title != null) vHolder.tvLatestNewsTitle.setText(title);
        if(timeStamp != null){
            timeStamp = DateUtils.format(timeStamp);
            vHolder.tvDate.setText(timeStamp);
        }

        Glide.with(context)
                .load(article.getUrlToImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        vHolder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        vHolder.progressBar.setVisibility(View.INVISIBLE);
                        return false;
                    }
                })
                .into(vHolder.imgNewThumbnail);


    }



    static class ViewHolder{
        SquareImageView imgNewThumbnail;
        TextView tvLatestNewsTitle,tvDate;
        ProgressBar progressBar;
    }

}
