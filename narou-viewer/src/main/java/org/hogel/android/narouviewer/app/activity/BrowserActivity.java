package org.hogel.android.narouviewer.app.activity;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import org.hogel.android.narouviewer.app.R;
import org.hogel.android.narouviewer.app.view.NarouWebView;
import org.hogel.android.narouviewer.app.webview.NarouWebViewClient;

public class BrowserActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browser_view);

        final NarouWebView mainWebView = (NarouWebView) findViewById(R.id.mainWebView);

        Uri data = getIntent().getData();
        mainWebView.loadUrl(data.toString());
        mainWebView.setWebViewClient(new NarouWebViewClient(getApplicationContext()));
    }
}
