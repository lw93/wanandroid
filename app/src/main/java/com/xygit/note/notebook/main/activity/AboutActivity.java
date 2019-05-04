package com.xygit.note.notebook.main.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.xygit.note.notebook.BuildConfig;
import com.xygit.note.notebook.R;
import com.xygit.note.notebook.base.BaseActivity;

/**
 * 搜索页
 *
 * @author Created by xiuyaun
 * @time on 2019/4/7
 */

public class AboutActivity extends BaseActivity {
    private Toolbar toolbar;
    private ImageView ivAppAbout;
    private TextView tvVersionAbout;
    private TextView tvWebTitleAbout;
    private TextView tvWebContentAbout;
    private TextView tvSourceAbout;
    private TextView tvGithubAbout;


    @Override
    public int layoutId() {
        return R.layout.activity_about;
    }


    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        ivAppAbout = (ImageView) findViewById(R.id.iv_app_about);
        tvVersionAbout = (TextView) findViewById(R.id.tv_version_about);
        tvWebTitleAbout = (TextView) findViewById(R.id.tv_web_title_about);
        tvWebContentAbout = (TextView) findViewById(R.id.tv_web_content_about);
        tvSourceAbout = (TextView) findViewById(R.id.tv_source_about);
        tvGithubAbout = (TextView) findViewById(R.id.tv_github_about);
    }

    @Override
    public void initAction() {

    }

    @Override
    public void initData() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (null != actionBar) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setTitle("关于");
        tvVersionAbout.setText(getResources().getString(getApplicationInfo().labelRes) + " V" + BuildConfig.VERSION_NAME);
        tvGithubAbout.setMovementMethod(LinkMovementMethod.getInstance());
        String webContent = "<p>本网站基于WanAndroid开发API开发，随着WanAndroid网站变化而变化，每天会更新相关文章，闲暇时间可以上来学习；同时，非常感谢那些开源的项目及友人。" +
                "当然，未来会根据开放API提供更多便捷的功能...</p>";
        String sourceProject = "<p>本应用开源，仅供大家学习。如果大家有发现什么问题，请到项目中提交issue或者pull request。" +
                "项目将会开源到<a href=\"https://github.com\">Github</a>上，地址：<a href=\"https://github.com/lw93/wanandroid\">https://github.com/lw93/wanandroid</a>。</p>";
        tvWebContentAbout.setText(Html.fromHtml(webContent));
        tvGithubAbout.setText(Html.fromHtml(sourceProject));
    }

    @Override
    public void clearData() {

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
