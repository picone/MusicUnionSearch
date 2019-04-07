package chien.com.musicunionsearch.http.response;

import java.util.List;

public class QQMusicSearchSongResponse {

    public Data data;

    public static class Data {
        public Song song;

        public static class Song {
            public List<SongItem> list;

            public static class SongItem {
                public String name;
                public String mid;
                public List<Singer> singer;
                public Album album;

                public static class Album {
                    public String mid;
                    public String name;
                }

                public static class Singer {
                    public String name;
                }
            }
        }
    }
}
