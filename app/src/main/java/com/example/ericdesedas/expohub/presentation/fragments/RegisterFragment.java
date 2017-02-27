package com.example.ericdesedas.expohub.presentation.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.events.RegisterEvent;
import com.example.ericdesedas.expohub.data.events.ShowLoginRegisterRoute;
import com.example.ericdesedas.expohub.data.events.SwapToLoginEvent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class RegisterFragment extends BaseFragment {

    @BindView(R.id.name_field)      EditText nameEditText;
    @BindView(R.id.username_field)  EditText usernameEditText;
    @BindView(R.id.email_field)     EditText emailEditText;
    @BindView(R.id.password_field)  EditText passwordField;

    private EventBus eventBus;

    public static RegisterFragment newInstance(EventBus eventBus) {
        RegisterFragment registerFragment = new RegisterFragment();
        registerFragment.setEventBus(eventBus);
        return registerFragment;
    }

    // Lifecycle Methods

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view   = inflater.inflate(R.layout.fragment_register, container, false);
        unbinder    = ButterKnife.bind(this, view);
        return view;
    }

    // Click Methods

    @OnClick(R.id.register_button)
    public void onRegisterButtonClick() {

        if (nameEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.empty_name_param), Toast.LENGTH_LONG).show();
            return;
        }

        if (usernameEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.empty_username_param), Toast.LENGTH_LONG).show();
            return;
        }

        if (emailEditText.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.empty_email_param), Toast.LENGTH_LONG).show();
            return;
        }

        if (passwordField.getText().toString().equals("")) {
            Toast.makeText(getActivity(), getString(R.string.empty_password_param_register), Toast.LENGTH_LONG).show();
            return;
        }

        RegisterEvent registerEvent = new RegisterEvent();
        registerEvent.name          = nameEditText.getText().toString();
        registerEvent.username      = usernameEditText.getText().toString();
        registerEvent.email         = emailEditText.getText().toString();
        registerEvent.password      = passwordField.getText().toString();

        eventBus.post(registerEvent);
    }

    @OnClick(R.id.go_to_login_text)
    public void onGoToLoginTextClick() {
        eventBus.post(new SwapToLoginEvent());
    }

    @OnClick(R.id.route_info_button)
    public void onRouteInfoButtonClick() {
        eventBus.post(new ShowLoginRegisterRoute("POST", "/register"));
    }

    // Public Methods

    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }
}
