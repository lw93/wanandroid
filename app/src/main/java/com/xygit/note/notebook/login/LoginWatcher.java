package com.xygit.note.notebook.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

/**
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class LoginWatcher implements TextWatcher {

    private EditText etUserName;
    private EditText etPassword;
    private EditText etRePassword;
    private Button btnOperator;

    public LoginWatcher(EditText etUserName, EditText etPassword, EditText etRePassword, Button btnOperator) {
        this.etUserName = etUserName;
        this.etPassword = etPassword;
        this.etRePassword = etRePassword;
        this.btnOperator = btnOperator;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (null != etUserName && TextUtils.isEmpty(etUserName.getText())) {
            if (btnOperator.isEnabled()) {
                btnOperator.setEnabled(false);
            }
            return;
        }
        if (null != etPassword && TextUtils.isEmpty(etPassword.getText())) {
            if (btnOperator.isEnabled()) {
                btnOperator.setEnabled(false);
            }
            return;
        }
        if (etRePassword == null) {
            if (!btnOperator.isEnabled()) {
                btnOperator.setEnabled(true);
            }
            return;
        }
        if (TextUtils.isEmpty(etRePassword.getText())) {
            if (btnOperator.isEnabled()) {
                btnOperator.setEnabled(false);
            }
            return;
        }
        if (!btnOperator.isEnabled()) {
            btnOperator.setEnabled(true);
        }
    }
}
