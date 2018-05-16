package com.chan.samples.news.data.remote;

import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.SourceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by chan on 1/13/18.
 */

public interface APIEndpoint {


    @GET("sources?language=en")
    Call<SourceResponse> getAllSources();


    @GET("sources?language=en")
    Call<SourceResponse> getSourcesByCategory(@Query("category") String category);



    @GET("top-headlines")
    Call<ArticleResponse> getHeadlineArticleByCategory(@Query("category") String category,
                                                       @Query("pageSize") String pageSize);


    @GET("everything")
    Call<ArticleResponse> getArticleBySources(@Query("sources") String sources,
                                              @Query("pageSize") String pageSize);


    @GET("everything")
    Call<ArticleResponse> getArticleBySources(@Query("sources") String sources,
                                              @Query("pageSize") int pageSize,
                                              @Query("page") int page);


    @GET("top-headlines")
    Call<ArticleResponse> getHeadlineArticleBySource(@Query("sources") String source,
                                                     @Query("pageSize") String pageSize);


    @GET("everything?language=en")
    Call<ArticleResponse> getLatestArticleBySource(@Query("sources") String source,
                                                   @Query("pageSize") String pageSize);


    @GET("top-headlines")
    Call<ArticleResponse> getHeadlineArticleByCategory(@Query("category") String category,
                                                        @Query("pageSize") int pageSize,
                                                        @Query("page") int page);


    @GET("top-headlines")
    Call<ArticleResponse> getHeadlineArticleBySource(@Query("sources") String sources,
                                                      @Query("pageSize") int pageSize,
                                                      @Query("page") int page);


    @GET("top-headlines")
    Call<ArticleResponse> getLatestArticleByCategory(@Query("category") String category,
                                                      @Query("pageSize") int pageSize,
                                                      @Query("page") int page);


    @GET("everything?language=en")
    Call<ArticleResponse> getLatestArticleBySource(@Query("sources") String sources,
                                                    @Query("pageSize") int pageSize,
                                                    @Query("page") int page);


    @GET("everything?language=en&sortBy=popularity")
    Call<ArticleResponse> getNewsByQuery(@Query("q") String query,
                                         @Query("pageSize") String pageSize,
                                         @Query("page") String page);

}
