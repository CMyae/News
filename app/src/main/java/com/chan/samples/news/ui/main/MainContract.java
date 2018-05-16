package com.chan.samples.news.ui.main;

import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.ui.base.MvpView;

import java.util.List;

/**
 * Created by chan on 1/18/18.
 */

public interface MainContract {

    interface MainView extends MvpView{
        void showArticlesResponse(List<ArticleResponse> articles);
        void showRefreshArticlesResponse(List<ArticleResponse> articles);
        void showMoreArticleResponse(List<ArticleResponse> newArticles);
        void showOfflineArticles(List<List<ArticleResponse>> articles);
        void showLoadingSpinner();
        void hideLoadingSpinner();
        void hideRefreshIndicator();
        void showSearchView();
        void hideSearchView();
        void resetLoadingStatus();
        void showEditBottomSheet();
        void showLoginBottomSheet();
        void showLogoutBottomSheet();
        void hideBottomSheet();
        void showLoginMenu();
        void hideLoginMenu();
        void showProfileIcon();
        void hideProfileIcon();
        void onLoginSuccess();
        void onLogoutSuccess();
        void onBookmarkEdited();
    }


    interface MainMvpPresenter{
        void loadArticles(Bookmark bookmark);
        void loadOfflineArticles();
        void loadMoreArticles(Bookmark bookmark);
        void onRefreshArticles(Bookmark bookmark);
        void onSearchMenuClick();
        void onEditMenuClick();
        void onLoginMenuClick();
    }
}
