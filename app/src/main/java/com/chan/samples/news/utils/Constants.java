package com.chan.samples.news.utils;


import com.chan.samples.news.data.models.Bookmark;

import java.util.Arrays;
import java.util.List;

/**
 * Created by chan on 1/10/18.
 */

public class Constants {

    public static final String API_KEY = "81cbc5bda75c47e0b3a1350c22cf64a6";
    public static final String BASE_URL = "https://newsapi.org/v2/";
    public static final String HEADER_KEY = "X-Api-Key";

    public static final String DB_NAME = "news-db";

    public static final String PREF_BOOKMARK = "BOOKMARK";
    public static final String PREF_DEF = "DEFAULT";
    public static final String PREF_CONFIG = "CONFIG";


    public static final String MAIN_HEADLINE_NEWS_COUNT = "5";
    public static final String MAIN_LATEST_NEWS_COUNT = "4";
    public static final String MAX_SEARCH_ITEM = "10";
    public static int MAX_ARTICLE_SIZE = 10;
    public static int SMALL_CARD_RADIUS = 10;


    public static final List<Bookmark> categories = Arrays.asList(

            new Bookmark(0,"general",Bookmark.TYPE_CATEGORY),
            new Bookmark(1,"business",Bookmark.TYPE_CATEGORY),
            new Bookmark(2,"entertainment",Bookmark.TYPE_CATEGORY),
            new Bookmark(3,"health",Bookmark.TYPE_CATEGORY),
            new Bookmark(4,"science",Bookmark.TYPE_CATEGORY),
            new Bookmark(5,"sports",Bookmark.TYPE_CATEGORY),
            new Bookmark(6,"technology",Bookmark.TYPE_CATEGORY)
    );

}
