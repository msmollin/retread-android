package com.treadly.Treadly.UI.Util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.facebook.common.util.UriUtil;
import com.treadly.Treadly.R;
import com.treadly.Treadly.UI.Util.BaseClasses.BaseActivity;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class NormalWebViewActivity extends BaseActivity {
    private static final String TAG = "NormalWebViewActivity";
    ImageView leftBtn;
    private WebView mWebview;
    String title;
    LinearLayout titleLayer;
    TextView titleTv;
    String url;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_normal_webview);
        this.title = getIntent().getStringExtra("title");
        this.url = getIntent().getStringExtra("url");
        this.leftBtn = (ImageView) findViewById(R.id.leftBtn);
        this.leftBtn.setOnClickListener(new View.OnClickListener() { // from class: com.treadly.Treadly.UI.Util.NormalWebViewActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (NormalWebViewActivity.this.mWebview.canGoBack()) {
                    if (NormalWebViewActivity.this.count(NormalWebViewActivity.this.mWebview.getUrl(), UriUtil.HTTP_SCHEME) > 1) {
                        NormalWebViewActivity.this.mWebview.goBackOrForward(-2);
                        return;
                    } else {
                        NormalWebViewActivity.this.mWebview.goBack();
                        return;
                    }
                }
                NormalWebViewActivity.this.finish();
            }
        });
        this.titleTv = (TextView) findViewById(R.id.titleTv);
        this.titleTv.setText(this.title);
        this.mWebview = (WebView) findViewById(R.id.normal_activity_webv);
        initWebView();
        this.mWebview.loadUrl(this.url);
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void initWebView() {
        this.mWebview.requestFocus();
        WebSettings settings = this.mWebview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        if (Build.VERSION.SDK_INT > 19) {
            settings.setMixedContentMode(0);
        }
        settings.setCacheMode(2);
        this.mWebview.setWebViewClient(new WebViewClient());
        this.mWebview.setWebChromeClient(new WebChromeClient());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int count(String str, String str2) {
        int i = 0;
        int i2 = 0;
        while (true) {
            int indexOf = str.indexOf(str2, i);
            if (indexOf < 0) {
                return i2;
            }
            i = indexOf + str2.length();
            i2++;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void customForward(String str) {
        String[] split;
        HashMap hashMap = new HashMap();
        if (str.contains("?")) {
            String substring = str.substring(str.indexOf("?") + 1);
            if (substring.contains("&")) {
                for (String str2 : substring.split("&")) {
                    hashMap.put(str2.substring(0, str2.indexOf("=")), str2.substring(str2.indexOf("=") + 1));
                }
            }
        }
        if (hashMap.isEmpty()) {
            return;
        }
        if (hashMap.containsKey("Login")) {
            String str3 = (String) hashMap.get("Login");
        }
        if (hashMap.containsKey("WebView")) {
            String str4 = (String) hashMap.get("WebView");
        }
        if (hashMap.containsKey("toUrl")) {
            String str5 = (String) hashMap.get("toUrl");
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (this.mWebview.canGoBack()) {
                if (count(this.mWebview.getUrl(), UriUtil.HTTP_SCHEME) > 1) {
                    this.mWebview.goBackOrForward(-2);
                } else {
                    this.mWebview.goBack();
                }
            } else {
                finish();
            }
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* loaded from: classes2.dex */
    class MyWebViewClient extends WebViewClient {
        MyWebViewClient() {
        }

        @Override // android.webkit.WebViewClient
        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            if (str.startsWith("mingtuu")) {
                NormalWebViewActivity.this.customForward(str);
                return false;
            }
            webView.loadUrl(str);
            return true;
        }

        @Override // android.webkit.WebViewClient
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            NormalWebViewActivity.this.showLoadingDialog(true);
        }

        @Override // android.webkit.WebViewClient
        public void onPageFinished(WebView webView, String str) {
            NormalWebViewActivity.this.closeLoadingDialog();
            super.onPageFinished(webView, str);
        }

        @Override // android.webkit.WebViewClient
        public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
            AlertDialog.Builder builder = new AlertDialog.Builder(NormalWebViewActivity.this);
            builder.setMessage("SSL certificate error.");
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.Util.NormalWebViewActivity.MyWebViewClient.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    sslErrorHandler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() { // from class: com.treadly.Treadly.UI.Util.NormalWebViewActivity.MyWebViewClient.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    sslErrorHandler.cancel();
                }
            });
            builder.create().show();
        }
    }

    /* loaded from: classes2.dex */
    class BaseWebChromeClient extends WebChromeClient {
        BaseWebChromeClient() {
        }

        @Override // android.webkit.WebChromeClient
        public void onProgressChanged(WebView webView, int i) {
            super.onProgressChanged(webView, i);
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        closeLoadingDialog();
        this.mWebview.destroy();
    }
}
