package com.xygit.note.notebook.util;

import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xygit.note.notebook.base.BaseApplication;

/**
 * @author Created by xiuyaun
 * @time on 2019/2/23
 */

public class ToastUtil {
    private static Toast mToast;

    public static Toast getToast() {
        if (null == mToast) {
            mToast = Toast.makeText(BaseApplication.getAppContext(), "", Toast.LENGTH_SHORT);
        }
        return mToast;
    }

    public static void showToast(String text) {
        boolean isMain = LooperUtil.isMainThread();
        if (!isMain) {
            Looper.prepare();
        }
        showToast(text, Gravity.CENTER, Toast.LENGTH_SHORT);
        if (!isMain) {
            Looper.loop();
        }
    }

    public static void showToast(int resId) {
        boolean isMain = LooperUtil.isMainThread();
        if (!isMain) {
            Looper.prepare();
        }
        Toast toast = getToast();
        toast.setText(resId);
        toast.show();
        if (!isMain) {
            Looper.loop();
        }
    }

    public static void showToast(String text, int gravity, int duration) {
        boolean isMain = LooperUtil.isMainThread();
        if (!isMain) {
            Looper.prepare();
        }
        showToast(text, gravity, 0, 0, duration);
        if (!isMain) {
            Looper.loop();
        }
    }

    public static void showToast(String text, int gravity, int xOffset, int yOffset, int duration) {
        boolean isMain = LooperUtil.isMainThread();
        if (!isMain) {
            Looper.prepare();
        }
        Toast toast = getToast();
        toast.setText(text);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(duration);
        toast.show();
        if (!isMain) {
            Looper.loop();
        }
    }


    public static void showToast(View view) {
        boolean isMain = LooperUtil.isMainThread();
        if (!isMain) {
            Looper.prepare();
        }
        Toast toast = getToast();
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
        if (!isMain) {
            Looper.loop();
        }
    }

    public static void showToast(View view, int gravity, int duration) {
        boolean isMain = LooperUtil.isMainThread();
        if (!isMain) {
            Looper.prepare();
        }
        Toast toast = getToast();
        toast.setView(view);
        toast.setGravity(gravity, 0, 0);
        toast.setDuration(duration);
        toast.show();
        if (!isMain) {
            Looper.loop();
        }
    }

    public static void showToast(View view, int gravity, int xOffset, int yOffset, int duration) {
        boolean isMain = LooperUtil.isMainThread();
        if (!isMain) {
            Looper.prepare();
        }
        Toast toast = getToast();
        toast.setView(view);
        toast.setGravity(gravity, xOffset, yOffset);
        toast.setDuration(duration);
        toast.show();
        if (!isMain) {
            Looper.loop();
        }
    }

    public void setToastBackground(int messageColor, int background) {
        View view = getToast().getView();
        if (view != null) {
            TextView message = ((TextView) view.findViewById(android.R.id.message));
            message.setBackgroundResource(background);
            message.setTextColor(messageColor);
        }
    }
}
