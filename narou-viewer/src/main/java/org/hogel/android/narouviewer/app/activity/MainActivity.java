package org.hogel.android.narouviewer.app.activity;

import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

import android.app.Activity;
import android.os.Bundle;
import org.hogel.android.narouviewer.app.R;


public class MainActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = new Intent(this, BrowserActivity.class);
        intent.setData(Uri.parse(getString(R.string.narou_url)));
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        finish();
    }
}
