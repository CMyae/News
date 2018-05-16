package com.chan.samples.news.ui.detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.chan.samples.news.NewsApp;
import com.chan.samples.news.R;
import com.chan.samples.news.di.components.DaggerActivityComponent;
import com.chan.samples.news.di.modules.ActivityModule;
import com.chan.samples.news.ui.base.BaseActivity;
import com.chan.samples.news.utils.ViewUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsDetailActivity extends BaseActivity implements DetailContract.DetailView{

    private static final String TAG = "NewsDetailActivity";
    private static final String URL = "URL";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.webView) WebView webView;
    @BindView(R.id.progress_loading_circle) ProgressBar progressBar;

    @Inject DetailContract.DetailMvpPresenter presenter;

    private String articleUrl;


    public static Intent getActivityIntent(Context context,String url){
        Intent intent = new Intent(context,NewsDetailActivity.class);
        intent.putExtra(URL,url);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        ButterKnife.bind(this);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(NewsApp.getApp().getComponent())
                .build()
                .inject(this);

        initViews();

    }


    /**
     * Initialize all views
     */
    private void initViews(){

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressBar.post(new Runnable() {
            @Override
            public void run() {
                //place center y
                progressBar.setY(ViewUtils.getScreenHeight(NewsDetailActivity.this)/2
                        - progressBar.getHeight()/2
                        - ViewUtils.dpToPx(NewsDetailActivity.this,64));
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);

            }
        });

        articleUrl = getIntent().getStringExtra(URL);

        if(articleUrl != null){
            webView.loadUrl(articleUrl);
        }

        //hide progress loading circle
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;

            case R.id.menu_share:
                presenter.onMenuShareClick();
                break;

            case R.id.menu_open_with:
                presenter.onMenuBrowserOpenClick(articleUrl);
                break;
        }
        return true;
    }




    @Override
    public void openBrowserIntent(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if(browserIntent.resolveActivity(getPackageManager()) != null){
            startActivity(browserIntent);
        }
    }


    @Override
    public void openShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT,"title");
        shareIntent.putExtra(Intent.EXTRA_TEXT,articleUrl);
        startActivity(Intent.createChooser(shareIntent,"Share With"));
    }


    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
