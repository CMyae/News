package com.chan.samples.news.di.components;

import com.chan.samples.news.di.PerActivity;
import com.chan.samples.news.di.modules.ActivityModule;
import com.chan.samples.news.ui.articles.ArticleListFragment;
import com.chan.samples.news.ui.detail.NewsDetailActivity;
import com.chan.samples.news.ui.edit.EditBookmarkFragment;
import com.chan.samples.news.ui.login.LoginFragment;
import com.chan.samples.news.ui.logout.LogoutFragment;
import com.chan.samples.news.ui.main.MainActivity;
import com.chan.samples.news.ui.search.NewsSearchFragment;

import dagger.Component;

/**
 * Created by chan on 1/17/18.
 */

@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity activity);
    void inject(NewsDetailActivity activity);
    void inject(NewsSearchFragment fragment);
    void inject(ArticleListFragment fragment);
    void inject(EditBookmarkFragment fragment);
    void inject(LoginFragment activity);
    void inject(LogoutFragment activity);
}
