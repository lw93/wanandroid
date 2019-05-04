package com.xygit.note.notebook.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.xygit.note.notebook.BuildConfig;

import java.io.File;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/5
 */

public class FileProviderUtil {

    public static Uri getUriForFile(Context context, File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            fileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static void setIntentDataAndType(Context context, Intent intent, String type, File file) {
        setIntentDataAndType(context, intent, type, file, false);
    }

    public static void setIntentDataAndType(Context context, Intent intent, String type, File file, boolean writeAble) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            intent.setDataAndType(getUriForFile(context, file), type);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type);
        }
    }

}
