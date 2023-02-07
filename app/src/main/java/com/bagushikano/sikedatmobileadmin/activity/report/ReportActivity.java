package com.bagushikano.sikedatmobileadmin.activity.report;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import android.print.PrintManager;
import android.view.View;

import com.bagushikano.sikedatmobileadmin.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import im.delight.android.webview.AdvancedWebView;

public class ReportActivity extends AppCompatActivity implements AdvancedWebView.Listener {

    private Toolbar homeToolbar;
    SharedPreferences loginPreferences;
    private AdvancedWebView advancedWebView;
    ExtendedFloatingActionButton printTestFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        homeToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(homeToolbar);
        homeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        advancedWebView = findViewById(R.id.invoice_transaksi_webview);
        loginPreferences = getSharedPreferences("login_preferences", Context.MODE_PRIVATE);
        advancedWebView.setListener(this, this);
        advancedWebView.setMixedContentAllowed(true);
        advancedWebView.setDesktopMode(false);
        advancedWebView.addHttpHeader("Authorization", "Bearer " +  loginPreferences.getString("token", "empty"));
        advancedWebView.loadUrl(getResources().getString(R.string.api_endpoint) + getIntent().getStringExtra("URL"));
    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) {

    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        Snackbar.make(getWindow().getDecorView().findViewById(android.R.id.content),
                "Gagal dalam membuat laporan.",
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) {
        // some file is available for download
        // either handle the download yourself or use the code below

        if (AdvancedWebView.handleDownload(this, url, suggestedFilename)) {
            // download successfully handled
        }
        else {

        }
    }

    @Override
    public void onExternalPageRequest(String url) {

    }
}