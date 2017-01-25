package com.example.ericdesedas.expohub.presentation.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.events.LoginEvent;
import com.example.ericdesedas.expohub.data.events.RegisterEvent;
import com.example.ericdesedas.expohub.data.events.SwapToLoginEvent;
import com.example.ericdesedas.expohub.data.events.SwapToRegisterEvent;
import com.example.ericdesedas.expohub.data.models.User;
import com.example.ericdesedas.expohub.presentation.fragments.LoginFragment;
import com.example.ericdesedas.expohub.presentation.fragments.RegisterFragment;
import com.example.ericdesedas.expohub.presentation.presenters.LoginRegisterPresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class LoginRegisterActivity extends BaseActivity implements
    LoginRegisterPresenter.View {

    private RegisterFragment registerFragment;
    private LoginFragment loginFragment;
    private ProgressDialog loginProgressDialog;
    private ProgressDialog registerProgressDialog;

    @Inject EventBus eventBus;
    @Inject LoginRegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        ButterKnife.bind(this);

        getActivityComponent().inject(this);

        // Fragments
        loginFragment       = LoginFragment.newInstance(eventBus);
        registerFragment    = RegisterFragment.newInstance(eventBus);

        // Login Dialog
        loginProgressDialog = new ProgressDialog(this);
        loginProgressDialog.setMessage(getString(R.string.login_process_message));

        // Register Dialog
        registerProgressDialog = new ProgressDialog(this);
        registerProgressDialog.setMessage(getString(R.string.register_process_message));

        presenter.setView(this);
        presenter.initialize();

        displayLoginFragment();
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventBus.register(this);
        presenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        eventBus.unregister(this);
        presenter.onStop();
    }

    @Subscribe
    public void onLoginEvent(LoginEvent event) {
        presenter.onLoginCommand(event.loginParam, event.password);
    }

    @Subscribe
    public void onRegisterEvent(RegisterEvent event) {
        presenter.onRegisterCommand(event.name, event.username, event.email, event.password);
    }

    @Subscribe
    public void onSwapToRegisterEvent(SwapToRegisterEvent event) {
        displayRegisterFragment();
    }

    @Subscribe
    public void onSwapToLoginEvent(SwapToLoginEvent event) {
        displayLoginFragment();
    }

    // View methods

    @Override
    public void toggleLoginLoading(boolean showLoading) {
        if (showLoading) {
            loginProgressDialog.show();
        } else if (loginProgressDialog.isShowing()) {
            loginProgressDialog.dismiss();
        }
    }

    @Override
    public void toggleRegisterLoading(boolean showLoading) {
        if (showLoading) {
            registerProgressDialog.show();
        } else if (loginProgressDialog.isShowing()) {
            registerProgressDialog.dismiss();
        }
    }

    @Override
    public void showError(int code, String error) {
        if (code == 500) {
            Toast.makeText(this, getString(R.string.generic_network_error), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showLoggedMessage(User user) {
        Toast.makeText(this, String.format(getString(R.string.welcome_back_message), user.name), Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRegisteredMessage(User user) {
        Toast.makeText(this, String.format(getString(R.string.welcome_message), user.name), Toast.LENGTH_LONG).show();
    }

    @Override
    public void closeView() {
        finish();
    }

    // Private Methods

    private void displayLoginFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_view, loginFragment)
                .commit();
    }

    private void displayRegisterFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.root_view, registerFragment)
                .commit();
    }
}
