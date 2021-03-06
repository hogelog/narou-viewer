package org.hogel.android.narouviewer.app.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import org.hogel.android.narouviewer.app.R;

public class NarouWebView extends WebView {

    private int backKeyCount = 0;

    public NarouWebView(Context context) {
        this(context, null, android.R.attr.webViewStyle);
    }

    public NarouWebView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.webViewStyle);
    }

    public NarouWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        final WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (canGoBack()) {
                goBack();
                return true;
            } else if (backKeyCount == 0) {
                ++backKeyCount;
                Toast.makeText(getContext(), R.string.message_back_exit_confirm, Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        backKeyCount = 0;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        backKeyCount = 0;
        return super.onTouchEvent(event);
    }

}
