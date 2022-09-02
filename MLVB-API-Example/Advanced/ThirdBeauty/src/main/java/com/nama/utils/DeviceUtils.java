package com.nama.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.opengl.GLES20;
import android.os.Build;

import java.io.File;
import java.util.Arrays;
import java.util.Locale;

/**
 * 设备信息的工具类，可以获取设备的信息，由于安全合规问题，必须控制设备信息的获取时机和获取次数。
 */
public final class DeviceUtils {

    private DeviceUtils() {
    }

    /**
     * 获取设备信息，包括 Brand、Hardware、Product 等等。
     *
     * @param context The application context.
     * @return 返回当前设备的整体信息。
     */
    public static String retrieveDeviceInfo(Context context) {
        return "Manufacturer: " + Build.MANUFACTURER + ", Model: " + Build.MODEL + ", Brand: " + Build.BRAND
                + ", Product: " + Build.PRODUCT + ", Hardware: " + Build.HARDWARE + ", Board: " + Build.BOARD
                + ", Build ID: " + Build.DISPLAY + ", Screen Width: " + context.getResources()
                .getDisplayMetrics().widthPixels + ", Screen Height: " + context.getResources()
                .getDisplayMetrics().heightPixels + ", Language: " + Locale.getDefault().getLanguage()
                + ", Android Version: " + Build.VERSION.RELEASE + ", Android SDK: " + Build.VERSION.SDK_INT
                + ", Supported ABIs: " + getSupportedAbis() + ", Main ABI: " + getMainAbi(context) + ", GL Vendor: "
                + GLES20.glGetString(GLES20.GL_VENDOR) + ", GL Renderer: " + GLES20.glGetString(GLES20.GL_RENDERER)
                + ", GL Version : " + GLES20.glGetString(GLES20.GL_VERSION) + ", App Name: " + context
                .getPackageManager().getApplicationLabel(context.getApplicationInfo()) + ", App Package Name: "
                + context.getPackageName() + ", App VersionName: " + getAppVersionName(context) + ", App VersionCode: "
                + getAppVersionCode(context);
    }

    private static String getAppVersionName(Context context) {
        try {
            String packageName = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    private static int getAppVersionCode(Context context) {
        try {
            String packageName = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return -1;
        }
    }

    private static String getSupportedAbis() {
        String supportedAbis;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportedAbis = Arrays.toString(Build.SUPPORTED_ABIS);
        } else {
            String cpuAbi = Build.CPU_ABI;
            String cpuAbi2 = Build.CPU_ABI2;
            supportedAbis = "[" + cpuAbi + ", " + cpuAbi2 + "]";
        }
        return supportedAbis;
    }

    private static String getMainAbi(Context context) {
        String nativeLibraryDir = context.getApplicationInfo().nativeLibraryDir;
        String abi = nativeLibraryDir.substring(nativeLibraryDir.lastIndexOf(File.separatorChar) + 1);
        return abi;
    }

}
