package com.sunsun.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by dexian on 2016/9/12.
 */
public class BaseDialog extends DialogFragment {

    private DialogInterface.OnDismissListener onDismissListener;

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    /*This is called after  #onCreateView()
     * and before  #onViewStateRestored(Bundle).*/
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.dialog_default);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    public void onStart() {
        super.onStart();
        setupDialogParams();
    }

    @Override
    public void dismiss() {
        /**
         * 处理重复调用dismiss报的异常
         *  The fragment manager we are associated with.  Set as soon as the
         * fragment is used in a transaction; cleared after it has been removed
         * from all transactions.
         */
        synchronized (this) {
            if (getFragmentManager() != null) {
                dismissAllowingStateLoss();
            }
        }
    }

    protected void setupDialogParams() {
        Dialog dialog = getDialog();
        if (dialog == null) {
            throw new IllegalArgumentException("Dialog hasn't been created");
        }
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
    }

    public void show(Context context) {
        synchronized (this) {
            if (context instanceof AppCompatActivity) {
                AppCompatActivity activity = (AppCompatActivity) context;
                if (!activity.isFinishing())
                    show(activity.getSupportFragmentManager(), this.getClass().getSimpleName());
            } else {
                throw new IllegalStateException("The context invoked must be an Activity Context!");
            }
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        synchronized (this) {
            if (isAdded())
                dismiss();
            else
                super.show(manager, tag);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }
    }

}
