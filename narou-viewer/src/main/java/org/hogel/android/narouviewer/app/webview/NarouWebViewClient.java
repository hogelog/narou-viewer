package org.hogel.android.narouviewer.app.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.common.base.Optional;
import org.hogel.android.narouviewer.app.R;
import org.hogel.android.narouviewer.app.activity.BrowserActivity;
import org.hogel.android.narouviewer.app.view.NarouWebView;

import java.util.regex.Pattern;

public class NarouWebViewClient extends WebViewClient {
    private final BrowserActivity browserActivity;
    private final Context context;
    private NarouUrl currentNarouUrl;

    public NarouWebViewClient(BrowserActivity activity) {
        browserActivity = activity;
        context = browserActivity.getApplicationContext();
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        currentNarouUrl = NarouUrl.parse(url);
        if (!currentNarouUrl.isNarouUrl()) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, currentNarouUrl.getUri()));
            return true;
        }
        if (currentNarouUrl.isNcodeUrl()) {
            browserActivity.loadNcodeUrl(url);
            return true;
        }
        return false;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        browserActivity.onPageStarted(view, url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        browserActivity.onPageFinished(view, url);
    }

    public NarouUrl getCurrentNarouUrl() {
        return currentNarouUrl;
    }
}
