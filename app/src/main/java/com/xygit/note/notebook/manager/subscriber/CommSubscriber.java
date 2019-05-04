package com.xygit.note.notebook.manager.subscriber;

import android.content.Context;
import android.content.Intent;

import com.xygit.note.notebook.base.BaseSubscriber;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/16
 */

public abstract class CommSubscriber<T> extends BaseSubscriber<T> {

    public CommSubscriber(Context context) {
        super(context);
    }

    public CommSubscriber(Context context, Intent intent) {
        super(context, true, intent);
    }

    public CommSubscriber(Context context, boolean autoHandle, Intent intent) {
        super(context, autoHandle, intent);
    }
}
