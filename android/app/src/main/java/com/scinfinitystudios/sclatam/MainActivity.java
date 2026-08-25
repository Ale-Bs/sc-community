package com.scinfinitystudios.sclatam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setAllowContentAccess(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return openTikTok(request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return openTikTok(Uri.parse(url));
            }

            @Override
            public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
                Uri uri = Uri.parse(url);
                if (isTikTok(uri)) {
                    view.stopLoading();
                    openTikTok(uri);
                    return;
                }
                super.onPageStarted(view, url, favicon);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("https://scinfinitystudios.github.io/sc-latam.community/");
    }

    private boolean isTikTok(Uri uri) {
        String scheme = uri.getScheme();
        String host = uri.getHost();
        return "snssdk1233".equalsIgnoreCase(scheme)
                || (host != null && ("tiktok.com".equalsIgnoreCase(host)
                || host.toLowerCase().endsWith(".tiktok.com")));
    }

    private boolean openTikTok(Uri uri) {
        if (!isTikTok(uri)) {
            return false;
        }

        // Si TikTok redirige el WebView a snssdk1233://, nunca dejamos que
        // WebView intente cargar ese esquema: lo entregamos directamente a Android.
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } catch (Exception ignored) {
            // Para esquemas internos sin handler, abrir el perfil HTTPS en el navegador.
            try {
                Uri fallback = Uri.parse("https://www.tiktok.com/@sc.latamcommunity");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, fallback);
                browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(browserIntent);
            } catch (Exception ignoredAgain) {
                // No interrumpir la APK.
            }
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
