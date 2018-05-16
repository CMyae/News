package com.chan.samples.news.data.remote;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.data.models.Source;
import com.chan.samples.news.data.models.SourceResponse;
import com.chan.samples.news.utils.Constants;
import com.chan.samples.news.utils.Util;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by chan on 1/13/18.
 */

public class RemoteController {

    private static final String TAG = "RemoteController";

    private APIEndpoint remoteApi;
    private Handler handler;
    private Call<ArticleResponse> searchCall;
    private static long index;

    @Inject
    public RemoteController(APIEndpoint apiEndpoint) {
        this.remoteApi = apiEndpoint;
        handler = new Handler(Looper.getMainLooper());
    }


    public void loadHeadlineAndLatestNewsByBookmark(final Bookmark bookmark, final boolean isLoadMore, final OnResponseListener listener) {

        final List<ArticleResponse> responses = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Call<ArticleResponse> headlineArticle;
                    Call<ArticleResponse> latestArticle;

                    if (bookmark.getType() == Bookmark.TYPE_CATEGORY) {
                        Call<SourceResponse> response = remoteApi.getSourcesByCategory(bookmark.getName());
                        SourceResponse sourceResponse = response.execute().body();
                        String sourceName = Util.getSourcesName(sourceResponse);
                        headlineArticle = remoteApi.getHeadlineArticleByCategory(bookmark.getName(), Constants.MAIN_HEADLINE_NEWS_COUNT);
                        latestArticle = remoteApi.getArticleBySources(sourceName, Constants.MAIN_LATEST_NEWS_COUNT);

                    } else {
                        headlineArticle = remoteApi.getHeadlineArticleBySource(bookmark.getName(), Constants.MAIN_HEADLINE_NEWS_COUNT);
                        latestArticle = remoteApi.getLatestArticleBySource(bookmark.getName(), Constants.MAIN_LATEST_NEWS_COUNT);
                    }
                    ArticleResponse headlineResponse = headlineArticle.execute().body();
                    ArticleResponse latestResponse = latestArticle.execute().body();

                    headlineResponse.setId(++index);
                    headlineResponse.setBookmark_id(bookmark.getBookmark_id());
                    headlineResponse.setType(ArticleResponse.TYPE_HEADLINE);
                    headlineResponse.setArticleResponseId();

                    latestResponse.setId(++index);
                    latestResponse.setBookmark_id(bookmark.getBookmark_id());
                    latestResponse.setType(ArticleResponse.TYPE_LATEST);
                    latestResponse.setArticleResponseId();

                    responses.add(headlineResponse);
                    responses.add(latestResponse);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isLoadMore) {
                                listener.onMoreArticleResponse(responses);
                            } else {
                                Log.i("TAG", "article fetched");
                                listener.onArticleResponse(responses);
                            }
                        }
                    });
                } catch (Exception e) {
                    sendErrorMessage(listener);
                    Log.i("TAG",e.getMessage());
                }
            }
        }).start();

    }


    public void loadNewsByQuery(final String query, String page, final boolean isLoadMore, final OnSearchResponseListener listener) {

        //cancel previous request
        if (searchCall != null) searchCall.cancel();

        searchCall = remoteApi.getNewsByQuery(query, Constants.MAX_SEARCH_ITEM, page);
        searchCall.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if (isLoadMore) {
                    listener.onMoreArticleResponse(response.body());
                } else {
                    listener.onArticleResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                if (call.isCanceled()) {
                    listener.onCancel();
                } else {
                    listener.onError();
                }
            }
        });

    }


    public void sendErrorMessage(final OnResponseListener listener) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                listener.onError();
            }
        });
    }


    public void loadHeadlineArticlesByBookmark(Bookmark bookmark, int page, final boolean isLoadMore, final OnArticleListener listener) {
        Call<ArticleResponse> call;
        if (bookmark.getType() == Bookmark.TYPE_CATEGORY) {
            call = remoteApi.getHeadlineArticleByCategory(bookmark.getName(), Constants.MAX_ARTICLE_SIZE, page);
        } else {
            call = remoteApi.getHeadlineArticleBySource(bookmark.getName(), Constants.MAX_ARTICLE_SIZE, page);
        }
        Log.i(TAG, "article loaded...");

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if (isLoadMore) {
                    listener.onMoreArticleLoaded(response.body());
                } else {
                    listener.onArticleLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                listener.onError();
            }
        });
    }


    public void loadLatestArticlesByBookmark(Bookmark bookmark, int page, final boolean isLoadMore, final OnArticleListener listener) {
        Call<ArticleResponse> call = remoteApi.getLatestArticleBySource(bookmark.getName(), Constants.MAX_ARTICLE_SIZE, page);

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if (isLoadMore) {
                    listener.onMoreArticleLoaded(response.body());
                } else {
                    listener.onArticleLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                listener.onError();
            }
        });

    }


    public void loadLatestArticlesBySources(String sources, int page, final boolean isLoadMore, final OnArticleListener listener) {
        Call<ArticleResponse> call = remoteApi.getArticleBySources(sources, Constants.MAX_ARTICLE_SIZE, page);

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                if (isLoadMore) {
                    listener.onMoreArticleLoaded(response.body());
                } else {
                    listener.onArticleLoaded(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                listener.onError();
            }
        });

    }


    public APIEndpoint getRemoteApi() {
        return remoteApi;
    }
}
