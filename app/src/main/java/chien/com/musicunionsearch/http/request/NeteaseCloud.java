package chien.com.musicunionsearch.http.request;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

import java.util.Locale;
import java.util.Random;

import chien.com.musicunionsearch.util.AES;
import chien.com.musicunionsearch.util.MD5;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

public class NeteaseCloud {

    private static final String EAPI_AES_KEY = "e82ckenh8dichen8";

    private AES aes;

    public NeteaseCloud() {
        aes = new AES(EAPI_AES_KEY);
    }

    public Request searchSong(String query, int page, int pageSize) {
        SearchSong searchSong = new SearchSong(query, 1);
        searchSong.offset = page;
        searchSong.limit = pageSize;
        String params = encrypt("/api/cloudsearch/pc", searchSong).toUpperCase(Locale.CHINESE);
        return getBaseRequest("https://music.163.com/eapi/cloudsearch/pc", params).build();
    }

    public Request playerUrl(int id) {
        PlayerUrl playerUrl = new PlayerUrl(new int[]{id});
        String params = encrypt("/api/song/enhance/player/url", playerUrl);
        return getBaseRequest("https://music.163.com/eapi/song/enhance/player/url", params).build();
    }

    private Request.Builder getBaseRequest(String url, String params) {
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
        RequestBody requestBody = RequestBody.create(mediaType, "params=" + params);
        return new Request.Builder()
                .addHeader("Accept", "*/*")
                .addHeader("Origin", "orpheus://orpheus")
                .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/605.1.15 (KHTML, like Gecko)")
                .addHeader("Accept-language", "zh-cn")
                .url(url)
                .post(requestBody);
    }

    private String encrypt(String path, Object obj) {
        String params = new Gson().toJson(obj);
        String sign = MD5.hash("nobody" + path + "use" + params + "md5forencrypt").toUpperCase(Locale.CHINESE);
        String src = path + "-36cd479b6b5-" + params + "-36cd479b6b5-" + sign;
        return aes.encrypt(src);
    }

    private static class SearchSong extends BaseNeteaseRequest {
        String s;
        int offset = 0;
        int limit = 100;
        String hlposttag = "</span>";
        String hlpretag = "<span class=\"s-fc2\">";
        String total = "true";
        int type = 1;

        SearchSong(String query, int type) {
            this.s = query;
            this.type = type;
        }
    }

    private static class PlayerUrl extends BaseNeteaseRequest {
        String ids;
        int br = 320000;

        PlayerUrl(@NonNull int[] ids) {
            if (ids.length == 0) {
                this.ids = "[]";
            } else {
                StringBuilder idsBuilder = new StringBuilder("[");
                for (int id: ids) {
                    idsBuilder.append(id).append(',');
                }
                idsBuilder.setCharAt(idsBuilder.length() - 1, ']');
                this.ids = idsBuilder.toString();
            }
        }
    }

    private static class BaseNeteaseRequest {
        int verifyId = 1;
        String os = "OSX";
        Header header = new Header();

        BaseNeteaseRequest() {
            header.requestId = String.format(Locale.getDefault(), "%8d", new Random().nextInt(99999999));
        }

        private static class Header {
            String os = "osx";
            String appver = "1.5.9";
            String requestId;
            String clientSign;
        }
    }
}
