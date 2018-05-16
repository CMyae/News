package com.chan.samples.news.ui.articles;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chan.samples.news.NewsApp;
import com.chan.samples.news.R;
import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.data.models.Source;
import com.chan.samples.news.data.models.SourceResponse;
import com.chan.samples.news.di.components.DaggerActivityComponent;
import com.chan.samples.news.di.modules.ActivityModule;
import com.chan.samples.news.ui.detail.NewsDetailActivity;
import com.chan.samples.news.ui.views.EndlessScrollListener;
import com.chan.samples.news.utils.NetworkUtils;
import com.chan.samples.news.utils.Util;
import com.chan.samples.news.utils.ViewUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ArticleListFragment extends Fragment implements ArticleContract.ArticleView, ArticleAdapter.OnItemClickListener {

    private static final String TAG = "ArticleListFragment";
    private static final String TYPE = "TYPE";
    private static final String RESPONSE_ID = "RESPONSE_ID";
    private static final String BOOKMARK = "BOOKMARK";

    @BindView(R.id.rv_articles) RecyclerView rv_articles;
    @BindView(R.id.progress_loading_circle) ProgressBar progressBar;

    @Inject DataManager dataManager;
    @Inject ArticleContract.ArticleMvpPresenter presenter;

    private ArticleAdapter adapter;
    private int page;
    private int index = 1;
    private boolean isLoadMore;
    private Bookmark bookmark;
    private ArticleResponse article = new ArticleResponse(new ArrayList<Article>());
    private EndlessScrollListener listener;
    private String sources;

    public static ArticleListFragment getInstance(long id,int type,Bookmark bookmark){
        ArticleListFragment fragment = new ArticleListFragment();

        Bundle bdn = new Bundle();
        bdn.putLong(RESPONSE_ID,id);
        bdn.putInt(TYPE,type);
        bdn.putParcelable(BOOKMARK,bookmark);
        fragment.setArguments(bdn);

        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(getContext(),this))
                .applicationComponent(NewsApp.getApp().getComponent())
                .build()
                .inject(this);

        Bundle bdn = getArguments();

        if(bdn != null){
            article.setId(bdn.getLong(RESPONSE_ID));
            article.setType(bdn.getInt(TYPE));
            bookmark = bdn.getParcelable(BOOKMARK);
        }

        presenter.setArticleResponseId(article.getId());

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        ButterKnife.bind(this,view);


        progressBar.setVisibility(View.VISIBLE);
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                //place center y
                progressBar.setY(ViewUtils.getScreenHeight(getContext())/2 - progressBar.getHeight()/2 -
                        ViewUtils.dpToPx(getContext(),64));
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv_articles.setLayoutManager(layoutManager);
        rv_articles.setHasFixedSize(true);
        adapter = new ArticleAdapter(getContext(),article.getArticles(),this);
        rv_articles.setAdapter(adapter);

        listener = new EndlessScrollListener(layoutManager);
        listener.setOnLoadMoreListener(new EndlessScrollListener.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isLoadMore) return;
                if(index <= page){
                    index++;
                    showLoadingSpinner();
                    presenter.loadMoreArticles(bookmark,index,article.getType());
                }else{
                    listener.stopLoading(); //no more data to load
                }
            }
        });

        rv_articles.addOnScrollListener(listener);


        //check network connection
        if(NetworkUtils.isNetworkConnected(getContext())){

            if(bookmark.getType() == Bookmark.TYPE_SOURCE){
                presenter.loadArticles(bookmark,index,article.getType());

            }else if(bookmark.getType() == Bookmark.TYPE_CATEGORY){

                Call<SourceResponse> call = dataManager.getRemoteController().getRemoteApi()
                        .getSourcesByCategory(bookmark.getName());
                call.enqueue(new Callback<SourceResponse>() {
                    @Override
                    public void onResponse(Call<SourceResponse> call, Response<SourceResponse> response) {
                        sources = Util.getSourcesName(response.body());
                        presenter.initialize(sources);
                        presenter.loadArticles(bookmark,index,article.getType());
                    }
                    @Override
                    public void onFailure(Call<SourceResponse> call, Throwable t) {}
                });
            }

        }else{
            presenter.loadOfflineArticles();
        }

        return view;
    }


    @Override
    public void showArticleList(ArticleResponse response) {
        page = Util.calculatePageCount(response.getTotalResult());
        page--;

        article.setStatus(response.getStatus());
        article.setTotalResult(response.getTotalResult());
        article.getArticles().clear();
        article.getArticles().addAll(response.getArticles());

        adapter.notifyDataSetChanged();
    }


    @Override
    public void showMoreArticleList(ArticleResponse moreResponse) {
        article.getArticles().addAll(moreResponse.getArticles());
        adapter.notifyDataSetChanged();
        listener.setLoading();
    }


    @Override
    public void showOfflineArticles(List<Article> offlineArticle) {
        article.getArticles().clear();
        article.getArticles().addAll(offlineArticle);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void hideLoadingCircle() {
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void showLoadingSpinner() {
        article.getArticles().add(null);
        adapter.notifyItemInserted(article.getArticles().size()-1);
    }


    @Override
    public void hideLoadingSpinner() {
        int index = article.getArticles().size()-1;
        article.getArticles().remove(index);
        adapter.notifyItemRemoved(index);
    }


    @Override
    public void resetLoadingStatus() {
        isLoadMore = true;
        index = 1;
        listener.setLoading();
    }

    @Override
    public void onItemClicked(int position) {
        Article currentArticle = article.getArticles().get(position);
        Intent intent = NewsDetailActivity.getActivityIntent(getContext(),currentArticle.getUrl());
        getContext().startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }
}
