package org.hogel.android.narouviewer.app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.common.base.Optional;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import org.apache.http.Header;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.hogel.android.narouviewer.app.R;
import org.hogel.android.narouviewer.app.util.HtmlCacheStore;
import org.hogel.android.narouviewer.app.view.NarouWebView;
import org.hogel.android.narouviewer.app.webview.NarouUrl;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

import javax.inject.Inject;
import java.nio.charset.Charset;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BrowserActivity extends RoboActivity {
    class NarouViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            currentNarouUrl = NarouUrl.parse(url);
            if (!currentNarouUrl.isNarouUrl()) {
                startActivity(new Intent(Intent.ACTION_VIEW, currentNarouUrl.getUri()));
                return true;
            }
            if (currentNarouUrl.isNcodeUrl()) {
                loadNcodeUrl(url);
                return true;
            }
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            setProgressBarIndeterminateVisibility(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            setProgressBarIndeterminateVisibility(false);

            syncCookie();
        }
    }

    @InjectView(R.id.mainWebView)
    NarouWebView narouWebView;

    @Inject
    private HtmlCacheStore htmlCacheStore;

    private AsyncHttpClient asyncHttpClient;
    private PersistentCookieStore persistentCookieStore;

    private NarouUrl currentNarouUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(R.string.app_action_bar_title);

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.browser_view);

        narouWebView.setWebViewClient(new NarouViewClient());

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setUserAgent(narouWebView.getSettings().getUserAgentString());
        persistentCookieStore = new PersistentCookieStore(this);
        asyncHttpClient.setCookieStore(persistentCookieStore);

        Uri data = getIntent().getData();
        narouWebView.loadUrl(data.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.browser_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_top:
                goTop();
                return true;
            case R.id.action_user_home:
                goUserHome();
                return true;
            case R.id.action_ranking:
                goRanking();
                return true;
            case R.id.action_reload:
                reload();
                return true;
            case R.id.action_finish:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void goTop() {
        narouWebView.loadUrl(getString(R.string.url_narou_top));
    }

    private void goUserHome() {
        narouWebView.loadUrl(getString(R.string.url_narou_user_home));
    }

    private void goRanking() {
        narouWebView.loadUrl(getString(R.string.url_narou_ranking));
    }

    private void reload() {
        if (currentNarouUrl.isNcodeUrl()) {
            loadNcodeUrl(currentNarouUrl.getUrl(), true);
        } else {
            narouWebView.reload();
        }
    }

    public void loadNcodeUrl(String url) {
        loadNcodeUrl(url, false);
    }

    public void loadNcodeUrl(final String url, boolean isReload) {
        setProgressBarIndeterminateVisibility(true);

        if (!isReload) {
            Optional<String> cache = htmlCacheStore.findCache(url);
            if (cache.isPresent()) {
                narouWebView.loadDataWithBaseURL(url, cache.get(), "text/html", "utf-8", url);
                return;
            }
        }

        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String data = new String(responseBody, Charset.forName("UTF-8"));
                narouWebView.loadDataWithBaseURL(url, data, "text/html", "utf-8", url);
                htmlCacheStore.addCache(url, data);
                prefetchNcodeHtml(data);
            }
        });
    }

    private static final Pattern PREFETCH_ANCHOR_PATTERN = Pattern.compile("<a rel=\"(?:prev|next)\" href=\"([^\"]+)\"");
    private void prefetchNcodeHtml(String html) {
        Matcher matcher = PREFETCH_ANCHOR_PATTERN.matcher(html);
        while (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            String href = matchResult.group(1);
            final String url = "http://ncode.syosetu.com" + href;
            if (htmlCacheStore.hasCache(url)) {
                continue;
            }
            asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String data = new String(responseBody, Charset.forName("UTF-8"));
                    htmlCacheStore.addCache(url, data);
                }
            });
        }
    }

    private void syncCookie() {
        String webviewCookiesValues = CookieManager.getInstance().getCookie(getString(R.string.syostu_cookie_url));
        for (String webviewCookieValue : webviewCookiesValues.split("; ")) {
            String[] nameAndVal = webviewCookieValue.split("=");
            BasicClientCookie cookie = new BasicClientCookie(nameAndVal[0], nameAndVal[1]);
            cookie.setDomain(getString(R.string.syostu_cookie_url));
            persistentCookieStore.addCookie(cookie);
        }
        asyncHttpClient.setCookieStore(persistentCookieStore);
    }
}
