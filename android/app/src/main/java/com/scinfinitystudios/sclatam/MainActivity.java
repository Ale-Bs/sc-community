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
                return handleExternalUrl(request.getUrl().toString());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleExternalUrl(url);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("https://scinfinitystudios.github.io/sc-latam.community/");
    }

    private boolean handleExternalUrl(String url) {
        Uri uri = Uri.parse(url);
        String scheme = uri.getScheme();
        String host = uri.getHost();

        // TikTok puede redirigir a un esquema propio (snssdk1233://).
        // WebView no puede cargarlo; se entrega al sistema Android.
        if ("snssdk1233".equalsIgnoreCase(scheme)
                || (host != null && ("tiktok.com".equalsIgnoreCase(host)
                || host.toLowerCase().endsWith(".tiktok.com")))) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            } catch (Exception ignored) {
                // Si no hay una app compatible, intentar abrir el enlace en el navegador.
                try {
                    if ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme)) {
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                } catch (Exception ignoredAgain) {
                    // No interrumpir la navegación de la APK.
                }
            }
            return true;
        }

        return false;
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
