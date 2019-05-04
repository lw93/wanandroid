package com.xygit.note.notebook.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.spruce.SpruceManager;

/**
 * @author Created by xiuyaun
 * @time on 2018/11/27
 */

public abstract class BaseActivity extends AppCompatActivity {
    @LayoutRes
    public abstract int layoutId();

    public abstract void initView();

    public abstract void initAction();

    public abstract void initData();

    public abstract void clearData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId());
        initView();
        initAction();
        initData();
    }

    @Override
    protected void onDestroy() {
        clearData();
        SpruceManager.getInstance().release();
        HttpManager.getInstance().clearSubscribers();
        super.onDestroy();
    }
}
