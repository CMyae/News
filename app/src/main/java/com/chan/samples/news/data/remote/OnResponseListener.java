package com.chan.samples.news.data.remote;

import com.chan.samples.news.data.models.ArticleResponse;

import java.util.List;

/**
 * Created by chan on 1/13/18.
 */

public interface OnResponseListener {

    void onArticleResponse(List<ArticleResponse> articles);

    void onMoreArticleResponse(List<ArticleResponse> newArticles);

    void onOfflineArticleResponse(List<List<ArticleResponse>> articles);

    void onError();
}
