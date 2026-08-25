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
                return openExternalIfNeeded(request.getUrl());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return openExternalIfNeeded(Uri.parse(url));
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("https://scinfinitystudios.github.io/sc-latam.community/");
    }

    private boolean openExternalIfNeeded(Uri uri) {
        String scheme = uri.getScheme();
        String host = uri.getHost();

        boolean isTikTok = "snssdk1233".equalsIgnoreCase(scheme)
                || (host != null && ("tiktok.com".equalsIgnoreCase(host)
                || host.toLowerCase().endsWith(".tiktok.com")));

        if (!isTikTok) {
            return false;
        }

        // Primero entrega el enlace al sistema Android. Si TikTok está instalado,
        // Android abrirá automáticamente la aplicación asociada.
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return true;
        } catch (Exception ignored) {
            // Si el esquema propio no tiene una aplicación asociada, convertir
            // el enlace de TikTok a HTTPS para abrirlo en el navegador.
            try {
                if ("snssdk1233".equalsIgnoreCase(scheme)) {
                    Uri fallback = Uri.parse("https://www.tiktok.com/@sc.latamcommunity");
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, fallback);
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(browserIntent);
                    return true;
                }
            } catch (Exception ignoredAgain) {
                // No interrumpir la aplicación si Android no encuentra un handler.
            }
        }

        return true;
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
