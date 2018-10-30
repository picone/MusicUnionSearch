package chien.com.musicunionsearch.http.request;

import com.google.gson.Gson;

import chien.com.musicunionsearch.util.MD5;
import okhttp3.Request;

public class NeteaseCloud {

    public static Request searchSong(String query, int page, int pageSize) {
        SearchSong searchSong = new SearchSong(query, 1);
        searchSong.offset = page;
        searchSong.limit = pageSize;
        String params = encrypt("/api/cloudsearch/pc", searchSong);
    }

    private static String encrypt(String path, Object obj) {
        String params = new Gson().toJson(obj);
        String sign = MD5.hash("nobody" + path + "use" + params + "md5forencrypt").toUpperCase();
        String src = path + "-36cd479b6b5-" + params + "-36cd479b6b5-" + sign;
        
    }

    private static class SearchSong {
        public String s;
        public int offset = 0;
        public int limit = 100;
        public String hlposttag = "</span>";
        public String hlpretag = "<span class=\"s-fc2\">";
        public String total = "true";
        public int type = 1;

        SearchSong(String query, int type) {
            this.s = query;
            this.type = type;
        }
    }
}
