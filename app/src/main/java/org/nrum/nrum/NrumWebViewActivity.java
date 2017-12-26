package org.nrum.nrum;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.nrum.util.MFunction;

@SuppressLint("SetJavaScriptEnabled")
public class NrumWebViewActivity extends AppCompatActivity {
    WebView webView;
    ProgressDialog pDialog;
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if( url.startsWith("http:") || url.startsWith("https:") ) {
                return false;
            }
            // Otherwise allow the OS to handle things like tel, mailto, etc.
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity( intent );
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

        @SuppressWarnings("deprecation")
        @Override
        public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
            // hide webView content and show custom msg
            webView.setVisibility(View.INVISIBLE);
            Toast.makeText(NrumWebViewActivity.this,getString(R.string.server_not_responding), Toast.LENGTH_SHORT).show();
        }

        @TargetApi(android.os.Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
            // Redirect to deprecated method, so you can use it in all SDK versions
            onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nrum_web_view);
        webView =(WebView)findViewById(R.id.NrumWebView);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle(getString(R.string.app_name));
        pDialog.setMessage(getString(R.string.loading));
        if (MFunction.isInternetAvailable(getApplicationContext())){
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.getSettings().setBuiltInZoomControls(true);
            webView.setWebViewClient(new MyWebViewClient());
            webView.loadUrl(Constant.SERVER);
        } else {
            Toast.makeText(NrumWebViewActivity.this, getString(R.string.no_internet_connection),Toast.LENGTH_LONG).show();
        }
        // action for refreshWeb
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.refreshWeb);
        fab.setAlpha(0.7f);
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
