package org.hogel.android.narouviewer.app.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.hogel.android.narouviewer.app.R;
import org.hogel.android.narouviewer.app.activity.BrowserActivity;

public class NarouWebViewClient extends WebViewClient {
    private final BrowserActivity browserActivity;
    private final Context context;

    public NarouWebViewClient(BrowserActivity browserActivity) {
        this.browserActivity = browserActivity;
        this.context = browserActivity.getApplicationContext();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri uri = Uri.parse(url);
        if (uri.getHost().endsWith(context.getString(R.string.narou_domain))) {
            return false;
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        browserActivity.onPageStarted(view, url);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        browserActivity.onPageFinished(view, url);
        super.onPageFinished(view, url);
    }
}
