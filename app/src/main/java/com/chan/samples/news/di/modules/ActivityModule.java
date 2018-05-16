package com.chan.samples.news.di.modules;

import android.content.Context;

import com.chan.samples.news.di.ActivityContext;
import com.chan.samples.news.di.PerActivity;
import com.chan.samples.news.di.View;
import com.chan.samples.news.ui.articles.ArticleContract;
import com.chan.samples.news.ui.articles.ArticlePresenter;
import com.chan.samples.news.ui.detail.DetailContract;
import com.chan.samples.news.ui.detail.DetailPresenter;
import com.chan.samples.news.ui.edit.EditContract;
import com.chan.samples.news.ui.edit.EditPresenter;
import com.chan.samples.news.ui.login.LoginContract;
import com.chan.samples.news.ui.login.LoginPresenter;
import com.chan.samples.news.ui.logout.LogoutContract;
import com.chan.samples.news.ui.logout.LogoutPresenter;
import com.chan.samples.news.ui.main.MainContract;
import com.chan.samples.news.ui.main.MainPresenter;
import com.chan.samples.news.ui.search.SearchContract;
import com.chan.samples.news.ui.search.SearchPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chan on 1/17/18.
 */

@Module
public class ActivityModule {

    private Context context;
    private Object view;

    public ActivityModule(Context context) {
        this.context = context;
    }

    public ActivityModule(Context context,Object view){
        this.context = context;
        this.view = view;
    }

    @ActivityContext
    @PerActivity
    @Provides
    public Context provideContext(){
        return context;
    }


    @View
    @PerActivity
    @Provides
    public Object provideView(){
        return view;
    }


    @PerActivity
    @Provides
    public MainContract.MainMvpPresenter provideMainPresenter(MainPresenter presenter){
        return presenter;
    }


    @PerActivity
    @Provides
    public SearchContract.SearchMvpPresenter provideSearchPresenter(SearchPresenter presenter){
        return presenter;
    }


    @PerActivity
    @Provides
    public ArticleContract.ArticleMvpPresenter provideArticlePresenter(ArticlePresenter presenter){
        return presenter;
    }


    @PerActivity
    @Provides
    public DetailContract.DetailMvpPresenter provideDetailPresenter(DetailPresenter presenter){
        return presenter;
    }

    @PerActivity
    @Provides
    public LoginContract.LoginMvpPresenter provideLoginPresenter(LoginPresenter presenter){
        return presenter;
    }


    @PerActivity
    @Provides
    public EditContract.EditMvpPresenter provideEditPresenter(EditPresenter presenter){
        return presenter;
    }


    @PerActivity
    @Provides
    public LogoutContract.LogoutMvpPresenter provideLogoutPresenter(LogoutPresenter presenter){
        return presenter;
    }

}
