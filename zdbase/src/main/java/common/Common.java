package common;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.socks.library.KLog;

import java.io.File;

/**
 * 书写一个类，用来完成项目内部公共的方法
 * Created by CJ on 2016/12/13 0013.
 */

public class Common {

    /**
     * 获取Toolbar的透明值
     *
     * @param maxHeight 滚动到多少时变不透明
     * @param scrollY   滚动高度
     * @return
     */
    public static int getToolbarAlpha(int maxHeight, int scrollY) {
        scrollY = scrollY < 0 ? 0 : scrollY;
        float rate = (float) scrollY / maxHeight;
        if (rate > 1) {
            rate = 1;
        }
        return (int) (255 * rate);
    }

    /**
     * 皮肤缓存路径
     *
     * @param context
     * @return
     */
    public static File getSkinCachePath(Context context) {
        String dir = Environment.getExternalStorageDirectory() + File.separator + context.getPackageName() + File.separator;
        File newFile = new File(dir, "skin");
        if (!newFile.exists()) {
            newFile.mkdirs();
        }
        return newFile;
    }

    /**
     * 相机缓存路径
     *
     * @param context
     * @return
     */
    public static File getCameraCachePath(Context context) {
        File dir = getCacheFile(context);
        File newFile = new File(dir, "camera");
        if (!newFile.exists()) {
            newFile.mkdir();
        }
        return newFile;
    }

    /**
     * 剪切缓存路径
     *
     * @param context
     * @return
     */
    public static File getCropCachePath(Context context) {
        File dir = getCacheFile(context);
        File newFile = new File(dir, "crop");
        if (!newFile.exists()) {
            newFile.mkdir();
        }
        return newFile;
    }

    /**
     * 获取缓存文件夹
     *
     * @param context
     * @return
     */
    public static File getCacheFile(@NonNull Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = null;
            file = context.getExternalCacheDir();
            if (file == null) {
                file = new File(getCacheFilePath());
                makeDirs(file);
            }
            return file;
        } else {
            return context.getCacheDir();
        }
    }

    /**
     * 获取自定义缓存文件地址
     *
     * @return
     */
    public static String getCacheFilePath() {
        return "/mnt/sdcard/wangwang";
    }

    /**
     * 获取照片缓存路径
     *
     * @param context
     * @return
     */
    public static File getPhotoCachePath(Context context) {
        File dir = getCacheFile(context);
        File newFile = new File(dir, "capture");
        if (!newFile.exists()) {
            KLog.i("photo cache path make successful : " + newFile.mkdir());
        }
        return newFile;
    }

    /**
     * 创建未存在的文件夹
     *
     * @param file
     * @return
     */
    public static File makeDirs(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public static File createPhotoDir(Context context) {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File destDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            if (!destDir.exists()) {
                boolean succ = destDir.mkdirs();//在根创建了文件夹ABC
                KLog.d(succ);
            } else {
                KLog.d("dir : " + destDir.getAbsolutePath());
            }
            return destDir;
        } else {
            Toast.makeText(context, "未发现储存卡", Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    /**
     * 背景图片缓存路径
     *
     * @param context
     * @return
     */
    public static File getBackgroundCachePath(Context context) {
        File dir = getCacheFile(context);
        File newFile = new File(dir, "background");
        if (!newFile.exists()) {
            newFile.mkdir();
        }
        return newFile;
    }


    /**
     * 背景图片缓存路径
     *
     * @param context
     * @return
     */
    public static File getVoiceCachePath(Context context) {
        File dir = getCacheFile(context);
        File newFile = new File(dir, "voice");
        if (!newFile.exists()) {
            newFile.mkdir();
        }
        return newFile;
    }
}