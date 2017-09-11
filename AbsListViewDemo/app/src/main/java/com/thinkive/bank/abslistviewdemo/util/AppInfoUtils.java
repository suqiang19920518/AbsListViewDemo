package com.thinkive.bank.abslistviewdemo.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author: sq
 * @date: 2017/7/21
 * @corporation: 深圳市思迪信息科技有限公司
 * @description: 获取App应用相关信息
 */
public class AppInfoUtils {
    private static Object lock = new Object();
    private static AppInfoUtils mInstance;
    private DisplayMetrics displayMetrics = null;
    private Context mContext;

    private AppInfoUtils(Context context) {
        mContext = context;
    }

    public static AppInfoUtils getApp(Context context) {
        if (mInstance == null) {
            synchronized (lock) {
                if (mInstance == null) {
                    mInstance = new AppInfoUtils(context);
                }
            }
        }
        return mInstance;
    }

    /**
     * 获取App唯一标识
     */
    public String getAppId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取应用app名
     *
     * @return
     */
    public String getAppName() {
        ApplicationInfo applicationInfo = mContext.getApplicationInfo();
        String appName = (String) mContext.getPackageManager().getApplicationLabel(applicationInfo);
        return appName;
    }

    /**
     * 获取包信息
     *
     * @param context
     * @return
     */
    public PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        String packageName = context.getPackageName();

        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo;
        } catch (PackageManager.NameNotFoundException var4) {
            return packageInfo;
        }
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null ? packageInfo.versionName : "";
    }

    /**
     * 获取版本号
     *
     * @param context
     * @return
     */
    public int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        return packageInfo != null ? packageInfo.versionCode : 1;
    }

    /**
     * 获取当前系统SDK版本号
     */
    public static int getSystemVersion() {
        /*获取当前系统的android版本号*/
        int version = android.os.Build.VERSION.SDK_INT;
        return version;
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 启动app
     *
     * @param context
     * @param packageName
     */
    public void startApp(Context context, String packageName) {
        new Intent();
        PackageManager packageManager = context.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        intent.setFlags(268435456);
        context.startActivity(intent);
    }

    /**
     * 判断APP是否在后台运行
     *
     * @param context
     * @return
     */
    public static boolean isAppBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
//              BACKGROUND=400 EMPTY=500 FOREGROUND=100 GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                Log.i(context.getPackageName(), "此appimportace =" + appProcess.importance + ",context.getClass().getName()=" + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//					Log.i(context.getPackageName(), "处于后台" + appProcess.processName);
                    return true;
                } else {
//					Log.i(context.getPackageName(), "处于前台" + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 判断当前进程是否为主进程
     *
     * @param context
     * @return
     */
    public boolean isMainProcess(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List appProcesses = am.getRunningAppProcesses();
        String name = context.getPackageName();
        int pid = Process.myPid();
        Iterator it = appProcesses.iterator();

        ActivityManager.RunningAppProcessInfo processInfo;
        do {
            if (!it.hasNext()) {
                return false;
            }

            processInfo = (ActivityManager.RunningAppProcessInfo) it.next();
        } while (processInfo.pid != pid || !name.equals(processInfo.processName));

        return true;
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public float getScreenDensity() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(mContext.getApplicationContext().getResources().getDisplayMetrics());
        }
        return this.displayMetrics.density;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getScreenHeight() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(mContext.getApplicationContext().getResources().getDisplayMetrics());
        }
        return this.displayMetrics.heightPixels;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        if (this.displayMetrics == null) {
            setDisplayMetrics(mContext.getApplicationContext().getResources().getDisplayMetrics());
        }
        return this.displayMetrics.widthPixels;
    }

    public void setDisplayMetrics(DisplayMetrics DisplayMetrics) {
        this.displayMetrics = DisplayMetrics;
    }

    public int dp2px(float f) {
        return (int) (0.5F + f * getScreenDensity());
    }

    public int px2dp(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }

    //获取应用的data/data/....File目录
    public String getFilesDirPath() {
        return mContext.getApplicationContext().getFilesDir().getAbsolutePath();
    }

    //获取应用的data/data/....Cache目录
    public String getCacheDirPath() {
        return mContext.getApplicationContext().getCacheDir().getAbsolutePath();
    }


}
