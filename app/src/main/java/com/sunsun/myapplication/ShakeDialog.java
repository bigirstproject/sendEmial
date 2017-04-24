package com.sunsun.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunsun.myapplication.activity.SendAtachmentEmailActivity;
import com.sunsun.myapplication.activity.SendEmailActivity;
import com.sunsun.myapplication.activity.SendSSLCCAndBCCEmailActivity;
import com.sunsun.myapplication.activity.SendSSLEmailActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dexian on 4/6/17.
 */
public class ShakeDialog extends BaseDialog implements View.OnClickListener {

    @Bind(R.id.feed_book)
    TextView feedbook;
    @Bind(R.id.feed_book_atachment)
    TextView feedBookAtachment;
    @Bind(R.id.feed_book_sll)
    TextView feedBookSll;
    @Bind(R.id.feed_book_sll_to_bb_bcc)
    TextView feedBookSllToBbBcc;
    @Bind(R.id.no_Accidentally)
    TextView noAccidentally;
    @Bind(R.id.cancel)
    TextView cancel;
    private boolean hasShow;
    private static final String KEY_CANCEL = "cancel";

    public static ShakeDialog getInstance(boolean cancelable) {
        ShakeDialog f = new ShakeDialog();
        Bundle args = new Bundle();
        args.putBoolean(KEY_CANCEL, cancelable);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawableResource(R.color.transparent);
        View view = inflater.inflate(R.layout.shake_sensor_layout, container);
        ButterKnife.bind(this, view);
        if (getArguments() == null) {
            setCancelable(true);
        } else {
            setCancelable(getArguments().getBoolean(KEY_CANCEL, true));
        }
        setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                hasShow = false;
            }
        });
        feedbook.setOnClickListener(this);
        feedBookAtachment.setOnClickListener(this);
        feedBookSll.setOnClickListener(this);
        feedBookSllToBbBcc.setOnClickListener(this);
        noAccidentally.setOnClickListener(this);
        feedbook.setOnClickListener(this);
        noAccidentally.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return view;
    }

    public void show(Context context) {
        super.show(context);
        hasShow = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feed_book:
                SendEmailActivity.invoke(getActivity());
                dismiss();
                hasShow = false;
                break;
            case R.id.feed_book_atachment:
                SendAtachmentEmailActivity.invoke(getActivity());
                dismiss();
                hasShow = false;
                break;
            case R.id.feed_book_sll:
                SendSSLEmailActivity.invoke(getActivity());
                dismiss();
                hasShow = false;
                break;
            case R.id.feed_book_sll_to_bb_bcc:
                SendSSLCCAndBCCEmailActivity.invoke(getActivity());
                dismiss();
                hasShow = false;
                break;
            case R.id.no_Accidentally:
                dismiss();
                hasShow = false;
                break;
            case R.id.cancel:
                dismiss();
                hasShow = false;
                break;
            default:
                break;
        }
    }

    public boolean getHasShowState() {
        return hasShow;
    }

}
