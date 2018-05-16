package com.chan.samples.news.data;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.chan.samples.news.data.local.NewsDatabase;
import com.chan.samples.news.data.local.PrefHelper;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.data.remote.BookmarkCallback;
import com.chan.samples.news.data.remote.OnArticleListener;
import com.chan.samples.news.data.remote.OnResponseListener;
import com.chan.samples.news.data.remote.OnSearchResponseListener;
import com.chan.samples.news.data.remote.RemoteController;
import com.chan.samples.news.utils.Constants;
import com.chan.samples.news.utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by chan on 1/13/18.
 */

public class DataManager {

    private static final String TAG = "DataManager";

    private PrefHelper prefHelper;
    private NewsDatabase db;
    private RemoteController remoteController;
    private Handler handler;

    @Inject
    public DataManager(PrefHelper prefHelper,RemoteController remoteController, NewsDatabase db) {
        this.prefHelper = prefHelper;
        this.remoteController = remoteController;
        this.db = db;
        handler = new Handler(Looper.getMainLooper());
    }



    public void getRemoteNewsByBookmark(Bookmark bookmark, boolean isLoadMore, OnResponseListener listener){
        remoteController.loadHeadlineAndLatestNewsByBookmark(bookmark,isLoadMore,listener);
    }


    public void searchNewsByQuery(String query,String page,boolean isLoadMore, OnSearchResponseListener listener){
        remoteController.loadNewsByQuery(query,page,isLoadMore,listener);
    }


    public void getRemoteHeadlineArticlesByBookmark(boolean isLoadMore,Bookmark bookmark,int page,OnArticleListener listener){
        remoteController.loadHeadlineArticlesByBookmark(bookmark,page,isLoadMore,listener);
    }

    public void getRemoteLatestArticlesByBookmark(String sources,boolean isLoadMore,Bookmark bookmark,int page,OnArticleListener listener){
        if(bookmark.getType() == Bookmark.TYPE_CATEGORY){
            remoteController.loadLatestArticlesBySources(sources,page,isLoadMore,listener);
        }else{
            remoteController.loadLatestArticlesByBookmark(bookmark,page,isLoadMore,listener);
        }
    }



    public void getLocalHeadlineAndLatestArticle(final OnResponseListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    List<ArticleResponse> headlineResponse = db.daoAccess().getOfflineHeadlineResponse();
                    List<ArticleResponse> latestResponse = db.daoAccess().getOfflineLatestResponse();

                    if(headlineResponse == null && latestResponse == null){
                        return;
                    }

                    if(headlineResponse.size() == 0 || latestResponse.size() == 0){
                        return;
                    }

                    final List<List<ArticleResponse>> results = new ArrayList<>();

                    for(int i=0;i<headlineResponse.size();i++){
                        List<ArticleResponse> list = new ArrayList<>();

                        ArticleResponse headline = headlineResponse.get(i);
                        headline.setArticles(db.daoAccess().getArticlesWithLimit(headline.getId(), 5));

                        ArticleResponse latest = latestResponse.get(i);
                        latest.setArticles(db.daoAccess().getArticlesWithLimit(latest.getId(), 4));

                        list.add(headline);
                        list.add(latest);
                        results.add(list);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onOfflineArticleResponse(results);
                        }
                    });

                }catch (Exception e){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onError();
                        }
                    });
                }
            }
        }).start();
    }




    public void storeArticleInLocal(final List<ArticleResponse> responses){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //delete all old data
                db.daoAccess().deleteAllArticleResponse();
                db.daoAccess().deleteAllArticles();

                for (int i = 0; i <responses.size() ; i++) {
                    ArticleResponse res = responses.get(i);
                    db.daoAccess().insertArticleResponse(res);
                    db.daoAccess().insertArticles(res.getArticles());
                }
                Log.i(TAG,"store data success");
            }
        }).start();

    }



    public void storeMoreArticleInLocal(final List<ArticleResponse> responses){

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i <responses.size() ; i++) {
                    ArticleResponse res = responses.get(i);
                    db.daoAccess().insertArticleResponse(res);
                    db.daoAccess().insertArticles(res.getArticles());
                }
                Log.i(TAG,"store data success");
            }
        }).start();

    }


    public void storeArticleByResponseInLocal(final long responseId, final List<Article> articles){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //delete all old data
                db.daoAccess().deleteArticlesByResponseId(responseId);
                for(Article article:articles){
                    article.setArticle_response_id(responseId);
                }
                db.daoAccess().insertArticles(articles);
            }
        }).start();
    }


    public void storeMoreArticleByResponseInLocal(final long responseId, final List<Article> articles){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(Article article:articles){
                    article.setArticle_response_id(responseId);
                }
                db.daoAccess().insertArticles(articles);
            }
        }).start();
    }


    public void getLocalArticleByResponseId(final long responsId, final OnArticleListener listener){

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Article> articles = db.daoAccess().getArticlesByResponseId(responsId);
                if(articles != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            listener.onOfflineArticleLoaded(articles);
                        }
                    });
                }
            }
        }).start();
    }


    public void saveDefaultDataInLocal(){
        List<Bookmark> bookmarks = Constants.categories;
        prefHelper.saveDefaultCategories(bookmarks);
        prefHelper.saveUserBookmark(bookmarks);
    }



    public Task<Void> storeBookmarkInNetwork(String id,List<Bookmark> bookmarks){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        return ref.child(id).setValue(bookmarks);

    }


    public void getNetworkBookmarksByUser(String id, final BookmarkCallback callback){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<List<Bookmark>> gti = new GenericTypeIndicator<List<Bookmark>>() {};
                List<Bookmark> bookmarks = dataSnapshot.getValue(gti);
                callback.fetchedBookmark(bookmarks);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.loadedError();
            }
        });

    }



    public PrefHelper getPrefHelper(){
        return prefHelper;
    }


    public RemoteController getRemoteController() {
        return remoteController;
    }

}
