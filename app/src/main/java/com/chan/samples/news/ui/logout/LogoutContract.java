package com.chan.samples.news.ui.logout;

/**
 * Created by chan on 2/23/18.
 */

public interface LogoutContract {

    interface LogoutView{
        void onLogoutFinished();
    }

    interface LogoutMvpPresenter{
        void logout();
    }
}
