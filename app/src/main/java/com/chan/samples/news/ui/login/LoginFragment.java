package com.chan.samples.news.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chan.samples.news.NewsApp;
import com.chan.samples.news.R;
import com.chan.samples.news.di.components.DaggerActivityComponent;
import com.chan.samples.news.di.modules.ActivityModule;
import com.chan.samples.news.ui.main.MainActivity;
import com.chan.samples.news.utils.NetworkUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LoginFragment extends Fragment implements LoginContract.LoginView {

    private static final String TAG = "LoginFragment";

    @BindView(R.id.tvLogin)
    TextView tvLogin;

    @Inject
    LoginContract.LoginMvpPresenter presenter;

    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        ButterKnife.bind(this, view);
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(getContext(), this))
                .applicationComponent(NewsApp.getApp().getComponent())
                .build()
                .inject(this);

        mAuth = FirebaseAuth.getInstance();

        callbackManager = CallbackManager.Factory.create();

        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.message_fb_login));
        dialog.setCancelable(false);
        dialog.setIndeterminateDrawable(getContext().getDrawable(R.drawable.custom_progress_bar));

        //delete below code
//        FirebaseAuth.getInstance().signOut();
//        LoginManager.getInstance().logOut();

        return view;
    }


    @OnClick(R.id.tvLogin)
    public void onLoginBtnClick(View v) {
        if (!NetworkUtils.isNetworkConnected(getContext())) {
            Toast.makeText(getContext(), getString(R.string.message_error_connection), Toast.LENGTH_SHORT).show();
            return;
        }
        ((MainActivity) getActivity()).hideBottomSheet();
        showProgressDialog();
        login();
    }


    @OnClick(R.id.tvCancel)
    public void onCancelClick(View v) {
        ((MainActivity) getActivity()).hideBottomSheet();
    }


    private void login() {
        LoginManager.getInstance().logInWithReadPermissions(
                LoginFragment.this,
                Arrays.asList("email", "public_profile")
        );
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                hideProgressDialog();
            }

            @Override
            public void onError(FacebookException error) {
                hideProgressDialog();
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            updateDialogMessage();
                            ((MainActivity) getActivity()).setUpProfilePic(FirebaseAuth.getInstance().getCurrentUser());
                            loadUserBookmark();

                        } else {
                            hideProgressDialog();
                        }
                    }
                });
    }


    private void loadUserBookmark(){
        String id = Profile.getCurrentProfile().getId();
        if(id != null){
            presenter.loadBookmarkByUser(id);
        }
    }


    @Override
    public void showProgressDialog() {
        dialog.show();
    }


    @Override
    public void hideProgressDialog() {
        dialog.dismiss();
    }


    @Override
    public void updateDialogMessage() {
        dialog.setMessage("Sync user info");
    }



    @Override
    public void onLoginCompleted() {
        hideProgressDialog();
        ((MainActivity) getActivity()).onLoginSuccess();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
