package com.chan.samples.news.ui.login;

/**
 * Created by chan on 2/12/18.
 */

public interface LoginContract {

    interface LoginView{
        void onLoginCompleted();
        void showProgressDialog();
        void hideProgressDialog();
        void updateDialogMessage();
    }

    interface LoginMvpPresenter{
        void loadBookmarkByUser(String id);
    }
}
