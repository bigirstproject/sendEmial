package com.sunsun.myapplication;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.List;

/**
 * Created by jesse on 3/31/16.
 */
public class PlatformUtils {

    private PlatformUtils() {
        //unused
    }

    /**
     * is debug version
     *
     * @param context
     * @return
     */
    public static boolean isDebugVersion(Context context) {
        try {
            String pkgName = context.getPackageName();
            PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(
                    pkgName, PackageManager.GET_ACTIVITIES);
            if (pkgInfo != null) {
                ApplicationInfo info = pkgInfo.applicationInfo;
                return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            }

        } catch (Exception e) {
            Log.e("PlatformUtils", e.getMessage());
        }
        return false;
    }

    /**
     * is network available
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mConnMan = (ConnectivityManager) context.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnMan.getActiveNetworkInfo();
        if (info == null) {
            return false;
        }
        return info.isConnected();
    }

    /**
     * is wifi connected
     */
    public static boolean isWifiConn(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo == null)
            return false;
        return networkInfo.isConnected();
    }

    /**
     * is sdcard available
     *
     * @return
     */
    public static boolean isSDCardAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getDeviceId(Context ctx) {
        String deviceId;
        StringBuilder sbDeviceMeta = new StringBuilder();

        String imei = getIMEI(ctx);
        if (imei != null) {
            sbDeviceMeta.append(imei.trim());
        }

        String mac = getMac(ctx);
        if (mac != null) {
            sbDeviceMeta.append(mac.trim());
        }

        deviceId = EncryptionUtils.toMD5(sbDeviceMeta.toString());
        byte[] bytes = deviceId.getBytes();
        int sum1 = 0, sum2 = 0;
        for (int i = 0; i < bytes.length; i++) {
            if (i % 2 == 0) {
                sum1 += bytes[i];
            } else {
                sum2 += bytes[i];
            }
        }

        final char[] codes = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int checkSum1 = sum1 % codes.length;
        int checkSum2 = sum2 % codes.length;
        deviceId = deviceId + codes[checkSum1] + codes[checkSum2];

        return deviceId;
    }

    public static String getMac(Context context) {
        String mac = "";
        WifiManager manager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (manager != null) {
            WifiInfo info = manager.getConnectionInfo();
            if (info != null) {
                mac = info.getMacAddress();
            }
        }

        return mac;
    }

    public static String getIMEI(Context context) {
        String imei = "";
        TelephonyManager manager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (manager != null) {
            try {
                imei = manager.getDeviceId();
            } catch (Exception e) {
                Log.e("PlatformUtils", e.getMessage());
            }
        }

        return imei;
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                return appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
            }
        }
        return false;
    }

    public static String getVersionName(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("PlatformUtils", e.getMessage());
        }

        return String.valueOf(100);
    }

    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("PlatformUtils", e.getMessage());
        }

        return 1;
    }


    public static int getSDKInt() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return 手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

}
