package com.chan.samples.news.ui.main.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chan.samples.news.R;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.ui.main.MainActivity;
import com.chan.samples.news.ui.main.adapters.NewsAdapter;
import com.chan.samples.news.ui.main.adapters.NewsGridAdapter;
import com.chan.samples.news.ui.main.adapters.NewsPagerAdapter;
import com.chan.samples.news.ui.views.CustomGridView;
import com.chan.samples.news.ui.views.ExpandViewPager;
import com.chan.samples.news.utils.ViewUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chan on 1/9/18.
 */

public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.gridView) public CustomGridView gridView;
    @BindView(R.id.viewPager) public ExpandViewPager viewPager;
    @BindView(R.id.tvBreakingNew) TextView tvHeadlineNew;
    @BindView(R.id.tvLatestNews) TextView tvLatestNew;
    @BindView(R.id.tvHeadlineNewsSeeMore) TextView tvHeadlineNewsSeeMore;
    @BindView(R.id.tvLatestNewsSeeMore) TextView tvLatestNewsSeeMore;

    private Context context;
    private NewsAdapter.OnItemClickListener listener;


    public NewsViewHolder(View itemView, final NewsAdapter.OnItemClickListener listener) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        this.listener = listener;
        context = itemView.getContext();

        viewPager.setPageMargin(ViewUtils.dpToPx(context,20));

        tvHeadlineNewsSeeMore.setOnClickListener(this);
        tvLatestNewsSeeMore.setOnClickListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listener.onArticleItemClick(getAdapterPosition(),position);
            }
        });
    }



    public void bindView(int position, List<ArticleResponse> response){

        ArticleResponse headlineNews = response.get(0);
        ArticleResponse latestNews = response.get(1);

        NewsPagerAdapter pagerAdapter = new NewsPagerAdapter(
                ((MainActivity)context).getSupportFragmentManager(),headlineNews);

        viewPager.setId(position+1);
        viewPager.setAdapter(pagerAdapter);
        viewPager.playCarouselAnimation();

        NewsGridAdapter gridAdapter = new NewsGridAdapter(context,latestNews);
        gridView.setAdapter(gridAdapter);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.tvHeadlineNewsSeeMore){
            listener.onSeeMoreClick(getAdapterPosition(),ArticleResponse.TYPE_HEADLINE);
        }

        if(v.getId() == R.id.tvLatestNewsSeeMore){
            listener.onSeeMoreClick(getAdapterPosition(),ArticleResponse.TYPE_LATEST);
        }
    }


}
