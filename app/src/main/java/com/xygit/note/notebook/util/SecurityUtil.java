package com.xygit.note.notebook.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * security
 *
 * @author Created by xiuyaun
 * @time on 2019/3/5
 */

public class SecurityUtil {

    private static final String TAG = SecurityUtil.class.getSimpleName();
    private static final int TEST = 495210525;

    public static void apkVerify(Application app) {
        if (0 != (app.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE) || isRunningInEmualtor()) {
            BuglyUtil.logWarn(TAG, "程序被修改为可调试状态！！！");
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public static boolean isRunningInEmualtor() {
        boolean qemuKernel = false;
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("getprop ro.kernel.qemu");
            os = new DataOutputStream(process.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
            qemuKernel = (Integer.valueOf(in.readLine()) == 1);
            BuglyUtil.logWarn(TAG, "检测到模拟器:" + qemuKernel);
        } catch (Exception e) {
            qemuKernel = false;
            BuglyUtil.logWarn(TAG, "run failed" + e.getMessage());
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception ignored) {

            }
            BuglyUtil.logWarn(TAG, "run finally");
        }
        return qemuKernel;
    }

    public static int getSignature(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            Log.i(TAG, sign.hashCode() + "");
            return sign.hashCode();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getSignature(Activity context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            MessageDigest md1 = MessageDigest.getInstance("SHA1");
            md1.update(sign.toByteArray());
            byte[] digest2 = md1.digest();
            return byte2hex(digest2);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    public static void apkVerifyWithSHA(Context context, String baseSHA) {
        String apkPath = context.getPackageCodePath(); // 获取Apk包存储路径
        try {
            MessageDigest dexDigest = MessageDigest.getInstance("SHA-1");
            byte[] bytes = new byte[1024];
            int byteCount;
            FileInputStream fis = new FileInputStream(new File(apkPath)); // 读取apk文件
            while ((byteCount = fis.read(bytes)) != -1) {
                dexDigest.update(bytes, 0, byteCount);
            }
            BigInteger bigInteger = new BigInteger(1, dexDigest.digest()); // 计算apk文件的哈希值
            String sha = bigInteger.toString(16);
            fis.close();
            if (!sha.equals(baseSHA)) { // 将得到的哈希值与原始的哈希值进行比较校验
                android.os.Process.killProcess(android.os.Process.myPid()); // 验证失败则退出程序
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
