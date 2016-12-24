package org.nrum.nrum;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class NrumWebViewActivity extends AppCompatActivity {
    String msg = "LogInfo : ";
    WebView webview;
    ProgressBar mProgress;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrum_web_view);
        webview =(WebView)findViewById(R.id.NrumWebView);
        mProgress = (ProgressBar)findViewById(R.id.progressBar);

        if (CheckNetwork.isInternetAvailable(NrumWebViewActivity.this)){
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setBuiltInZoomControls(true);
            webview.setWebViewClient(new MyWebViewClient());
            webview.setWebChromeClient(new WebChromeClient(){
                public void onProgressChanged(WebView view, int progress) {
                    mProgress.setProgress(progress);
                    Log.d(msg, "progress=" + progress);
                    if (progress == 100) {
                        Log.d(msg,"Showing Progressbar");
                        mProgress.setVisibility(View.GONE);

                    } else {
                        mProgress.setVisibility(View.VISIBLE);

                    }
                }
            });
            webview.loadUrl("https://developer.android.com/guide/index.html");
        } else {
            //no connection
            Toast toast = Toast.makeText(NrumWebViewActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
