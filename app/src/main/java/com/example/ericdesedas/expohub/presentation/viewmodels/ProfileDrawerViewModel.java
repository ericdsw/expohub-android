package com.example.ericdesedas.expohub.presentation.viewmodels;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.Session;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileDrawerViewModel {

    @BindView(R.id.logged_user_layout)  View loggedUserView;
    @BindView(R.id.unidentified_view)   View unidentifiedView;
    @BindView(R.id.user_name)           TextView userNameTextView;
    @BindView(R.id.user_image)          ImageView userImage;

    private Context context;
    private Unbinder unbinder;
    private WeakReference<Listener> listener;

    public ProfileDrawerViewModel(View view, Listener listener) {
        this.unbinder   = ButterKnife.bind(this, view);
        this.listener   = new WeakReference<>(listener);
        this.context    = view.getContext();
    }

    public void showSessionData(Session session) {
        userNameTextView.setText(session.name);
        loggedUserView.setVisibility(View.VISIBLE);
        unidentifiedView.setVisibility(View.GONE);
    }

    public void formatUnidentified() {
        loggedUserView.setVisibility(View.GONE);
        unidentifiedView.setVisibility(View.VISIBLE);
    }

    public void releaseViews() {
        unbinder.unbind();
    }

    public List<Pair<View, String>> getTransitioningElements() {

        List<Pair<View, String>> transitioningElements = new ArrayList<>();
        transitioningElements.add(new Pair<View, String>(userImage, context.getString(R.string.transition_profile_image)));
        transitioningElements.add(new Pair<View, String>(userNameTextView, context.getString(R.string.transition_profile_name)));
        return transitioningElements;
    }

    @OnClick(R.id.logged_user_layout)
    void onProfileClick() {
        if (listener.get() != null) {
            listener.get().onProfileClick();
        }
    }

    @OnClick(R.id.login_button)
    void onLoginButtonClick() {
        if (listener.get() != null) {
            listener.get().onLoginButtonClick();
        }
    }

    public interface Listener {
        void onProfileClick();
        void onLoginButtonClick();
    }
}
