package com.chan.samples.news.ui.search;

import com.chan.samples.news.data.models.ArticleResponse;

/**
 * Created by chan on 1/26/18.
 */

public interface SearchContract {

    interface SearchView{
        void hideProgressCircle();
        void showProgressCircle();
        void showMessage(String message);
        void hideMessage();
        void showArticleResults(ArticleResponse result);
        void showMoreArticleResults(ArticleResponse result);
        void showLoadingSpinner();
        void hideLoadingSpinner();
        void hideArticleResults();
    }

    interface SearchMvpPresenter{
        void onQueryTextChange(String previousQuery,String query);
        void loadMoreData(String query,String page);
    }
}
