package chien.com.musicunionsearch.http.request;

import java.util.Locale;
import java.util.Random;

import okhttp3.HttpUrl;
import okhttp3.Request;

public class QQMusic {

    public static Request searchSong(String query, int page, int pageSize) {
        String url = new HttpUrl.Builder()
                .scheme("http")
                .host("soso.music.qq.com")
                .encodedPath("/fcgi-bin/client_search_cp")
                .addQueryParameter("format", "json")
                .addQueryParameter("t", "0")
                .addQueryParameter("inCharset", "utf-8")
                .addQueryParameter("outCharset", "utf-8")
                .addQueryParameter("qqmusic_ver", "50500")
                .addQueryParameter("ct", "6")
                .addQueryParameter("catZhida", "1")
                .addQueryParameter("p", String.valueOf(page))
                .addQueryParameter("n", String.valueOf(pageSize))
                .addQueryParameter("w", query)
                .addQueryParameter("flag_qc", "1")
                .addQueryParameter("remoteplace", "txt.mac.search")
                .addQueryParameter("new_json", "1")
                .addQueryParameter("lossless", "0")
                .addQueryParameter("aggr", "0")
                .addQueryParameter("cr", "1")
                .addQueryParameter("sem", "0")
                .toString();
        return getDefaultBuilder().url(url).build();
    }

    public static Request playSong(String mid, String guid) {
        String filename = "C400" + mid + ".m4a";
        String url = new HttpUrl.Builder()
                .scheme("https")
                .host("c.y.qq.com")
                .encodedPath("/base/fcgi-bin/fcg_music_express_mobile3.fcg")
                .addQueryParameter("format", "json")
                .addQueryParameter("inCharset", "utf-8")
                .addQueryParameter("outCharset", "utf-8")
                .addQueryParameter("platform", "yqq")
                .addQueryParameter("cid", "205361747")
                .addQueryParameter("uin", "0")
                .addQueryParameter("needNewCode", "0")
                .addQueryParameter("songmid", mid)
                .addQueryParameter("filename", filename)
                .addQueryParameter("guid", guid)
                .toString();
        return getDefaultBuilder().url(url).build();
    }

    public static String getPlayerUrl(String filename, String vkey, String guid) {
        return String.format(Locale.getDefault(), "http://isure.stream.qqmusic.qq.com/%s?vkey=%s&" +
                "guid=%s&uin=0&fromtag=151", filename, vkey, guid);
    }

    public static String getAlbumUrl(String mid) {
        return "https://y.gtimg.cn/music/photo_new/T002R300x300M000" + mid + ".jpg";
    }

    public static String getGuid() {
        long guid = new Random().nextLong() % 9999999999L;
        return String.format(Locale.getDefault(), "%10d", guid);
    }

    private static Request.Builder getDefaultBuilder() {
        return new Request.Builder()
                .addHeader("Referer", "http://y.qq.com")
                .get();
    }
}
