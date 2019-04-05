package chien.com.musicunionsearch.http.request;

import chien.com.musicunionsearch.util.MD5;
import okhttp3.HttpUrl;
import okhttp3.Request;

public class Kugou {
    public static Request searchSong(String query, int page, int pageSize) {
        String url = new HttpUrl.Builder()
                .scheme("http")
                .host("mobilecdnbj.kugou.com")
                .encodedPath("/api/v3/search/song")
                .addQueryParameter("showtype", "14")
                .addQueryParameter("highlight", "")
                .addQueryParameter("pagesize", String.valueOf(pageSize))
                .addQueryParameter("tag_aggr", "1")
                .addQueryParameter("tagtype", "全部")
                .addQueryParameter("plat", "0")
                .addQueryParameter("sver", "5")
                .addQueryParameter("keyword", query)
                .addQueryParameter("correct", "1")
                .addQueryParameter("api_ver", "1")
                .addQueryParameter("version", "9068")
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("area_code", "1")
                .addQueryParameter("tag", "1")
                .toString();
        return getDefaultBuilder().url(url).build();
    }

    public static Request playSong(String hash) {
        String url = new HttpUrl.Builder()
                .scheme("http")
                .host("trackercdnbj.kugou.com")
                .encodedPath("/i/v2/")
                .addQueryParameter("behavior", "play")
                .addQueryParameter("cmd", "23")
                .addQueryParameter("hash", hash)
                .addQueryParameter("pid", "2")
                .addQueryParameter("appid", "1005")
                .addQueryParameter("key", getKeyByHash(hash))
                .toString();
        return getDefaultBuilder().url(url).build();
    }

    private static Request.Builder getDefaultBuilder() {
        return new Request.Builder()
                .addHeader("User-Agent", "Android810-AndroidPhone-9068-47-0-NetMusic-wifi")
                .get();
    }

    private static String getKeyByHash(String hash) {
        return MD5.hash(hash + "kgcloudv2");
    }
}
