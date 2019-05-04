package com.xygit.note.notebook.base;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.xygit.note.notebook.BuildConfig;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.util.BuglyUtil;
import com.xygit.note.notebook.util.SecurityUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * app 全局配置
 *
 * @author Created by xiuyaun
 * @time on 2019/2/25
 */

public class AppConfiguration implements IThirdConfig, Thread.UncaughtExceptionHandler {

    private Application mApplication;

    private AppConfiguration() {
    }

    public static AppConfiguration getInstance() {
        return SingletonHolder.mAppConfiguration;
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    public AppConfiguration initAppConfiguration(Application application) {
        this.mApplication = application;
        if (!BuildConfig.DEBUG) {
            Thread.setDefaultUncaughtExceptionHandler(this);
            SecurityUtil.apkVerify(mApplication);
        }
        Stetho.initializeWithDefaults(application);
//        Stetho.initialize(
//                Stetho.newInitializerBuilder(application)
//                        .enableDumpapp(
//                                Stetho.defaultDumperPluginsProvider(application))
//                        .enableWebKitInspector(
//                                Stetho.defaultInspectorModulesProvider(application))
//                        .build());
        Method[] methods = getClass().getDeclaredMethods();
        if (null != methods) {
            try {
                for (Method method : methods) {
                    if (method.getName().contains("init") && !method.getName().contains("initAppConfiguration")) {
                        method.invoke(this);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return getInstance();
    }


    @Override
    public void initBugly() {
        // 获取当前包名
        String packageName = mApplication.getPackageName();
        // 获取当前进程名
        String processName = BuglyUtil.getProcessName(android.os.Process.myPid());
        CrashReport.UserStrategy userStrategy = new CrashReport.UserStrategy(mApplication);
        // 设置是否为上报进程
        userStrategy.setUploadProcess(processName == null || processName.equals(packageName));
        CrashReport.initCrashReport(mApplication, userStrategy);
    }

    @Override
    public void initHotfix() {
        SophixManager.getInstance().setContext(mApplication)
                .setAppVersion(BuildConfig.VERSION_NAME)
                .setAesKey(null)
                .setEnableDebug(BuildConfig.DEBUG)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            SophixManager.getInstance().cleanPatches();
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }
                    }
                }).initialize();
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        BuglyUtil.logError(mApplication.getPackageName(), e.toString());
        CrashReport.startCrashReport();
    }

    private static class SingletonHolder {
        private static final AppConfiguration mAppConfiguration = new AppConfiguration();
    }


}
