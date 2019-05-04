package com.xygit.note.notebook.manager.spruce;

import android.animation.Animator;
import android.view.ViewGroup;

import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;
import com.willowtreeapps.spruce.sort.SortFunction;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/10
 */

public class SpruceManager {

    private Animator mSpruceAnimator;

    private SpruceManager() {
    }

    public static SpruceManager getInstance() {
        return SpruceManagerHelp.mSpruceManager;
    }

    public void build(ViewGroup parentViewGroup) {
        build(parentViewGroup, new DefaultSort(50L), DefaultAnimations.shrinkAnimator(parentViewGroup, 800));
    }

    public void build(ViewGroup parentViewGroup, SortFunction sortFunction) {
        build(parentViewGroup, sortFunction, DefaultAnimations.shrinkAnimator(parentViewGroup, 800));
    }

    public void build(ViewGroup parentViewGroup, SortFunction sortFunction, Animator... animators) {
        if (null != mSpruceAnimator) {
            if (mSpruceAnimator.isRunning()) {
                mSpruceAnimator.cancel();
            }
            mSpruceAnimator = null;
        }
        mSpruceAnimator = new Spruce
                .SpruceBuilder(parentViewGroup)
                .sortWith(sortFunction)
                .animateWith(animators)
                .start();
    }

    public void start() {
        if (null != mSpruceAnimator) {
            mSpruceAnimator.start();
        }
    }


    public void release() {
        if (null != mSpruceAnimator) {
            if (mSpruceAnimator.isRunning()) {
                mSpruceAnimator.cancel();
            }
            mSpruceAnimator = null;
        }
    }

    public Animator getSpruceAnimator() {
        return mSpruceAnimator;
    }

    private static class SpruceManagerHelp {
        private static final SpruceManager mSpruceManager = new SpruceManager();
    }
}
