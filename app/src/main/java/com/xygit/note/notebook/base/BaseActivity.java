package com.xygit.note.notebook.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.xiaomi.ad.common.pojo.AdType;
import com.xygit.note.notebook.adv.MiAdvType;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.spruce.SpruceManager;
import com.xygit.note.notebook.splash.SplashActivity;

/**
 * @author Created by xiuyaun
 * @time on 2018/11/27
 */

public abstract class BaseActivity extends AppCompatActivity implements MimoAdListener {
    @LayoutRes
    public abstract int layoutId();

    public abstract void initView();

    public abstract void initAction();

    public abstract void initData();

    public abstract void clearData();

    private IAdWorker interstitialAdv;

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        initView();
        initAction();
        initData();
        if (this instanceof SplashActivity) {
            return;
        }
        try {
            interstitialAdv = AdWorkerFactory.getAdWorker(this, (ViewGroup) getWindow().getDecorView(), this, AdType.AD_INTERSTITIAL);
            interstitialAdv.load(MiAdvType.verticalInsertScreen.getAdvId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        clearData();
        SpruceManager.getInstance().release();
        HttpManager.getInstance().clearSubscribers();
        try {
            if (null != interstitialAdv) {
                interstitialAdv.recycle();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }

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
    }

    @Override
    public void onAdFailed(String s) {
        Log.e(TAG, "onAdFailed : " + s);
    }

    @Override
    public void onAdLoaded(int i) {
        try {
            interstitialAdv.show();
        } catch (Exception e) {
            Log.e(TAG, "onAdLoaded");
            e.printStackTrace();
        }
    }

    @Override
    public void onStimulateSuccess() {

    }
}
