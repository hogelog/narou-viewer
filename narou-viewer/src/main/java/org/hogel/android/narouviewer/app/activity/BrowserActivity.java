package org.hogel.android.narouviewer.app.activity;

import android.net.Uri;
import android.os.Bundle;
import org.hogel.android.narouviewer.app.R;
import org.hogel.android.narouviewer.app.view.NarouWebView;
import org.hogel.android.narouviewer.app.webview.NarouWebViewClient;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;

public class BrowserActivity extends RoboActivity {
    @InjectView(R.id.mainWebView) NarouWebView narouWebView;

    @Inject NarouWebViewClient narouWebViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.browser_view);

        Uri data = getIntent().getData();
        narouWebView.loadUrl(data.toString());
        narouWebView.setWebViewClient(narouWebViewClient);
    }
}
