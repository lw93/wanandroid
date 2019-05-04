package com.xygit.note.notebook.manager.glide;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/16
 */

public class GlideImageLoder extends ImageLoader {
    private static final long serialVersionUID = 64846800772687473L;

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        imageView.setScaleType(ImageView.ScaleType.FIT_XY );
        Glide.with(context).load(path).into(imageView);
    }
}
