package com.xygit.note.notebook.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.util.SparseArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xygit.note.notebook.R;

/**
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class ProgressLoading extends Dialog {
    private int layoutId;
    private ProgressBar progressBar;
    private TextView tvTip;
    private View customView;
    private String tipMessage;
    private SparseArray<String> messages;
    private SparseArray<View.OnClickListener> listeners;

    public ProgressLoading(@NonNull Context context) {
        this(context, R.style.ProgressLoadingStyle);
    }

    public ProgressLoading(@NonNull Context context, @StyleRes int themeResId) {
        this(context, themeResId, R.layout.layout_progress_loading);
    }

    public ProgressLoading(@NonNull Context context, @StyleRes int themeResId, @LayoutRes int layoutId) {
        super(context, themeResId);
        this.layoutId = layoutId;
        this.messages = new SparseArray<String>(7);
        this.listeners = new SparseArray<View.OnClickListener>(7);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutId);
        initView();
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    private void initView() {
        for (int i = 0, size = messages.size(); i < size; i++) {
            int id = messages.keyAt(i);
            View view = findViewById(id);
            if (null != view && view instanceof TextView) {
                ((TextView) view).setText(messages.get(id));
            }
        }
        for (int i = 0, size = listeners.size(); i < size; i++) {
            int id = listeners.keyAt(i);
            View view = findViewById(id);
            if (null != view) {
                view.setOnClickListener(listeners.get(id));
            }
        }
    }

    public void addTipText(String message) {
        addTipText(R.id.tv_tip, message);
    }

    public void addTipText(@IdRes int viewId, String message) {
        this.messages.put(viewId, message);
    }

    public void addOnClickListener(@IdRes int viewId, View.OnClickListener onClickListener) {
        this.listeners.put(viewId, onClickListener);
    }

    public View getCustomView() {
        return customView;
    }

    public void setCustomView(View customView) {
        this.customView = customView;
        setContentView(customView);
    }

}
