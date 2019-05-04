package com.xygit.note.notebook.main.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.xygit.note.notebook.R;
import com.xygit.note.notebook.api.CommResponse;
import com.xygit.note.notebook.api.vo.TodoDesc;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.constant.NoteBookConst;
import com.xygit.note.notebook.manager.evenbus.TodoAction;
import com.xygit.note.notebook.manager.net.HttpCallBack;
import com.xygit.note.notebook.manager.net.HttpManager;
import com.xygit.note.notebook.manager.subscriber.CommSubscriber;
import com.xygit.note.notebook.util.DateUtil;
import com.xygit.note.notebook.util.ToastUtil;
import com.xygit.note.notebook.view.ProgressLoading;

import org.greenrobot.eventbus.EventBus;

import java.util.Calendar;
import java.util.Date;

/**
 * TODO更新页
 *
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class UpdateTODOActivity extends BaseActivity implements View.OnClickListener, HttpCallBack, DatePickerDialog.OnDateSetListener {
    public static final int START_TYPE_CREATE = 0x0012;
    public static final int START_TYPE_UPDATE = 0x0013;
    private Toolbar toolbar;
    private TodoDesc todoDesc;
    private EditText tvTitleUpdate;
    private EditText tvDetailUpdate;
    private RadioButton rbCommonUpdate;
    private RadioButton rbImportantUpdate;
    private RadioGroup rgPriorityUpdate;
    private TextView tvDateUpdate;
    private Button btnSaveTodo;
    private ProgressLoading loading;
    private int startType = START_TYPE_CREATE;
    private DatePickerDialog datePicker;

    @Override
    public int layoutId() {
        return R.layout.activity_update_todo;
    }


    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitleUpdate = (EditText) findViewById(R.id.tv_title_update);
        tvDetailUpdate = (EditText) findViewById(R.id.tv_detail_update);
        rbCommonUpdate = (RadioButton) findViewById(R.id.rb_common_update);
        rbImportantUpdate = (RadioButton) findViewById(R.id.rb_important_update);
        rgPriorityUpdate = (RadioGroup) findViewById(R.id.rg_priority_update);
        tvDateUpdate = (TextView) findViewById(R.id.tv_date_update);
        btnSaveTodo = (Button) findViewById(R.id.btn_save_todo);
    }

    @Override
    public void initAction() {
        tvDateUpdate.setOnClickListener(this);
        btnSaveTodo.setOnClickListener(this);
    }

    @Override
    public void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("编辑");
        Intent intent = getIntent();
        if (intent != null) {
            todoDesc = (TodoDesc) intent.getSerializableExtra(NoteBookConst.INETENT_PARAM_DATA);
            startType = intent.getIntExtra(NoteBookConst.INETENT_START_TYPE, START_TYPE_CREATE);
        }
        if (todoDesc != null) {
            String title = todoDesc.getTitle();
            tvTitleUpdate.setText(title);
            tvTitleUpdate.setSelection(title.length());
            String content = todoDesc.getContent();
            tvDetailUpdate.setText(content);
            if (!TextUtils.isEmpty(content)) {
                tvDetailUpdate.setSelection(content.length());
            }
            if (NoteBookConst.TODO_PRIORITY_IMPORTANT == todoDesc.getPriority()) {
                rbImportantUpdate.setChecked(true);
            } else {
                rbCommonUpdate.setChecked(true);
            }
            tvDateUpdate.setText(todoDesc.getDateStr());
        } else {
            tvDateUpdate.setText(DateUtil.strToDate(new Date()));
        }
        loading = new ProgressLoading(this);
        if (startType == START_TYPE_CREATE) {
            loading.addTipText("正在保存中......");
        } else {
            loading.addTipText("正在更新中......");
        }
        Calendar date = Calendar.getInstance();
        datePicker = new DatePickerDialog(this, this, date.get(Calendar.YEAR),
                date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
        datePicker.getDatePicker().setMinDate(date.getTimeInMillis());
//        R.style.DatePickerTheme
//        datePicker.init(date.getYear(),date.getMonth(),date.getDay(),this);
    }

    @Override
    public void clearData() {
        if (todoDesc != null) {
            todoDesc = null;
        }
        if (loading != null) {
            if (loading.isShowing()) {
                loading.dismiss();
            }
            loading = null;
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
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_save_todo) {
            if (prepareParam()) {
                saveTodo();
            }
        } else if (id == R.id.tv_date_update) {
            if (!datePicker.isShowing()) {
                datePicker.show();
            }
        }
    }

    private boolean prepareParam() {
        String title = tvTitleUpdate.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            ToastUtil.showToast("标题不能为空");
            return false;
        }
        String detail = tvDetailUpdate.getText().toString().trim();
        if (startType == START_TYPE_CREATE) {
            todoDesc = new TodoDesc();
            todoDesc.setTitle(title);
            todoDesc.setContent(detail);
            String dateStr = tvDateUpdate.getText().toString().trim();
            todoDesc.setDateStr(dateStr);
            todoDesc.setDate(DateUtil.strToDay(dateStr));
        }
        return true;
    }

    private void saveTodo() {
        String priority = rbCommonUpdate.isChecked() ? "2" : "1";
        if (startType == START_TYPE_CREATE) {
            HttpManager.getInstance().addTODO(this, todoDesc.getTitle(), todoDesc.getContent(), todoDesc.getDateStr(),
                    null, priority, new CommSubscriber<TodoDesc>(this) {
                        @Override
                        protected void onSucess(CommResponse<TodoDesc> todoDescCommResponse) {
                            if (todoDescCommResponse != null) {
                                TodoDesc todoDesc = todoDescCommResponse.getData();
                                TodoAction todoAction = new TodoAction();
                                todoAction.setType(START_TYPE_CREATE);
                                todoAction.setData(todoDesc);
                                EventBus.getDefault().post(todoAction);
                                btnSaveTodo.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 1500);
                            }
                        }
                    });
            return;
        }
        HttpManager.getInstance().updateTODO(this, String.valueOf(todoDesc.getId()), todoDesc.getTitle(), todoDesc.getContent(),
                String.valueOf(todoDesc.getStatus()), todoDesc.getDateStr(), null, priority, new CommSubscriber<TodoDesc>(this) {
                    @Override
                    protected void onSucess(CommResponse<TodoDesc> todoDescCommResponse) {
                        if (todoDescCommResponse != null) {
                            TodoDesc todoDesc = todoDescCommResponse.getData();
                            TodoAction todoAction = new TodoAction();
                            todoAction.setType(START_TYPE_UPDATE);
                            todoAction.setData(todoDesc);
                            EventBus.getDefault().post(todoAction);
                            btnSaveTodo.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 1500);
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        view.updateDate(year, month, dayOfMonth);
        int realMonth = month + 1;
        String day = dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "";
        if (realMonth < 10) {
            tvDateUpdate.setText(year + "-0" + realMonth + "-" + day);
        } else {
            tvDateUpdate.setText(year + "-" + realMonth + "-" + day);
        }
        datePicker.dismiss();
    }
}
