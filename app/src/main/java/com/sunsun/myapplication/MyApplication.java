package com.sunsun.myapplication;


import android.app.Application;
import android.os.Environment;

import com.sunsun.nativelogger.NLogger;
import com.sunsun.nativelogger.logger.LoggerLevel;
import com.sunsun.nativelogger.util.CrashWatcher;

/**
 * Created by lidexian on 2017/4/24.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init(){
        NLogger.getInstance()
                .builder()
                .tag("sunsun")
                .loggerLevel(LoggerLevel.DEBUG)
                .catchException(true, new CrashWatcher.UncaughtExceptionListener() {
                    @Override
                    public void uncaughtException(Thread thread, Throwable ex) {
                        NLogger.e("uncaughtException", ex);
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .fileLogger(true)
                .fileDirectory(Environment.getExternalStorageDirectory().getPath() + "/" +
                        this.getPackageName() + "/logs")
                .expiredPeriod(4)
                .build();
    }

}
