package com.sunsun.myapplication.activity;

import android.graphics.Bitmap;
import android.hardware.SensorEvent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sunsun.myapplication.ScreenShot;
import com.sunsun.myapplication.ShakeDialog;
import com.sunsun.myapplication.ShakeSensor;
import com.sunsun.myapplication.ThreadPoolUtils;

import java.io.File;

/**
 * Created by lidexian on 2017/4/24.
 */

public class BaseActivity extends AppCompatActivity {

    private ShakeSensor mShakeSensor;
    private ShakeDialog mShakeDialog;
    private boolean hasFocus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initShakeSensor();
    }


    /**
     * 初始化摇一摇意见反馈
     */
    private void initShakeSensor() {
        mShakeSensor = new ShakeSensor(this, ShakeSensor.DEFAULT_SHAKE_SPEED);
        mShakeSensor.setShakeListener(new ShakeSensor.OnShakeListener() {

            @Override
            public void onShakeComplete(SensorEvent event) {
                if ((mShakeDialog != null && mShakeDialog.getHasShowState()) || !hasFocus) {
                    return;
                }
                try {
                    final Bitmap bitmap = ScreenShot.takeScreenShot(BaseActivity.this);
                    ThreadPoolUtils.getInstance().execute(new Runnable() {

                        @Override
                        public void run() {
                            ScreenShot.deleteSdCardPic();
                            ScreenShot.savePic(bitmap, ScreenShot.getSdCardPath().getAbsolutePath() + File.separator + ScreenShot.PIC_NAME);
                        }
                    });
                    mShakeDialog = ShakeDialog.getInstance(true);
                    mShakeDialog.show(BaseActivity.this);
                } catch (Exception e) {
                    Log.i("ShakeSensor", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        this.hasFocus = hasFocus;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mShakeSensor != null) {
            mShakeSensor.register();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mShakeSensor != null) {
            mShakeSensor.unregister();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mShakeSensor != null) {
            mShakeSensor.ondestory();
        }
        mShakeSensor = null;
        mShakeDialog = null;
    }

}
