package com.oconte.david.go4lunch.restodetails;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.oconte.david.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewActivity extends AppCompatActivity {

    // FOR DESIGN
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.web_view_all_new) WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        ButterKnife.bind(this);

        this.configureWebView();
    }



    /**
     * It's for manage the webView after click on one article for see the details
     */
    protected void configureWebView() {
        WebView mWebView = (WebView) findViewById(R.id.web_view_all_new);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                findViewById(R.id.web_view_all_new).setVisibility(View.VISIBLE);
            }
        });
        mWebView.loadUrl(getUrl());
    }

    public String getUrl() {
        return getIntent().getStringExtra("url2");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
