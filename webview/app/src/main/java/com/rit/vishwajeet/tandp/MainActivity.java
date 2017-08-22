package com.rit.vishwajeet.tandp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    WebView web;
    ProgressBar bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);
        web = (WebView) findViewById(R.id.webview1);
        bar= (ProgressBar) findViewById(R.id.progressBar);
        if(!isConnected(MainActivity.this)||isOnline()==false){
            web.loadUrl("file:///android_asset/internet.html");
            buildDialog(MainActivity.this).show();}
        else {

            web.getSettings().setJavaScriptEnabled(true);
            WebSettings webSettings = web.getSettings();
            //improve webview settings
            web.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
           // web.getSettings().setCacheMode(webSettings.LOAD_CACHE_ELSE_NETWORK);
            web.getSettings().setAppCacheEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setSavePassword(true);
            webSettings.setSaveFormData(true);
            webSettings.setEnableSmoothTransition(true);
            CookieSyncManager.createInstance(this);
            CookieSyncManager.getInstance().startSync();
            web.loadUrl("https://www.flipkart.com/");
            web.setWebViewClient(new MyWebViewClient());
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        CookieSyncManager.getInstance().stopSync();
    }
    @Override
    protected void onPause() {
        super.onPause();
        CookieSyncManager.getInstance().sync();
    }


    private class MyWebViewClient extends WebViewClient {



        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
           // bar= findViewById(R.id.progressBar);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            bar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(!isConnected(MainActivity.this)||isOnline()==false){
                web.loadUrl("file:///android_asset/internet.html");
                buildDialog(MainActivity.this).show();}

            else {

                view.loadUrl(url);
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (!web.canGoBack()) {
                        finish();
                    } else {

                        web.goBack();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
        else return false;
        } else
        return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("No Mobile Data or wifi to access this. Press retry");

        builder.setPositiveButton("retry", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(!isConnected(MainActivity.this)||isOnline()==false){
                   // dialog.dismiss();
                 //   onCreate(new Bundle());
                buildDialog(MainActivity.this).show();
                }
                    else
                    web.loadUrl("http://www.webtesting.somee.com/");
            }
        });

        return builder;
    }

    public boolean isOnline() {

        // Process p1=Runtime.getRuntime().exec("ping -c 1 www.google.com");;
        java.lang.Process p1 = null;
        try {
            p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal == 0);
            if (reachable) {


                return true;
            } else {

                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            p1.destroy();
        }

        return false;


    }
}
