package com.chan.samples.news.ui.login;

import android.util.Log;

import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.data.remote.BookmarkCallback;
import com.chan.samples.news.di.View;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by chan on 2/12/18.
 */

public class LoginPresenter implements LoginContract.LoginMvpPresenter, BookmarkCallback {

    private DataManager dataManager;
    private LoginContract.LoginView loginView;

    @Inject
    public LoginPresenter(DataManager dataManager,@View Object view) {
        this.dataManager = dataManager;
        this.loginView = (LoginContract.LoginView) view;
    }


    @Override
    public void loadBookmarkByUser(String id) {
        dataManager.getNetworkBookmarksByUser(id,this);
    }


    @Override
    public void fetchedBookmark(List<Bookmark> bookmarks) {
        if(bookmarks != null){
            //save in share pref
            Log.i("TAG","bookmark for account fetched -> " + bookmarks.size());
            dataManager.getPrefHelper().saveUserBookmark(bookmarks);
        }
        loginView.onLoginCompleted();
    }

    @Override
    public void loadedError() {

    }
}
