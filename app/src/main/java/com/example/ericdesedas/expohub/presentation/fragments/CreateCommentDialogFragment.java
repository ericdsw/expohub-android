package com.example.ericdesedas.expohub.presentation.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.ericdesedas.expohub.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateCommentDialogFragment extends DialogFragment {

    private Unbinder unbinder;
    private CreateCommentListener listener;

    @BindView(R.id.comment_text) EditText commentContent;

    public static CreateCommentDialogFragment newInstance(CreateCommentListener listener) {
        CreateCommentDialogFragment fragment    = new CreateCommentDialogFragment();
        fragment.listener                       = listener;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view   = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_fragment_create_comment, null);
        unbinder    = ButterKnife.bind(this, view);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(getString(R.string.title_create_comment))
                .setPositiveButton(R.string.action_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onCreateComment(commentContent.getText().toString());
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
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

    public interface CreateCommentListener {
        void onCreateComment(String commentText);
    }
}
