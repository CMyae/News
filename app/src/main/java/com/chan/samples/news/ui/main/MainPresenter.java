package com.chan.samples.news.ui.main;

import android.content.Context;

import com.chan.samples.news.R;
import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.data.models.Source;
import com.chan.samples.news.data.remote.OnResponseListener;
import com.chan.samples.news.di.ActivityContext;
import com.chan.samples.news.utils.NetworkUtils;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by chan on 1/18/18.
 */

public class MainPresenter implements MainContract.MainMvpPresenter {

    private Context context;
    private MainContract.MainView mainView;
    private DataManager dataManager;
    private OnResponseListener listener;
    private boolean isRefresh;


    @Inject
    public MainPresenter(@ActivityContext final Context context, final DataManager dataManager) {

        this.context = context;
        this.mainView = (MainContract.MainView) context;
        this.dataManager = dataManager;

        listener = new OnResponseListener() {
            @Override
            public void onArticleResponse(final List<ArticleResponse> articles) {

                dataManager.storeArticleInLocal(articles);

                mainView.hideRefreshIndicator();
                mainView.resetLoadingStatus();

                if(isRefresh){
                    mainView.showRefreshArticlesResponse(articles);
                }else{
                    mainView.showArticlesResponse(articles);
                }
            }

            @Override
            public void onOfflineArticleResponse(List<List<ArticleResponse>> articles) {
                mainView.showOfflineArticles(articles);
            }

            @Override
            public void onMoreArticleResponse(final List<ArticleResponse> newArticles) {
                dataManager.storeMoreArticleInLocal(newArticles);

                mainView.hideLoadingSpinner();
                mainView.showMoreArticleResponse(newArticles);
            }

            @Override
            public void onError() {
                mainView.showErrorMessage(context.getString(R.string.message_error_loading));
                mainView.hideRefreshIndicator();
            }
        };
    }



    @Override
    public void loadArticles(Bookmark bookmark) {

        if(!NetworkUtils.isNetworkConnected(context)){
            mainView.showErrorMessage(context.getString(R.string.message_error_connection));
            mainView.hideRefreshIndicator();
            return;
        }

        isRefresh = false;
        if(bookmark.getType() == Bookmark.TYPE_CATEGORY){
            dataManager.getRemoteNewsByBookmark(bookmark,false,listener);
        }else{
            dataManager.getRemoteNewsByBookmark(bookmark,false,listener);
        }
    }


    @Override
    public void loadOfflineArticles() {
        dataManager.getLocalHeadlineAndLatestArticle(listener);
    }


    @Override
    public void loadMoreArticles(Bookmark bookmark) {

        if(!NetworkUtils.isNetworkConnected(context)){
            mainView.showErrorMessage(context.getString(R.string.message_error_connection));
            mainView.hideRefreshIndicator();
            return;
        }

        isRefresh = false;
        if(bookmark.getType() == Bookmark.TYPE_CATEGORY){
            dataManager.getRemoteNewsByBookmark(bookmark,true,listener);
        }else{
            dataManager.getRemoteNewsByBookmark(bookmark,true,listener);
        }
    }


    @Override
    public void onRefreshArticles(Bookmark bookmark) {

        if(!NetworkUtils.isNetworkConnected(context)){
            mainView.showErrorMessage(context.getString(R.string.message_error_connection));
            mainView.hideRefreshIndicator();
            return;
        }

        isRefresh = true;
        if(bookmark.getType() == Bookmark.TYPE_CATEGORY){
            dataManager.getRemoteNewsByBookmark(bookmark,false,listener);
        }else{
            dataManager.getRemoteNewsByBookmark(bookmark,false,listener);
        }
    }


    @Override
    public void onEditMenuClick() {
        mainView.showEditBottomSheet();
    }


    @Override
    public void onLoginMenuClick() {
        mainView.showLoginBottomSheet();
    }


    @Override
    public void onSearchMenuClick() {
        mainView.showSearchView();
    }

}
