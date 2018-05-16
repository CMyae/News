package com.chan.samples.news.data.local;

import android.content.SharedPreferences;

import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.utils.Constants;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by chan on 2/14/18.
 */

public class PrefHelper {

    private static final String FIRST_TIME = "FIRST_TIME";

    private SharedPreferences prefConfig;
    private SharedPreferences prefBookmark;
    private SharedPreferences prefDef;
    private SharedPreferences.Editor bookmarkEditor;
    private SharedPreferences.Editor defEditor;
    private SharedPreferences.Editor configEditor;
    private Gson gson;


    @Inject
    public PrefHelper(@Named(Constants.PREF_BOOKMARK) SharedPreferences prefBookmark,
                      @Named(Constants.PREF_DEF) SharedPreferences prefDef,
                      @Named(Constants.PREF_CONFIG) SharedPreferences prefConfig,
                      Gson gson) {

        this.prefBookmark = prefBookmark;
        this.prefDef = prefDef;
        this.prefConfig = prefConfig;
        this.bookmarkEditor = prefBookmark.edit();
        this.defEditor = prefDef.edit();
        this.configEditor = prefConfig.edit();
        this.gson = gson;
    }


    public void saveDefaultCategories(List<Bookmark> categories){
        for (int i = 0; i < categories.size(); i++) {
            String json = gson.toJson(categories.get(i));
            defEditor.putString(Integer.toString(i),json);
        }
        defEditor.apply();
    }


    public void saveDefaultChannels(List<Bookmark> channel){
        int index = prefDef.getAll().size();

        for (int i = 0; i < channel.size(); i++) {

            int id = index++;
            Bookmark b = channel.get(i);
            b.setBookmark_id(id);

            String json = gson.toJson(b);
            defEditor.putString(Integer.toString(id),json);
        }
        defEditor.apply();
    }


    public Bookmark[] getAllBookmarks(){
        int count = getAllBookmarkCount();
        Bookmark[] bookmarks = new Bookmark[count];

        for(int i=0; i<count; i++){
            String value = prefDef.getString(Integer.toString(i),"");
            Bookmark b = gson.fromJson(value,Bookmark.class);
            if(b != null) bookmarks[i] = b;
        }
        return bookmarks;
    }


    public void saveUserBookmark(List<Bookmark> bookmarks){
        bookmarkEditor.clear(); //remove all old data
        bookmarkEditor.apply();

        for (int i = 0; i < bookmarks.size(); i++) {
            Bookmark b = bookmarks.get(i);
            String json = gson.toJson(b);
            bookmarkEditor.putString(Integer.toString(i),json);
        }
        bookmarkEditor.apply();
    }


    public Bookmark[] getUserBookmark(){

        int count = getUserBookmarkCount();
        Bookmark[] bookmarks = new Bookmark[count];

        for(int i=0; i<count; i++){
            String value = prefBookmark.getString(Integer.toString(i),"");
            Bookmark b = gson.fromJson(value,Bookmark.class);
            if(b != null) bookmarks[i] = b;
        }
        return bookmarks;
    }


    public boolean existChannel(){
        int count = getAllBookmarkCount();
        if(count == Constants.categories.size()){
            return false;
        }
        return true;
    }

    public int getUserBookmarkCount(){
        return prefBookmark.getAll().size();
    }

    public int getAllBookmarkCount(){
        return prefDef.getAll().size();
    }


    public boolean isFirstTime(){
        return prefConfig.getBoolean(FIRST_TIME,true);
    }


    public void setFirstTime(boolean firstTime){
        configEditor.putBoolean(FIRST_TIME,firstTime);
        configEditor.apply();
    }

}
