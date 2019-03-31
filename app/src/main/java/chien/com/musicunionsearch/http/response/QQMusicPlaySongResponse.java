package chien.com.musicunionsearch.http.response;

import java.util.List;

public class QQMusicPlaySongResponse {

    public int code;

    public Req req_0;

    public static class Req {

        public Data data;

        public static class Data {
            public List<MidUrlInfo> midurlinfo;
            public List<String> sip;

            public static class MidUrlInfo {
                public String purl;
            }
        }
    }
}
