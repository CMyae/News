package com.chan.samples.news.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chan.samples.news.NewsApp;
import com.chan.samples.news.R;
import com.chan.samples.news.data.DataManager;
import com.chan.samples.news.data.models.Article;
import com.chan.samples.news.data.models.ArticleResponse;
import com.chan.samples.news.data.models.Bookmark;
import com.chan.samples.news.di.components.DaggerActivityComponent;
import com.chan.samples.news.di.modules.ActivityModule;
import com.chan.samples.news.ui.base.BaseActivity;
import com.chan.samples.news.ui.detail.NewsDetailActivity;
import com.chan.samples.news.ui.edit.EditBookmarkFragment;
import com.chan.samples.news.ui.login.LoginFragment;
import com.chan.samples.news.ui.logout.LogoutFragment;
import com.chan.samples.news.ui.main.adapters.NewsAdapter;
import com.chan.samples.news.ui.articles.ArticleListFragment;
import com.chan.samples.news.ui.search.NewsSearchFragment;
import com.chan.samples.news.ui.views.EndlessScrollListener;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


//TODO: check box unclickable
public class MainActivity extends BaseActivity implements MainContract.MainView,
        SwipeRefreshLayout.OnRefreshListener,NewsAdapter.OnItemClickListener {

    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.appBarLayout) AppBarLayout appBarLayout;
    @BindView(R.id.imgProfile) CircleImageView imgProfile;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.searchContainer) FrameLayout searchContainer;
    @BindView(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.dimView) View dimView;
    @BindView(R.id.bottom_sheet) LinearLayout bottomSheet;

    @Inject DataManager dataManager;
    @Inject MainContract.MainMvpPresenter presenter;

    private List<List<ArticleResponse>> articles = new ArrayList<>();
    private Fragment currentFragment;
    private NewsAdapter adapter;
    private LinearLayoutManager layoutManager;
    private int index;
    private EndlessScrollListener listener;
    private boolean isLoadMore;
    private boolean isBottomsheetExpand;
    private Bookmark[] bookmarks;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(NewsApp.getApp().getComponent())
                .build()
                .inject(this);

        init();

        presenter.loadOfflineArticles();
        presenter.loadArticles(bookmarks[index]);

    }


    /**
     * Initialize all views
     */
    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_profile);

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        if(dataManager.getPrefHelper().isFirstTime()){
            dataManager.saveDefaultDataInLocal();
            dataManager.getPrefHelper().setFirstTime(false);
        }

        if(user != null){
            hideLoginMenu();
            setUpProfilePic(user);
        }else{
            showLoginMenu();
        }

        bookmarks = dataManager.getPrefHelper().getUserBookmark();

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(null);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        listener = new EndlessScrollListener(layoutManager);

        recyclerView.addOnScrollListener(listener);

        adapter = new NewsAdapter(this, articles);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);


        listener.setOnLoadMoreListener(new EndlessScrollListener.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isLoadMore) return;
                index++;
                if (index < bookmarks.length) {
                    showLoadingSpinner();
                    presenter.loadMoreArticles(bookmarks[index]);
                } else {
                    listener.stopLoading(); //no more data to load
                }
            }
        });

