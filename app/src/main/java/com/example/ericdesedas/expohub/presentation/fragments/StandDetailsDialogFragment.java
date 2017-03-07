package com.example.ericdesedas.expohub.presentation.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.data.models.Stand;
import com.example.ericdesedas.expohub.helpers.image.ImageDownloader;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StandDetailsDialogFragment extends DialogFragment {

    @BindView(R.id.stand_image)         ImageView standImage;
    @BindView(R.id.stand_name)          TextView standName;
    @BindView(R.id.stand_description)   TextView standDescription;

    private ImageDownloader imageDownloader;
    private Unbinder unbinder;
    private Stand stand;

    public StandDetailsDialogFragment() {
        //
    }

    public static StandDetailsDialogFragment newInstance(Stand stand, ImageDownloader imageDownloader) {

        StandDetailsDialogFragment fragment = new StandDetailsDialogFragment();
        fragment.stand                      = stand;
        fragment.imageDownloader            = imageDownloader;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view   = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_stand_details, null);
        unbinder    = ButterKnife.bind(this, view);

        standName.setText(stand.name);
        standDescription.setText(stand.description);

        imageDownloader.setMaxImageSize(500)
                .setCircularImage(stand.getImage(), standImage);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
