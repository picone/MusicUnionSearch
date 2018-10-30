package chien.com.musicunionsearch.http.response;

import java.util.List;

public class KugouSearchSongResponse {
    public Data data;

    public static class Data {

        public List<Info> info;

        public static class Info {
            public String hash;
            public String singername;
            public String songname;
            public String album_id;
        }
    }
}
