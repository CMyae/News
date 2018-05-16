package com.chan.samples.news.data.remote;

import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;

import java.util.List;

/**
 * Created by chan on 2/2/18.
 */

public interface OnArticleListener {

    void onArticleLoaded(ArticleResponse response);
    void onMoreArticleLoaded(ArticleResponse response);
    void onOfflineArticleLoaded(List<Article> articles);
    void onError();
}
