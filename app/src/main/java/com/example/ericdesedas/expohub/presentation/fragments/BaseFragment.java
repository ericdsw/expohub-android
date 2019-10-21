package com.example.ericdesedas.expohub.presentation.fragments;

import androidx.fragment.app.Fragment;

import butterknife.Unbinder;

public class BaseFragment extends Fragment {

    protected Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
