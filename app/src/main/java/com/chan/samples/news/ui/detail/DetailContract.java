package com.chan.samples.news.ui.detail;

/**
 * Created by chan on 2/10/18.
 */

public interface DetailContract {

    interface DetailView{
        void openBrowserIntent(String url);
        void openShareIntent();
    }

    interface DetailMvpPresenter{
        void onMenuBrowserOpenClick(String url);
        void onMenuShareClick();
    }
}
