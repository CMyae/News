package com.chan.samples.news;

import android.app.Application;
import android.widget.Toast;

import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.di.components.ApplicationComponent;
import com.chan.samples.news.di.components.DaggerApplicationComponent;
import com.chan.samples.news.di.modules.ApplicationModule;

import javax.inject.Inject;

/**
 * Created by chan on 1/17/18.
 */

public class NewsApp extends Application {

    private static NewsApp app;
    ApplicationComponent component;

    @Inject
    DataManager dataManager;

    public static NewsApp getApp(){
        return app;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(getApplicationContext()))
                .build();
        component.inject(this);

    }

    public ApplicationComponent getComponent() {
        return component;
    }


}
