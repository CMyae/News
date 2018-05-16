package com.chan.samples.news.ui.logout;

import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.di.View;
import com.chan.samples.news.utils.Constants;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by chan on 2/23/18.
 */

public class LogoutPresenter implements LogoutContract.LogoutMvpPresenter {

    private LogoutContract.LogoutView view;
    private DataManager dataManager;


    @Inject
    public LogoutPresenter(@View Object view, DataManager dataManager) {
        this.view = (LogoutContract.LogoutView) view;
        this.dataManager = dataManager;
    }


    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut(); //logout FireBase
        LoginManager.getInstance().logOut(); //logout facebook

        //reset default bookmark after logout
        List<Bookmark> defaultBookmarks = Constants.categories;
        dataManager.getPrefHelper().saveUserBookmark(defaultBookmarks);

        view.onLogoutFinished();
    }


}
