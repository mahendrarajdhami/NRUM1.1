package org.nrum.nrum;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
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
            webview.setWebChromeClient(new WebChromeClient(){
                public void onProgressChanged(WebView view, int progress) {
                    pDialog.setProgress(progress);
                    Log.d(msg, "progress=" + progress);
                    if (progress == 100) {
                        pDialog.show();

                    } else {
                        hidePDialog();

                    }
                }
            });
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

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }
}
