package org.hogel.android.narouviewer.app.webview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.hogel.android.narouviewer.app.R;
import roboguice.inject.InjectResource;

import javax.inject.Inject;

public class NarouWebViewClient extends WebViewClient {
    @Inject Context context;

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri uri = Uri.parse(url);
        if (uri.getHost().endsWith(context.getString(R.string.narou_domain))) {
            return false;
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        return true;
    }
}
