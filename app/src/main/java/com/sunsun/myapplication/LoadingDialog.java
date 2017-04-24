package com.sunsun.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jesse on 5/9/16.
 */
public class LoadingDialog extends BaseDialog {

    @Bind(R.id.pb_loading)
    ProgressBar progressBar;
    private int color = 0xff118cde;
    private static final String KEY_CANCEL = "cancel";

    public static LoadingDialog getInstance(boolean cancelable) {
        LoadingDialog f = new LoadingDialog();

        Bundle args = new Bundle();
        args.putBoolean(KEY_CANCEL, cancelable);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        View view = inflater.inflate(R.layout.dialog_loading, container);
        ButterKnife.bind(this, view);
        if (getArguments() == null)
            setCancelable(true);
        else
            setCancelable(getArguments().getBoolean(KEY_CANCEL, true));
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(color, android.graphics.PorterDuff.Mode.MULTIPLY);
        return view;
    }
}
