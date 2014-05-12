package org.hogel.android.narouviewer.app.webview;

import android.net.Uri;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.regex.Pattern;

@Data
@AllArgsConstructor(suppressConstructorProperties=true)
public class NarouUrl {
    private String url;
    private Uri uri;
    private boolean narouUrl;
    private boolean ncodeUrl;

    private static final Pattern NAROU_DOMAIN_PATTERN = Pattern.compile("^(?:.+?\\.)?syosetu.com$");
    private static final Pattern NCODE_VIEW_PATH_PATTERN = Pattern.compile("^/\\w+/\\d+/?$");

    public static NarouUrl parse(String url) {
        Uri uri = Uri.parse(url);
        boolean narouUrl = NAROU_DOMAIN_PATTERN.matcher(uri.getHost()).find();
        boolean ncodeUrl = NCODE_VIEW_PATH_PATTERN.matcher(uri.getPath()).find();
        return new NarouUrl(url, uri, narouUrl, ncodeUrl);
    }
}
