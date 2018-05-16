package com.chan.samples.news.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;

import java.util.List;

/**
 * Created by chan on 1/29/18.
 */

@Dao
public interface NewsDAO {

    @Insert
    void insertBookmark(Bookmark bookmark);

    @Insert
    void insertArticleResponse(ArticleResponse response);


    @Insert
    void insertArticles(List<Article> articles);


    @Query("select * from article_response where bookmark_id =:id and type = " + ArticleResponse.TYPE_HEADLINE)
    ArticleResponse getHeadlineArticleByBookmark(long id);


    @Query("select * from article_response where bookmark_id =:id and type = " + ArticleResponse.TYPE_LATEST)
    ArticleResponse getLatestArticleByBookmark(long id);


    @Query("select * from article_response where type = " + ArticleResponse.TYPE_HEADLINE)
    List<ArticleResponse> getOfflineHeadlineResponse();


    @Query("select * from article_response where type = " + ArticleResponse.TYPE_LATEST)
    List<ArticleResponse> getOfflineLatestResponse();


    @Query("select * from article where article_response_id = :article_response_id LIMIT :limit")
    List<Article> getArticlesWithLimit(long article_response_id,int limit);


    @Query("select * from article where article_response_id = :responseId")
    List<Article> getArticlesByResponseId(long responseId);


    @Query("delete from article where article_response_id = :responseId")
    void deleteArticlesByResponseId(long responseId);


    @Query("delete from article_response")
    void deleteAllArticleResponse();


    @Query("delete from article")
    void deleteAllArticles();


}
