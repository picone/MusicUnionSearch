package chien.com.musicunionsearch.http.response;

import java.util.List;

public class QQMusicPlaySongResponse {

    public Data data;

    public static class Data {

        public List<Item> items;

        public static class Item {
            public String filename;
            public String vkey;
        }
    }
}
