package org.hogel.android.narouviewer.app.webview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import org.hogel.android.narouviewer.app.R;
import org.hogel.android.narouviewer.app.activity.BrowserActivity;
import org.hogel.android.narouviewer.app.view.NarouWebView;

import java.util.regex.Pattern;

public class NarouWebViewClient extends WebViewClient {
    private final BrowserActivity browserActivity;
    private final Context context;

    public NarouWebViewClient(BrowserActivity activity) {
        browserActivity = activity;
        context = browserActivity.getApplicationContext();
    }

    private static final Pattern NCODE_VIEW_PATH_PATTERN = Pattern.compile("^/\\w+/\\d+/$");

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Uri uri = Uri.parse(url);
        if (uri.getHost().endsWith(context.getString(R.string.narou_domain))) {
            if (uri.getHost().equals("ncode.syosetu.com")) {
                if (NCODE_VIEW_PATH_PATTERN.matcher(uri.getPath()).find()) {
                    browserActivity.loadNcodeUrl(url);
                    return true;
                }
            }
            return false;
        }
        context.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        return true;
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
}
