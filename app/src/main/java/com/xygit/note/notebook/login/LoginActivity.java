package com.xygit.note.notebook.login;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xygit.note.notebook.R;
import com.xygit.note.notebook.api.vo.CommResponse;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.api.vo.LoginResult;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.constant.PreferencesConst;
import com.xygit.note.notebook.main.activity.SearchDetailActivity;
import com.xygit.note.notebook.manager.evenbus.CollectAction;
import com.xygit.note.notebook.manager.evenbus.LoginAction;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.ActivityUtil;
import com.xygit.note.notebook.util.PreferencesUtil;
import com.xygit.note.notebook.view.ProgressLoading;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends BaseActivity implements View.OnClickListener, HttpCallBack {
    private TextInputEditText etUsernameLogin;
    private TextInputLayout ilUsernameLogin;
    private TextInputEditText etPasswordLogin;
    private TextInputLayout ilPasswordLogin;
    private TextInputEditText etRepasswordLogin;
    private TextInputLayout ilRepasswordLogin;
    private Button btnOperator;
    private TextView tvTip;
    private ProgressLoading progressLoading;
    private int startType;
    public static final int START_FROM_RECOMMEND = 0x0001;
    private CommonData collectData;
    private String userName;
    private Toolbar toolbar;

    @Override
    public int layoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initView() {
        toolbar = findViewById(R.id.toolbar);
        etUsernameLogin = (TextInputEditText) findViewById(R.id.et_username_login);
        ilUsernameLogin = (TextInputLayout) findViewById(R.id.il_username_login);
        etPasswordLogin = (TextInputEditText) findViewById(R.id.et_password_login);
        ilPasswordLogin = (TextInputLayout) findViewById(R.id.il_password_login);
        etRepasswordLogin = (TextInputEditText) findViewById(R.id.et_repassword_login);
        ilRepasswordLogin = (TextInputLayout) findViewById(R.id.il_repassword_login);
        btnOperator = (Button) findViewById(R.id.btn_operator);
        tvTip = (TextView) findViewById(R.id.tv_tip);
    }

    @Override
    public void initAction() {
        btnOperator.setOnClickListener(this);
        tvTip.setOnClickListener(this);
        LoginWatcher loginWatcher = new LoginWatcher(etUsernameLogin, etPasswordLogin, null, btnOperator);
        etUsernameLogin.addTextChangedListener(loginWatcher);
        etPasswordLogin.addTextChangedListener(loginWatcher);
    }

    @Override
    public void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("登陆");
        ilRepasswordLogin.setVisibility(View.GONE);
        userName = PreferencesUtil.getDefaultPreferences().getString(PreferencesConst.USER_NAME, "");
        etUsernameLogin.setText(userName);
        progressLoading = new ProgressLoading(this);
        progressLoading.addTipText(R.id.tv_tip, "登陆中...");
        Intent intent = getIntent();
        if (null != intent) {
            startType = intent.getIntExtra(NoteBookConst.INETENT_START_TYPE, 0);
            collectData = (CommonData) intent.getSerializableExtra(NoteBookConst.INETENT_PARAM_DATA);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void clearData() {
        if (progressLoading != null) {
            if (progressLoading.isShowing()) {
                progressLoading.dismiss();
            }
            progressLoading = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_operator: {
                login();
                break;
            }
            case R.id.tv_tip: {
                toRegister();
                break;
            }
            default:
                break;
        }
    }

    private void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        if (startType != 0) {
            intent.putExtra(NoteBookConst.INETENT_START_TYPE, RegisterActivity.START_FROM_LOGIN);
        }
        intent.putExtra(NoteBookConst.INETENT_PARAM_DATA, collectData);
        ActivityUtil.startActivity(this, intent);
        finish();
    }

    private void login() {
        final String username = etUsernameLogin.getText().toString().trim();
        final String password = etPasswordLogin.getText().toString().trim();
        HttpManager.getInstance().login(this, username, password, new CommSubscriber<LoginResult>(this) {
            @Override
            protected void onSucess(CommResponse<LoginResult> loginResultCommResponse) {
                if (TextUtils.isEmpty(PreferencesUtil.getPreference(PreferencesConst.USER_NAME, ""))) {
                    PreferencesUtil.addPreference(PreferencesConst.USER_NAME, username);
                }
                if (TextUtils.isEmpty(PreferencesUtil.getPreference(PreferencesConst.PASSWORD, ""))) {
                    PreferencesUtil.addPreference(PreferencesConst.PASSWORD, password);
                }
                loginSuccess(loginResultCommResponse);
            }
        });
    }

    private void loginSuccess(CommResponse<LoginResult> loginResultCommResponse) {
        LoginAction loginAction = new LoginAction();
        loginAction.setData(loginResultCommResponse.getData());
        EventBus.getDefault().post(loginAction);
        if (startType == START_FROM_RECOMMEND || startType == SearchDetailActivity.START_FROM_SEARCH_DEATAIL) {
            CollectAction collectAction = new CollectAction();
            collectAction.setData(collectData);
            EventBus.getDefault().post(collectAction);
        }
        finish();
    }

    @Override
    public void showProgress() {
        if (!progressLoading.isShowing()) {
            progressLoading.show();
        }
    }

    @Override
    public void hideProgress() {
        if (progressLoading.isShowing()) {
            progressLoading.dismiss();
        }
    }
}
