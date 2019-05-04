package com.xygit.note.notebook.web;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;

/**
 * @author Created by xiuyaun
 * @time on 2019/3/16
 */

public class JsWebViewClient extends BridgeWebViewClient {

    private TextView tvTitle;
    private ProgressBar progressBar;

    public JsWebViewClient(BridgeWebView webView, TextView textView, ProgressBar progressBar) {
        super(webView);
        this.tvTitle = textView;
        this.progressBar = progressBar;
    }


    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        if (!TextUtils.isEmpty(view.getTitle())) {
            tvTitle.setText(view.getTitle());
        }
        return super.shouldOverrideUrlLoading(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (!TextUtils.isEmpty(view.getTitle())) {
            tvTitle.setText(view.getTitle());
        }
        progressBar.setVisibility(View.VISIBLE);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (!TextUtils.isEmpty(view.getTitle())) {
            tvTitle.setText(view.getTitle());
        }
        progressBar.setVisibility(View.GONE);
        super.onPageFinished(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        if (ERROR_FILE_NOT_FOUND == errorCode || ERROR_BAD_URL == errorCode) {
            view.loadUrl("file:///android_asset/404.html");
        } else if (ERROR_TIMEOUT >= errorCode) {
            view.loadUrl("file:///android_asset/net_error.html");
        } else {
            view.loadUrl("file:///android_asset/hint.html");
        }
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
        if (404 == errorResponse.getStatusCode() || 500 == errorResponse.getStatusCode()) {
            view.loadUrl("file:///android_asset/404.html");
        } else {
            view.loadUrl("file:///android_asset/hint.html");
        }
        super.onReceivedHttpError(view, request, errorResponse);
    }
}
