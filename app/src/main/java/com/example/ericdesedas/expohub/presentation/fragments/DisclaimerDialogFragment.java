package com.example.ericdesedas.expohub.presentation.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class DisclaimerDialogFragment extends DialogFragment {

    private Unbinder unbinder;
    private PreferenceHelper preferenceHelper;

    @BindView(R.id.checkbox) CheckBox checkBox;

    public static DisclaimerDialogFragment newInstance(PreferenceHelper preferenceHelper) {
        DisclaimerDialogFragment fragment   = new DisclaimerDialogFragment();
        fragment.preferenceHelper           = preferenceHelper;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view   = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_disclaimer, null);
        unbinder    = ButterKnife.bind(this, view);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                preferenceHelper.writeBoolPref(PreferenceHelper.DO_NOT_SHOW_DISCLAIMER, isChecked);
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.disclaimer))
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
