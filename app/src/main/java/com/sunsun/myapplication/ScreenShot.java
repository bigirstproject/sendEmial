package com.sunsun.myapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lidexian on 2017/4/6.
 * <p>
 * 获取当前activity的截屏(DecorView)
 */

public class ScreenShot {

    private static final String TAG = ScreenShot.class.getSimpleName();

    public static final String PIC_NAME = "temp.jpg";

    /**
     * sd 获取跟目录
     *
     * @return
     */
    public static File getSdCardPath() {
        File sdDir = Environment.getExternalStorageDirectory();
        return sdDir;
    }

    /**
     * 判断sd卡是否存在
     *
     * @return
     */
    public static boolean isExistSdCard() {
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }

    /**
     * 删除temp图片
     *
     * @return
     */
    public static boolean deleteSdCardPic() {
        if (ScreenShot.isExistSdCard()) {
            File file = new File(ScreenShot.getSdCardPath() + File.separator + ScreenShot.PIC_NAME);
            if (file.exists()) {
                return file.delete();
            }
        }
        return false;
    }


    /**
     * 获取当前activity截屏
     *
     * @param activity
     */
    public static void SaveScreenShotshoot(Activity activity) {
        if (isExistSdCard()) {
            ScreenShot.savePic(ScreenShot.takeScreenShot(activity), getSdCardPath().getAbsolutePath() + File.separator + PIC_NAME);
        }
    }

    /**
     * 获取指定Activity的截屏，保存到jpg文件
     *
     * @param activity
     * @return
     */
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay()
                .getHeight();
        // 去掉标题栏
        // Bitmap b = Bitmap.createBitmap(b1, 0, 25, 320, 455);
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * 保存到sdcard
     *
     * @param b
     * @param strFileName
     */
    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
