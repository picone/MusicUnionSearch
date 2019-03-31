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

    public static Request playSong(String mid) {
        String guid = QQMusic.getGuid();
        String data = "{\"req_0\":{\"module\":\"vkey.GetVkeyServer\",\"method\":\"CgiGetVkey\",\"param\":{\"guid\":\"{{guid}}\",\"songmid\":[\"{{songmid}}\"],\"songtype\":[0],\"uin\":\"0\",\"loginflag\":1,\"platform\":\"20\"}},\"comm\":{\"uin\":0,\"format\":\"json\",\"ct\":24,\"cv\":0}}"
                .replace("{{songmid}}", mid)
                .replace("{{guid}}", guid);
        String url = new HttpUrl.Builder()
                .scheme("https")
                .host("u.y.qq.com")
                .encodedPath("/cgi-bin/musicu.fcg")
                .addQueryParameter("loginUin", "0")
                .addQueryParameter("hostUin", "0")
                .addQueryParameter("format", "json")
                .addQueryParameter("inCharset", "utf-8")
                .addQueryParameter("outCharset", "utf-8")
                .addQueryParameter("notice", "0")
                .addQueryParameter("platform", "yqq.json")
                .addQueryParameter("needNewCode", "0")
                .addQueryParameter("data", data)
                .toString();
        return getDefaultBuilder().url(url).build();
    }

    public static String getPlayerUrl(String filename, String vkey, String guid) {
        return String.format(Locale.getDefault(), "http://isure.stream.qqmusic.qq.com/%s?vkey=%s&" +
                "guid=%s&uin=0&fromtag=8", filename, vkey, guid);
    }

    public static String getAlbumUrl(String mid) {
        return "https://y.gtimg.cn/music/photo_new/T002R300x300M000" + mid + ".jpg";
    }

    public static String getGuid() {
        long guid = Math.abs(new Random().nextLong()) % 9999999999L;
        return String.format(Locale.getDefault(), "%010d", guid);
    }

    private static Request.Builder getDefaultBuilder() {
        return new Request.Builder()
                .addHeader("Referer", "https://y.qq.com")
                .get();
    }
}