//        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                ViewCompat.setElevation(appBarLayout, 16);
//            }
//        });
        BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
        BottomSheetBehavior.from(bottomSheet).setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull final View bottomSheet, int newState) {
                        if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                            dimView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        dimView.setVisibility(View.VISIBLE);
                        dimView.setAlpha(slideOffset);
                    }
                });
    }



    public void setUpProfilePic(FirebaseUser user){
        if(user == null) return;
        String facebookUserId;
        for(UserInfo profile : user.getProviderData()) {
            // check if the provider id matches "facebook.com"
            if(FacebookAuthProvider.PROVIDER_ID.equals(profile.getProviderId())) {
                showProfileIcon();
                facebookUserId = profile.getUid();
                String photoUrl = "https://graph.facebook.com/" + facebookUserId + "/picture?type=large&redirect=true&width=600&height=600";
                Glide.with(this)
                        .load(photoUrl)
                        .dontAnimate()
                        .into(imgProfile);
            }
        }
    }


    @Override
    public void onLoginSuccess(){
        //refresh data
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
        hideLoginMenu();
    }


    @Override
    public void onLogoutSuccess() {
//        swipeRefreshLayout.setRefreshing(true);
//        onRefresh();
        hideProfileIcon();
        showLoginMenu();
    }


    @OnClick(R.id.imgProfile)
    public void onProfileImageClick(View v){
        showLogoutBottomSheet();
        //presenter.logOut();
    }


    @OnClick(R.id.dimView)
    public void onDimViewClick(View v){
        hideBottomSheet();
    }


    @Override
    public void showArticlesResponse(List<ArticleResponse> articles) {
        if (articles == null) return;

        this.articles.clear();
        this.articles.add(articles);
        adapter.notifyDataSetChanged();

    }


    @Override
    public void showRefreshArticlesResponse(List<ArticleResponse> articles) {
        if (articles == null) return;

        this.articles.clear();
        this.articles.add(articles);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void showMoreArticleResponse(List<ArticleResponse> newArticles) {
        if (articles == null) return;

        articles.add(newArticles);
        adapter.notifyItemInserted(articles.size() - 1);

        listener.setLoading();
    }


    @Override
    public void showOfflineArticles(List<List<ArticleResponse>> articles) {
        if (articles == null) return;

        this.articles.clear();
        this.articles.addAll(articles);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void showLoadingSpinner() {
        articles.add(null);
        adapter.notifyItemInserted(articles.size() - 1);
    }


    @Override
    public void hideLoadingSpinner() {
        int index = articles.size() - 1;
        articles.remove(index);
        adapter.notifyItemRemoved(index);
    }


    @Override
    public void showSearchView() {
        NewsSearchFragment fragment = new NewsSearchFragment();
        currentFragment = fragment;

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.searchContainer, fragment)
                .addToBackStack(null)
                .commit();
        searchContainer.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideSearchView() {
        currentFragment = null;
        getSupportFragmentManager().popBackStack();
        searchContainer.setVisibility(View.GONE);
    }


    @Override
    public void hideRefreshIndicator() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void resetLoadingStatus() {
        isLoadMore = true;
        index = 0;
        listener.setLoading();
    }


    @Override
    public void showEditBottomSheet() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_content,new EditBookmarkFragment())
                .commit();
        expandBottomSheet();
    }


    @Override
    public void showLoginBottomSheet() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_content,new LoginFragment())
                .commit();
        expandBottomSheet();
    }


    @Override
    public void showLogoutBottomSheet() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bottom_content,new LogoutFragment())
                .commit();
        expandBottomSheet();
    }


    public void expandBottomSheet(){
        bottomSheet.post(new Runnable() {
            @Override
            public void run() {
                bottomSheet.requestLayout();
                bottomSheet.invalidate();
                BottomSheetBehavior.from(bottomSheet)
                        .setState(BottomSheetBehavior.STATE_EXPANDED);
                isBottomsheetExpand = true;
            }
        });
    }


    @Override
    public void hideBottomSheet() {
        BottomSheetBehavior.from(findViewById(R.id.bottom_sheet))
                .setState(BottomSheetBehavior.STATE_COLLAPSED);
        isBottomsheetExpand = false;

    }

    @Override
    public void showLoginMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void hideLoginMenu() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void showProfileIcon() {
        imgProfile.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProfileIcon() {
        imgProfile.setVisibility(View.GONE);
    }


    @Override
    public void onBookmarkEdited() {
        //refresh data
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }


    @Override
    public void onRefresh() {
        //reset bookmark
        bookmarks = dataManager.getPrefHelper().getUserBookmark();
        presenter.onRefreshArticles(bookmarks[0]);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                presenter.onLoginMenuClick();
                break;

            case R.id.menu_edit:
                presenter.onEditMenuClick();
                break;

            case R.id.menu_search:
                presenter.onSearchMenuClick();
                break;

            default:
                break;
        }

        return true;
    }



    @Override
    public void onBackPressed() {

        //if bottom sheet expanded
        if(isBottomsheetExpand){
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.bottom_content);
            if(fragment != null) {
                if (fragment instanceof EditBookmarkFragment) {
                    ((EditBookmarkFragment) fragment).onCancelClick(null);
                    return;
                }
            }
            hideBottomSheet();
            return;
        }

        //check whether there is other fragment or not
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragmentManager.getBackStackEntryCount() > 0 && currentFragment != null) {

            if(currentFragment instanceof NewsSearchFragment){
                hideSearchView();

            }else if(currentFragment instanceof ArticleListFragment){
                fragmentManager.popBackStack();
            }

        } else {
            super.onBackPressed();
        }
    }



    @Override
    public void onSeeMoreClick(int position,int type) {
        ArticleResponse response;
        if(type == ArticleResponse.TYPE_HEADLINE){
            response = articles.get(position).get(0);
        }else{
            response = articles.get(position).get(1);
        }
        ArticleListFragment fragment = ArticleListFragment.getInstance(response.getId(),response.getType(), bookmarks[position]);
        currentFragment = fragment;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_view, fragment)
                .addToBackStack(null)
                .commit();
        expandToolbar();
    }


    /**
     * Click event of latest article in grid view
     * @param responsePos
     * @param articlePos
     */
    @Override
    public void onArticleItemClick(int responsePos, int articlePos) {
        ArticleResponse latestResponse = articles.get(responsePos).get(1);
        Article article = latestResponse.getArticles().get(articlePos);
        Intent intent = NewsDetailActivity.getActivityIntent(this,article.getUrl());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }


    /**
     * Click event of headline article from article pager
     * @param article
     */
    @Override
    public void onArticleItemClick(Article article) {
        Intent intent = NewsDetailActivity.getActivityIntent(this,article.getUrl());
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
    }


    public void expandToolbar(){
        appBarLayout.setExpanded(true,false);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoginFragment fragment = (LoginFragment) getSupportFragmentManager()
                .findFragmentById(R.id.bottom_content);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
