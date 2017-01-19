package com.example.ericdesedas.expohub.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.events.LoginEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LoginFragment extends Fragment {

    @BindView(R.id.login_param_field)   EditText loginParamField;
    @BindView(R.id.password_field)      EditText passwordField;

    private Unbinder unbinder;
    private EventBus eventBus;

    public static LoginFragment newInstance(EventBus eventBus) {
        LoginFragment loginFragment = new LoginFragment();
        loginFragment.setEventBus(eventBus);
        return loginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view   = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder    = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.login_button)
    public void onLoginButtonClick() {

        if (loginParamField.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.empty_login_param), Toast.LENGTH_LONG).show();
            return;
        }

        if (passwordField.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.empty_password_param), Toast.LENGTH_LONG).show();
            return;
        }

        LoginEvent loginEvent   = new LoginEvent();
        loginEvent.loginParam   = loginParamField.getText().toString();
        loginEvent.password     = passwordField.getText().toString();

        eventBus.post(loginEvent);
    }

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
