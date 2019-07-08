package com.xygit.note.notebook.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.spruce.SpruceManager;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/15
 */

public abstract class BaseFragment extends Fragment {

    protected View rootView;

    @LayoutRes
    public abstract int layoutId();

    public abstract void initView();

    public abstract void initAction();

    public abstract void initData();

    public abstract void clearData();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(layoutId(), container, false);
        initView();
        initAction();
        initData();
        return rootView;
    }

    @Override
    public void onDestroy() {
        clearData();
        SpruceManager.getInstance().release();
        HttpManager.getInstance().clearSubscribers();
        super.onDestroy();
    }
}
