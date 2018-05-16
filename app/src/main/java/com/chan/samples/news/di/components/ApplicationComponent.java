package com.chan.samples.news.di.components;

import android.app.Application;

import com.chan.samples.news.NewsApp;
import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by chan on 1/17/18.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(NewsApp app);

    DataManager getDataManager();
}
