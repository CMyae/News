package com.chan.samples.news.ui.search;

import android.content.Context;

import com.chan.samples.news.R;
import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.remote.OnSearchResponseListener;
import com.chan.samples.news.di.ActivityContext;
import com.chan.samples.news.di.View;
import com.chan.samples.news.utils.NetworkUtils;

import javax.inject.Inject;

/**
 * Created by chan on 1/26/18.
 */

public class SearchPresenter implements SearchContract.SearchMvpPresenter {

    private Context context;
    private SearchContract.SearchView view;
    private OnSearchResponseListener listener;

    private DataManager dataManager;

    private String query = "";
    private String message_no_match;
    private String message_no_connection;

    @Inject
    public SearchPresenter(@ActivityContext Context context, @View Object searchView, DataManager dataManager) {

        this.context = context;
        view = (SearchContract.SearchView) searchView;
        this.dataManager = dataManager;

        message_no_connection = context.getString(R.string.message_error_connection);
        message_no_match = context.getString(R.string.message_no_articles);

        this.listener = new OnSearchResponseListener() {
            @Override
            public void onArticleResponse(ArticleResponse response) {
                if(response == null) return;
                if(response.getArticles() == null) return;
                //make sure not to show results if query is empty
                if(query.isEmpty()) return;

                if(response.getArticles().size() == 0){
                    view.hideProgressCircle();
                    view.showMessage(message_no_match);
                }else{
                    view.hideMessage();
                    view.hideProgressCircle();
                    view.showArticleResults(response);
                }
            }

            @Override
            public void onMoreArticleResponse(ArticleResponse response) {
                if(response == null) return;
                if(response.getArticles() == null) return;
                view.hideLoadingSpinner();
                view.showMoreArticleResults(response);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError() {
                view.hideProgressCircle();
                view.showMessage(message_no_connection);
            }
        };

    }


    @Override
    public void onQueryTextChange(String previousQuery,String query) {
        this.query = query;

        if(!NetworkUtils.isNetworkConnected(context)){
            view.showMessage(message_no_connection);
            return;
        }

        //check if query is empty
        if(query.isEmpty()){
            view.hideProgressCircle();
            view.showMessage(message_no_match);
            view.hideArticleResults();
            return;
        }

        //if last query is equal to previous query
        if(previousQuery.equalsIgnoreCase(query)) return;

        view.showProgressCircle();
        view.hideMessage();
        view.hideArticleResults();
        dataManager.searchNewsByQuery(query,"1",false,listener);

    }

    @Override
    public void loadMoreData(String query,String page){
        dataManager.searchNewsByQuery(query,page,true,listener);
    }

}
