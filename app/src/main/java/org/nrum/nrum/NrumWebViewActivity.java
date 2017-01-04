package org.nrum.nrum;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("SetJavaScriptEnabled")
public class NrumWebViewActivity extends AppCompatActivity {
    String msg = "LogInfo : ";
    WebView webview;
    ProgressDialog pDialog;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            pDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrum_web_view);
        webview =(WebView)findViewById(R.id.NrumWebView);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");

        if (CheckNetwork.isInternetAvailable(NrumWebViewActivity.this)){
            webview.getSettings().setJavaScriptEnabled(true);
            webview.getSettings().setDomStorageEnabled(true);
            webview.getSettings().setLoadWithOverviewMode(true);
            webview.getSettings().setUseWideViewPort(true);
            webview.getSettings().setBuiltInZoomControls(true);
            webview.setWebViewClient(new MyWebViewClient());
            webview.loadUrl("https://developer.android.com/guide/index.html");
        } else {
            //no connection
            Toast toast = Toast.makeText(NrumWebViewActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
        }
        // action for refreshWeb
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.refreshWeb);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // refresh Activity
                finish();
                startActivity(getIntent());
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
