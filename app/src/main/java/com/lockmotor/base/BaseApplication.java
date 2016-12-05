package com.lockmotor.base;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.lockmotor.base.baseNetwork.NetworkStateReceiver;
import com.lockmotor.base.utils.DeviceUtils;
import com.lockmotor.base.utils.ImageLoaderUtils;
import com.lockmotor.base.utils.ToastUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BaseApplication extends Application implements
        NetworkStateReceiver.NetworkStateReceiverListener {

    private static final String TAG = "AppUtils";
    private static SimpleDateFormat formatter;
    private static Context appContext;
    private static boolean appState;


    public void onCreate() {
        super.onCreate();

        appContext = this.getApplicationContext();
        formatter = new SimpleDateFormat("E MMM d yyyy hh:mm a", java.util.Locale.getDefault());

        ImageLoaderUtils.configImageLoader(appContext);
        initialiseNetworkManagement();
    }

    public static Context getAppContext() {
        return appContext;
    }

    /**
     * Detect application status:
     *
     * Running or Pausing (Stop application by HomeButton)
     * Note: need set instance is false in onCreate and true in startActivity
     */
    public static void setAppState(boolean bool) {
        appState = bool;
    }

    public static boolean getAppState() {
        return appState;
    }

    public static Bundle getBundleMetaData() {
        try {
            ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(
                    appContext.getPackageName(),
                    PackageManager.GET_META_DATA);

            Bundle bundle = ai.metaData;
            if (bundle == null) {
                ToastUtils.showErrorMessageWithSuperToast("don't get bundle meta data", TAG);
                return null;
            }

            return bundle;
        } catch (PackageManager.NameNotFoundException e) {
            ToastUtils.showErrorMessageWithSuperToast(e.getMessage(), TAG);
        }

        return null;
    }

    /**
     * Network management
     *
     * That's handle will be return network status for changed.
     * Note: you need register network listener in your activity
     */
    private void initialiseNetworkManagement() {
        NetworkStateReceiver networkStateReceiver = new NetworkStateReceiver();
        networkStateReceiver.addListener(this);
        IntentFilter intentFilter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(networkStateReceiver, intentFilter);
    }

    @Override
    public void networkAvailable() {

    }

    @Override
    public void networkUnavailable() {

    }

    /**
     * Application manager
     *
     * Using for get application package, version
     */
    public static String getAppPackageName() {
        Context context = getAppContext();
        return context.getPackageName();
    }

    public static int getAppVersionCode() {
        Context context = getAppContext();

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        } catch (Exception e) {
            // should never happen
            throw new RuntimeException("Unknown expected exception in getAppVersion: " + e);
        }
    }

    public static String getAppVersionName() {
        Context context = getAppContext();

        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        } catch (Exception e) {
            // should never happen
            throw new RuntimeException("Unknown expected exception in getAppVersion: " + e);
        }
    }

    /**
     * Return the first hash generated to be used with Facebook SDK
     *
     * @return Return the first hash generated to be used with Facebook SDK
     */
    public static String showFacebookHash() {
        String hashString = null;

        //Generate Facebook hash
        try {
            Context ctx = getAppContext();
            PackageManager pm = ctx.getPackageManager();
            String packageName = ctx.getPackageName();
            Log.d("Package Name: ", packageName);

            PackageInfo info = pm != null ? pm.getPackageInfo(packageName, PackageManager.GET_SIGNATURES) : null;
            if (info != null && info.signatures != null) {
                for (Signature signature : info.signatures) {
                    MessageDigest md = MessageDigest.getInstance("SHA");
                    md.update(signature.toByteArray());
                    hashString = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                    Log.d("KeyHash: ", hashString);
                }
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return hashString;
    }

    /**
     * Helper function to append text to a log file
     *
     * @param tag  the tag name to log
     * @param text the string to log
     */
    public static void writeToLogFile(String tag, String text) {
        if (text == null || text.isEmpty())
            return;

        boolean canWrite = DeviceUtils.canWriteExternalStorage();
        if (!canWrite)
            return;

        String storagePath = Environment.getExternalStorageDirectory().toString();
        File logFile = new File(storagePath, "/application_logs.txt");

        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                logFile = null;
                e.printStackTrace();
            }
        }
        if (logFile == null) {
            Log.e(TAG, "Failed to create log file");
            return;
        }

        try {
            //BufferedWriter for good performance, 'true' to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));

            buf.append(formatter.format(new Date()));
            if (tag != null && !tag.isEmpty())
                buf.append(tag).append(": ");
            if (!text.isEmpty())
                buf.append(text);

            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper function to check if another app has been installed in user's device
     *
     * @param packageName the package name of the main activity. Eg. com.example.MainActivity
     */
    public static boolean hasOtherAppWithPackage(String packageName) {
        if (packageName == null || packageName.length() <= 0)
            return false;
        Intent intent = getAppContext().getPackageManager().getLaunchIntentForPackage(packageName);
        return intent != null;
    }

}
