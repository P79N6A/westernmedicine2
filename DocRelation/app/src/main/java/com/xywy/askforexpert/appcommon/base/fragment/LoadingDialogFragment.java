package com.xywy.askforexpert.appcommon.base.fragment;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xywy.askforexpert.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoadingDialogFragment extends DialogFragment {
    public static final String DIALOG_TITLE_ARG_KEY = "dialog_title";
    private static final String TAG = "LoadingDialogFragment";
    private String dialogTitle;

    public LoadingDialogFragment() {
        // Required empty public constructor
    }

    public static LoadingDialogFragment newInstance(String title) {
        LoadingDialogFragment fragment = new LoadingDialogFragment();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE_ARG_KEY, title);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dialogTitle = getArguments().getString(DIALOG_TITLE_ARG_KEY, "加载中……");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_loading_dialog, null);
        TextView title = (TextView) view.findViewById(R.id.dialog_title);
        title.setText(dialogTitle);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }
}
