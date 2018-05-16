package com.chan.samples.news.ui.articles;

import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;

import java.util.List;

/**
 * Created by chan on 2/1/18.
 */

public interface ArticleContract {

    interface ArticleView{
        void showMessage(String message);
        void showArticleList(ArticleResponse articleResponse);
        void showMoreArticleList(ArticleResponse moreResponse);
        void showOfflineArticles(List<Article> articles);
        void hideLoadingCircle();
        void showLoadingSpinner();
        void hideLoadingSpinner();
        void resetLoadingStatus();
    }

    interface ArticleMvpPresenter{
        void loadArticles(Bookmark bookmark,int page,int type);
        void loadMoreArticles(Bookmark bookmark,int page,int type);
        void setArticleResponseId(long responseId);
        void loadOfflineArticles();
        void initialize(String sources);
    }
}
