package com.chan.samples.news.ui.search;


import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chan.samples.news.NewsApp;
import com.chan.samples.news.R;
import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.di.components.DaggerActivityComponent;
import com.chan.samples.news.di.modules.ActivityModule;
import com.chan.samples.news.ui.detail.NewsDetailActivity;
import com.chan.samples.news.ui.main.MainActivity;
import com.chan.samples.news.ui.views.EndlessScrollListener;
import com.chan.samples.news.utils.KeyboardUtils;
import com.chan.samples.news.utils.Util;
import com.chan.samples.news.utils.ViewUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class NewsSearchFragment extends Fragment implements SearchView.OnQueryTextListener,
            SearchContract.SearchView, SearchAdapter.OnArticleClickListener {


    @BindView(R.id.rvSearchNews) RecyclerView rvSearchNews;
    @BindView(R.id.searchView) SearchView searchView;
    @BindView(R.id.progress_loading_circle) ProgressBar progressBar;
    @BindView(R.id.tv_message) TextView tvMessage;
    @BindView(R.id.tvCancel) TextView tvCancel;
    @BindView(R.id.contentView) RelativeLayout contentView;

    @Inject DataManager dataManager;
    @Inject SearchContract.SearchMvpPresenter presenter;

    private ArticleResponse result = new ArticleResponse(new ArrayList<Article>());
    private SearchAdapter adapter;
    private String previousQuery = "";
    private int page;
    private int index = 1;
    private EndlessScrollListener listener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_search, container, false);
        ButterKnife.bind(this,view);

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(getContext(),this))
                .applicationComponent(NewsApp.getApp().getComponent())
                .build()
                .inject(this);

        searchView.setOnQueryTextListener(this);
        searchView.requestFocus();
        KeyboardUtils.showInputMethod(getContext(),searchView);

        //animate search view
        searchView.getLayoutParams().width = 0; //initial 0
        contentView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                contentView.getViewTreeObserver().removeOnPreDrawListener(this);
                animateSearchView();
                return true;
            }
        });

        progressBar.setVisibility(View.INVISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvSearchNews.setLayoutManager(layoutManager);
        rvSearchNews.setHasFixedSize(true);

        listener = new EndlessScrollListener(layoutManager);
        rvSearchNews.addOnScrollListener(listener);

        listener.setOnLoadMoreListener(new EndlessScrollListener.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(index <= page){
                    index++;
                    showLoadingSpinner();
                    presenter.loadMoreData(searchView.getQuery().toString(),String.valueOf(index));
                }else{
                    listener.stopLoading(); //no more data to load
                }
            }
        });

        adapter = new SearchAdapter(getContext(),result.getArticles(),this);
        rvSearchNews.setAdapter(adapter);

        return view;
    }


    private void animateSearchView(){
        int initWidth = 0;
        int desiredWidth = calculateDesiredWidth();

        ValueAnimator anim = ValueAnimator.ofInt(initWidth,desiredWidth);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                searchView.getLayoutParams().width = (int) animation.getAnimatedValue();
                searchView.requestLayout();
            }
        });
        anim.setDuration(180);
        anim.start();

    }


    private int calculateDesiredWidth(){
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) tvCancel.getLayoutParams();
        return ViewUtils.getScreenWidth(getContext()) - contentView.getPaddingLeft()
                - contentView.getPaddingRight() - tvCancel.getMeasuredWidth()
                - params.leftMargin;
    }



    @OnClick(R.id.tvCancel)
    public void onCancelClick(View view){
        ((MainActivity)getActivity()).hideSearchView();
    }



    @Override
    public boolean onQueryTextSubmit(String query) {
        KeyboardUtils.hideInputMethod(getContext(),searchView);
        return true;
    }



    @Override
    public boolean onQueryTextChange(String query) {
        listener.setLoading();
        presenter.onQueryTextChange(previousQuery.trim(),query.trim());
        previousQuery = query;
        return true;
    }



    @Override
    public void hideProgressCircle() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void showProgressCircle() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void showMessage(String message) {
        tvMessage.setText(message);
        tvMessage.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideMessage() {
        tvMessage.setVisibility(View.INVISIBLE);
    }


    @Override
    public void showArticleResults(ArticleResponse response) {

        page = Util.calculatePageCount(response.getTotalResult());
        page--;

        result.setStatus(response.getStatus());
        result.setTotalResult(response.getTotalResult());
        result.getArticles().clear();
        result.getArticles().addAll(response.getArticles());

        rvSearchNews.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void showMoreArticleResults(ArticleResponse response) {
        result.getArticles().addAll(response.getArticles());
        adapter.notifyDataSetChanged();
        listener.setLoading();
    }


    @Override
    public void showLoadingSpinner() {
        result.getArticles().add(null);
        adapter.notifyItemInserted(result.getArticles().size()-1);
    }

    @Override
    public void hideLoadingSpinner() {
        int index = result.getArticles().size()-1;
        result.getArticles().remove(index);
        adapter.notifyItemRemoved(index);
    }

    @Override
    public void hideArticleResults() {
        rvSearchNews.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onItemClick(int position) {
        Article article = result.getArticles().get(position);
        Intent intent = NewsDetailActivity.getActivityIntent(getContext(),article.getUrl());
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }

}
