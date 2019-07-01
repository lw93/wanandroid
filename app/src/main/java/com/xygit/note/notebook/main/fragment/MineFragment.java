package com.xygit.note.notebook.main.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory;
import com.miui.zeus.mimo.sdk.ad.IAdWorker;
import com.miui.zeus.mimo.sdk.listener.MimoAdListener;
import com.xiaomi.ad.common.pojo.AdType;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.BaseRecyclerAdapter;
import com.xygit.note.notebook.adv.MiAdvType;
import com.xygit.note.notebook.api.vo.CommResponse;
import com.xygit.note.notebook.api.vo.LoginResult;
import com.xygit.note.notebook.base.BaseApplication;
import com.xygit.note.notebook.base.BaseFragment;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.constant.PreferencesConst;
import com.xygit.note.notebook.entity.MineFunction;
import com.xygit.note.notebook.login.LoginActivity;
import com.xygit.note.notebook.main.activity.AboutActivity;
import com.xygit.note.notebook.main.activity.CollectionActivity;
import com.xygit.note.notebook.main.activity.TODOActivity;
import com.xygit.note.notebook.main.adapter.MineFunctionAdapter;
import com.xygit.note.notebook.manager.evenbus.LoginAction;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.util.PreferencesUtil;
import com.xygit.note.notebook.util.ToastUtil;
import com.xygit.note.notebook.view.ProgressLoading;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的页
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment implements View.OnClickListener, BaseRecyclerAdapter.OnItemClickListener, HttpCallBack, SharedPreferences.OnSharedPreferenceChangeListener {

    private CircleImageView ivHeader;
    private TextView tvLoginStatus;
    private RecyclerView rvFunctions;
    private List<MineFunction> functions;
    private MineFunctionAdapter functionAdapter;
    private ProgressLoading loading;
    private ViewGroup containerMine;
    private IAdWorker advBanner;

    @Override
    public int layoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        ivHeader = rootView.findViewById(R.id.iv_header_mine);
        tvLoginStatus = rootView.findViewById(R.id.tv_login_mine);
        rvFunctions = rootView.findViewById(R.id.rv_function_mine);
        containerMine = rootView.findViewById(R.id.container_mine);
    }

    @Override
    public void initAction() {
        ivHeader.setOnClickListener(this);
        EventBus.getDefault().register(this);
        BaseApplication.getDefaultPreferences().registerOnSharedPreferenceChangeListener(this);
        initBanner();
    }

    private void initBanner() {
        try {
            advBanner = AdWorkerFactory.getAdWorker(getActivity(), null, new MimoAdListener() {
                @Override
                public void onAdPresent() {

                }

                @Override
                public void onAdClick() {

                }

                @Override
                public void onAdDismissed() {

                }

                @Override
                public void onAdFailed(String s) {

                }

                @Override
                public void onAdLoaded(int i) {
                    if (i >= 1) {
                        containerMine.removeAllViews();
                        try {
                            containerMine.addView(advBanner.updateAdView(null, 0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onStimulateSuccess() {

                }
            }, AdType.AD_BANNER);
            advBanner.load(MiAdvType.incentiveAdvertising.getAdvId());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initData() {
        functions = new ArrayList<>(4);
        functions.add(new MineFunction(R.drawable.ic_star_white_48dp, "收藏"));
        functions.add(new MineFunction(R.drawable.ic_assignment_white_48dp, "TODO"));
        functions.add(new MineFunction(R.drawable.ic_info_white_48dp, "关于"));
        functions.add(new MineFunction(R.drawable.ic_off_white_48dp, "退出"));
        loading = new ProgressLoading(ivHeader.getContext());
        loading.addTipText("正在退出...");
        functionAdapter = new MineFunctionAdapter(functions, rvFunctions);
        rvFunctions.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFunctions.addItemDecoration(new DividerItemDecoration(rvFunctions.getContext(), LinearLayout.VERTICAL));
        rvFunctions.setAdapter(functionAdapter);
        functionAdapter.setOnItemClickListener(this);
        tvLoginStatus.setText(PreferencesUtil.getPreference(PreferencesConst.USER_NAME, "登陆"));
    }

    @Override
    public void clearData() {
        if (functions != null) {
            functions.clear();
            functions = null;
        }
        if (functionAdapter != null) {
            functionAdapter.clear();
            functionAdapter = null;
        }
        if (loading != null) {
            if (loading.isShowing()) {
                loading.dismiss();
            }
            loading = null;
        }
        if (advBanner != null) {
            try {
                advBanner.recycle();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        EventBus.getDefault().unregister(this);
        BaseApplication.getDefaultPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        ActivityUtil.startActivity(getActivity(), new Intent(getActivity(), LoginActivity.class));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        String userName = tvLoginStatus.getText().toString();
        if (!"登陆".equals(userName)) {
            outState.putString(PreferencesConst.USER_NAME, userName);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            String userName = savedInstanceState.getString(PreferencesConst.USER_NAME);
            if (!TextUtils.isEmpty(userName)) {
                tvLoginStatus.setText(userName);
            }
        }
    }

    @Subscribe
    public void loginAction(LoginAction loginAction) {
        if (loginAction != null) {
            LoginResult loginResult = loginAction.getData();
            if (loginResult != null) {
                tvLoginStatus.setText(loginResult.getUsername());
            }
        }
    }

    @Override
    public void onItemClick(RecyclerView.Adapter adapter, View view, int position) {
        if (position == 0) {
            boolean haslogin = hasLogin();
            if (haslogin) {
                ActivityUtil.startActivity(getActivity(), new Intent(getActivity(), CollectionActivity.class));
            }
        } else if (position == 2) {
            ActivityUtil.startActivity(getActivity(), new Intent(getActivity(), AboutActivity.class));
        } else if (position == 3) {
            toLogOut();
        } else {
            boolean haslogin = hasLogin();
            if (haslogin) {
                ActivityUtil.startActivity(getActivity(), new Intent(getActivity(), TODOActivity.class));
            }
        }
    }

    //判断是否已登陆
    private boolean hasLogin() {
        if (!"登陆".equals(tvLoginStatus.getText().toString())) {
            return true;
        }
        ToastUtil.showToast("请先登陆");
        ActivityUtil.startActivity(getActivity(), new Intent(getActivity(), LoginActivity.class));
        return false;
    }

    private void toLogOut() {
        if ("登陆".equals(tvLoginStatus.getText().toString())) {
            ToastUtil.showToast("您已退出登陆");
            return;
        }
        HttpManager.getInstance().logout(this, new CommSubscriber<Object>(getActivity()) {
            @Override
            protected void onSucess(CommResponse<Object> objectCommResponse) {
                if (objectCommResponse != null) {
                    if (NoteBookConst.RESPONSE_SUCCESS == objectCommResponse.getErrorCode()) {
                        tvLoginStatus.setText("登陆");
                        PreferencesUtil.removePreference(PreferencesConst.USER_NAME);
                        PreferencesUtil.removePreference(PreferencesConst.PASSWORD);
                        EventBus.getDefault().post(new LoginAction());
                    }
                }
            }
        });
    }

    @Override
    public void showProgress() {
        if (!loading.isShowing()) {
            loading.show();
        }
    }

    @Override
    public void hideProgress() {
        if (loading.isShowing()) {
            loading.dismiss();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (TextUtils.isEmpty(sharedPreferences.getString(PreferencesConst.USER_NAME, ""))) {
            tvLoginStatus.setText("登陆");
        } else {
            tvLoginStatus.setText(sharedPreferences.getString(PreferencesConst.USER_NAME, "登陆"));
        }
    }
}
