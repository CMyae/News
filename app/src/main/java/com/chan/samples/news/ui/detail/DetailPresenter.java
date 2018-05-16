package com.chan.samples.news.ui.detail;

import android.content.Context;

import com.chan.samples.news.di.ActivityContext;

import javax.inject.Inject;

/**
 * Created by chan on 2/10/18.
 */

public class DetailPresenter implements DetailContract.DetailMvpPresenter{

    DetailContract.DetailView detailView;

    @Inject
    public DetailPresenter(@ActivityContext Context context){
        this.detailView = (DetailContract.DetailView) context;
    }

    @Override
    public void onMenuBrowserOpenClick(String url) {
        if(!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://" + url;
        }
        detailView.openBrowserIntent(url);
    }


    @Override
    public void onMenuShareClick() {
        detailView.openShareIntent();
    }

}
