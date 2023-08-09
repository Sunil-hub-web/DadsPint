package co.in.dadspint;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.android.gms.common.internal.Constants;

public class WebViewActivity extends AppCompatActivity {

    WebView webview;
    Dialog dialog;
    String str_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        // Find the WebView by its unique ID
        WebView webView = findViewById(R.id.webview);

        str_url = getIntent().getStringExtra("weburl");

        // loading https://www.geeksforgeeks.org url in the WebView.
        webView.loadUrl(str_url);

        // this will enable the javascript.
        webView.getSettings().setJavaScriptEnabled(true);

        // WebViewClient allows you to handle
        // onPageFinished and override Url loading.
        webView.setWebViewClient(new WebViewClient());

    }

    @Override
    public void onBackPressed() {

        if (webview.canGoBack()){
            webview.goBack();
        }else{
            super.onBackPressed();
        }

    }
}