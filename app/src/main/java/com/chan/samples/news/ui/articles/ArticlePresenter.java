package com.chan.samples.news.ui.articles;

import android.content.Context;

import com.chan.samples.news.R;
import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.data.remote.OnArticleListener;
import com.chan.samples.news.di.ActivityContext;
import com.chan.samples.news.di.View;
import com.chan.samples.news.utils.NetworkUtils;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by chan on 2/1/18.
 */

public class ArticlePresenter implements ArticleContract.ArticleMvpPresenter {

    private ArticleContract.ArticleView view;
    private DataManager dataManager;
    private OnArticleListener listener;
    private Context context;
    private long responseId;
    private String sources;

    @Inject
    public ArticlePresenter(@ActivityContext final Context context, @View Object v, final DataManager dataManager) {
        this.context = context;
        this.view = (ArticleContract.ArticleView) v;
        this.dataManager = dataManager;

        listener = new OnArticleListener() {
            @Override
            public void onArticleLoaded(ArticleResponse article) {
                if(article == null){
                    view.showMessage(context.getString(R.string.message_error_loading));
                    return;
                }

                dataManager.storeArticleByResponseInLocal(responseId,article.getArticles());

                view.resetLoadingStatus();
                view.hideLoadingCircle();
                view.showArticleList(article);
            }


            @Override
            public void onMoreArticleLoaded(ArticleResponse article) {
                if(article == null){
                    view.showMessage(context.getString(R.string.message_error_loading));
                    return;
                }
                dataManager.storeMoreArticleByResponseInLocal(responseId,article.getArticles());
                view.hideLoadingSpinner();
                view.showMoreArticleList(article);
            }


            @Override
            public void onOfflineArticleLoaded(List<Article> articles) {
                if(articles.size() == 0) return;

                view.hideLoadingCircle();
                view.showOfflineArticles(articles);
                view.showMessage(context.getString(R.string.message_error_connection));
            }

            @Override
            public void onError() {
                view.showMessage(context.getString(R.string.message_error_loading));
            }
        };
    }


    @Override
    public void setArticleResponseId(long responseId){
        this.responseId = responseId;
    }



    @Override
    public void loadArticles(Bookmark bookmark, int page, int type) {

        if(!NetworkUtils.isNetworkConnected(context)){
            view.showMessage(context.getString(R.string.message_error_connection));
            return;
        }

        if(type == ArticleResponse.TYPE_HEADLINE){
            dataManager.getRemoteHeadlineArticlesByBookmark(false,bookmark,page,listener);
        }else{
            dataManager.getRemoteLatestArticlesByBookmark(sources,false,bookmark,page,listener);
        }
    }



    @Override
    public void loadMoreArticles(Bookmark bookmark, int page, int type) {

        if(!NetworkUtils.isNetworkConnected(context)){
            view.showMessage(context.getString(R.string.message_error_connection));
            return;
        }
        if(type == ArticleResponse.TYPE_HEADLINE){
            dataManager.getRemoteHeadlineArticlesByBookmark(true,bookmark,page,listener);
        }else{
            dataManager.getRemoteLatestArticlesByBookmark(sources,true,bookmark,page,listener);
        }
    }


    @Override
    public void loadOfflineArticles() {
        dataManager.getLocalArticleByResponseId(responseId,listener);
    }

    @Override
    public void initialize(String sources) {
        this.sources = sources;
    }

}
