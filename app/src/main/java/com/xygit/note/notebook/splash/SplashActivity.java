package com.xygit.note.notebook.splash;

import android.content.Intent;
import android.widget.ImageView;

import com.xygit.note.notebook.BuildConfig;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.jni.JniHelper;
import com.xygit.note.notebook.main.MainActivity;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.util.BuglyUtil;

public class SplashActivity extends BaseActivity implements Runnable {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private ImageView ivSplash;

    @Override
    public int layoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        ivSplash = findViewById(R.id.iv_splash);
    }

    @Override
    public void initAction() {
        if (!BuildConfig.DEBUG && !JniHelper.debuggable(this)) {
            BuglyUtil.logWarn(TAG, "jni-> 程序被修改为可调试状态！！！");
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        ivSplash.postDelayed(this, 3 * 1000);
    }

    @Override
    public void initData() {

    }

    @Override
    public void clearData() {

    }

    @Override
    public void run() {
        ActivityUtil.startActivity(this, new Intent(this, MainActivity.class));
        finish();
    }
}
