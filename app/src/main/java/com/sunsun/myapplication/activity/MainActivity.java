package com.sunsun.myapplication.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sunsun.myapplication.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();


    @Bind(R.id.btn1)
    Button btn1;
    @Bind(R.id.btn2)
    Button btn2;
    @Bind(R.id.btn3)
    Button btn3;
    @Bind(R.id.btn4)
    Button btn4;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.bind(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                SendEmailActivity.invoke(this);
                break;
            case R.id.btn2:
                SendAtachmentEmailActivity.invoke(this);
                break;
            case R.id.btn3:
                SendSSLEmailActivity.invoke(this);
                break;
            case R.id.btn4:
                SendSSLCCAndBCCEmailActivity.invoke(this);
                break;
        }
    }


}