package com.chan.samples.news.data.remote;

import com.chan.samples.news.data.models.ArticleResponse;

/**
 * Created by chan on 1/25/18.
 */

public interface OnSearchResponseListener {

    void onArticleResponse(ArticleResponse response);
    void onMoreArticleResponse(ArticleResponse response);
    void onCancel();
    void onError();
}
