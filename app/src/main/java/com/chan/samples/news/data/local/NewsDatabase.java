package com.chan.samples.news.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;

/**
 * Created by chan on 1/29/18.
 */

@Database(entities = {Bookmark.class, ArticleResponse.class,
        Article.class},version = 1,exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase{

    public abstract NewsDAO daoAccess();
}
