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
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.api.vo.CommonData;
import com.xygit.note.notebook.api.vo.LoginResult;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.constant.PreferencesConst;
import com.xygit.note.notebook.manager.evenbus.CollectAction;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.PreferencesUtil;
import com.xygit.note.notebook.view.ProgressLoading;

import org.greenrobot.eventbus.EventBus;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, HttpCallBack {

    public static final int START_FROM_LOGIN = 0x0002;
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
    private CommonData collectData;
    private Toolbar toolbar;

    @Override
    public int layoutId() {
        return R.layout.activity_register;
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
        LoginWatcher loginWatcher = new LoginWatcher(etUsernameLogin, etPasswordLogin, etRepasswordLogin, btnOperator);
        etUsernameLogin.addTextChangedListener(loginWatcher);
        etPasswordLogin.addTextChangedListener(loginWatcher);
        etRepasswordLogin.addTextChangedListener(loginWatcher);
    }

    @Override
    public void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("注册");
        tvTip.setVisibility(View.GONE);
        btnOperator.setText("注册");
        progressLoading = new ProgressLoading(this);
        progressLoading.addTipText(R.id.tv_tip, "注册中...");
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
        if (collectData != null) {
            collectData = null;
        }
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
                register();
                break;
            }
            default:
                break;
        }
    }

    private void register() {
        final String username = etUsernameLogin.getText().toString().trim();
        final String password = etPasswordLogin.getText().toString().trim();
        String repassword = etRepasswordLogin.getText().toString().trim();
        HttpManager.getInstance().register(this,username, password, repassword, new CommSubscriber<LoginResult>(this) {
            @Override
            protected void onSucess(CommResponse<LoginResult> loginResultCommResponse) {
                registerSuccess(loginResultCommResponse);
            }
        });
    }

    private void registerSuccess(CommResponse<LoginResult> loginResultCommResponse) {
        String userName = PreferencesUtil.getDefaultPreferences().getString(PreferencesConst.USER_NAME, "");
        if (TextUtils.isEmpty(userName)) {
            LoginResult result = loginResultCommResponse.getData();
            if (null != result) {
                PreferencesUtil.addPreference(PreferencesConst.USER_NAME, result.getUsername());
            }
        }
        if (startType == START_FROM_LOGIN) {
            CollectAction collectAction = new CollectAction();
            collectAction.setData(collectData);
            EventBus.getDefault().post(collectAction);
        }
        btnOperator.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
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
