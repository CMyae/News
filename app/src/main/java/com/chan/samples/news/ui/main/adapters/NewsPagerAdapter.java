package com.chan.samples.news.ui.main.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.ui.main.HeadlineNewsFragment;

/**
 * Created by chan on 1/10/18.
 */

public class NewsPagerAdapter extends FragmentStatePagerAdapter {

    private ArticleResponse headlineNews;

    public NewsPagerAdapter(FragmentManager fm, ArticleResponse headlineNews) {
        super(fm);
        this.headlineNews = headlineNews;
    }


    @Override
    public Fragment getItem(int position) {
        return HeadlineNewsFragment.getInstance(headlineNews.getArticles().get(position));
    }

    @Override
    public int getCount() {
        return headlineNews.getArticles().size();
    }


}
