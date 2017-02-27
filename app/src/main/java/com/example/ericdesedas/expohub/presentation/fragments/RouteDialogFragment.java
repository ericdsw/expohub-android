package com.example.ericdesedas.expohub.presentation.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RouteDialogFragment extends DialogFragment {

    private Unbinder unbinder;
    private String routeString;
    private String routeMethod;

    @BindView(R.id.route_text) TextView textView;
    @BindView(R.id.route_method) TextView routeMethodTextView;

    public RouteDialogFragment() {
        //
    }

    public static RouteDialogFragment newInstance(String routeMethod, String routeString) {
        RouteDialogFragment fragment = new RouteDialogFragment();
        fragment.routeMethod = routeMethod;
        fragment.routeString = routeString;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_route, null);
        unbinder  = ButterKnife.bind(this, view);

        textView.setText(routeString);
        routeMethodTextView.setText(routeMethod);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.title_current_url))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
