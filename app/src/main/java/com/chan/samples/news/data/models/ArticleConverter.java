package com.chan.samples.news.data.models;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by chan on 1/29/18.
 */

public class ArticleConverter {

    private static Gson gson = new Gson();

    @TypeConverter
    public static List<Article> convertToArticleList(String data){
        if(data == null) return Collections.emptyList();

        Type type = new TypeToken<Article>() {}.getType();
        return gson.fromJson(data,type);
    }

    @TypeConverter
    public static String convertArticleToString(List<Article> articles){
        return gson.toJson(articles);
    }
}
