package com.xygit.note.notebook.splash;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.xiaomi.ad.common.pojo.AdType;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adv.MiAdvType;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.main.MainActivity;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.util.ToastUtil;

import io.reactivex.functions.Consumer;

public class SplashActivity extends BaseActivity implements Runnable {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private ImageView ivSplash;
    private ViewGroup splashContainer;
    private IAdWorker splashAd;

    @Override
    public int layoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public void initView() {
        ivSplash = findViewById(R.id.iv_splash);
        splashContainer = findViewById(R.id.container_splash);
    }

    @Override
    public void initAction() {
        // 如果api >= 23 需要显式申请权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            RxPermissions rxPermissions = new RxPermissions(this);
            rxPermissions.request(Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) {
                            if (aBoolean) {
                                initAdv();
                                return;
                            }
                            ToastUtil.showToast("友情提示：未授权可能会导致无法使用");
                        }
                    });
        } else {
            initAdv();
        }
    }

    private void initAdv() {
        try {
            splashAd = AdWorkerFactory.getAdWorker(SplashActivity.this, splashContainer, new MimoAdListener() {
                @Override
                public void onAdPresent() {
                    // 开屏广告展示
                    Log.d(TAG, "onAdPresent");
                }

                @Override
                public void onAdClick() {
                    //用户点击了开屏广告
                    Log.d(TAG, "onAdClick");
                }

                @Override
                public void onAdDismissed() {
                    //这个方法被调用时，表示从开屏广告消失。
                    Log.d(TAG, "onAdDismissed");
                    ivSplash.postDelayed(SplashActivity.this, 3 * 1000);
                }

                @Override
                public void onAdFailed(String s) {
                    Log.e(TAG, "onAdFailed : " + s);
                    splashContainer.setVisibility(View.GONE);
                    ivSplash.postDelayed(SplashActivity.this, 3 * 1000);
                }

                @Override
                public void onAdLoaded(int size) {
                    //do nothing
                    splashContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onStimulateSuccess() {

                }
            }, AdType.AD_SPLASH);
            splashAd.loadAndShow(MiAdvType.verticalOpenScreen.getAdvId());
        } catch (Exception e) {
            e.printStackTrace();
            splashContainer.setVisibility(View.GONE);
            ivSplash.postDelayed(SplashActivity.this, 3 * 1000);
        }
    }

    @Override
    public void initData() {

    }

    @Override
    public void clearData() {
        if (splashAd != null) {
            try {
                splashAd.recycle();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            // 捕获back键，在展示广告期间按back键，不跳过广告
            if (splashContainer.getVisibility() == View.VISIBLE) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void run() {
        ActivityUtil.startActivity(this, new Intent(this, MainActivity.class));
        finish();
    }
}
