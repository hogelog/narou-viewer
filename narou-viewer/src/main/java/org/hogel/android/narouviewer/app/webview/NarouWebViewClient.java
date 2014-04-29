package org.hogel.android.narouviewer.app.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.hogel.android.narouviewer.app.R;

public class NarouWebViewClient extends WebViewClient {
    private final Context context;

    public NarouWebViewClient(Context context) {
        this.context = context;
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
    public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
        super.onUnhandledKeyEvent(view, event);
    }
}
