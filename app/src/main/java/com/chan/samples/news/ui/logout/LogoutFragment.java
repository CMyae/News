package com.chan.samples.news.ui.logout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chan.samples.news.NewsApp;
import com.chan.samples.news.R;
import com.chan.samples.news.di.components.DaggerActivityComponent;
import com.chan.samples.news.di.modules.ActivityModule;
import com.chan.samples.news.ui.main.MainActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chan on 2/23/18.
 */

public class LogoutFragment extends Fragment implements LogoutContract.LogoutView{

    @BindView(R.id.tvLogin) TextView tvLogout;

    @Inject LogoutContract.LogoutMvpPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        ButterKnife.bind(this,view);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(getContext(), this))
                .applicationComponent(NewsApp.getApp().getComponent())
                .build()
                .inject(this);

        tvLogout.setText(getString(R.string.fb_logout));

        return view;
    }


    @OnClick(R.id.tvLogin)
    public void onLogoutClick(View v){
        presenter.logout();
    }


    @OnClick(R.id.tvCancel)
    public void onCancelClick(View v){
        ((MainActivity)getActivity()).hideBottomSheet();
    }



    @Override
    public void onLogoutFinished() {
        ((MainActivity)getActivity()).hideBottomSheet();
        ((MainActivity)getActivity()).onLogoutSuccess();
    }
}
