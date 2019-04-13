package chien.com.musicunionsearch.http;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chien.com.musicunionsearch.TestData;
import chien.com.musicunionsearch.http.request.QQMusic;
import chien.com.musicunionsearch.http.response.QQMusicPlaySongResponse;
import chien.com.musicunionsearch.http.response.QQMusicSearchSongResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QQMusicTest {

    private final static int testSongLen = 3;
    private List<QQMusicSearchSongResponse.Data.Song.SongItem> songItemList = new ArrayList<>();

    @Test
    public void testSearchSong() throws IOException {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        for (String songName : TestData.SongName) {
            Request req = QQMusic.searchSong(songName, 1, testSongLen);
            Response resp = httpClient.newCall(req).execute();
            Object data = ResponseHandler.parseResponse(resp, QQMusicSearchSongResponse.class);
            Assert.assertNotNull(data);
            QQMusicSearchSongResponse songs = (QQMusicSearchSongResponse)data;
            Assert.assertNotEquals(songs.data.song.list.size(), 0);
            songItemList.addAll(songs.data.song.list);
        }
    }

    @After
    public void testPlaySong() throws IOException {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        for (QQMusicSearchSongResponse.Data.Song.SongItem songItem : songItemList) {
            Request req = QQMusic.playSong(songItem.mid);
            Response resp = httpClient.newCall(req).execute();
            Object data = ResponseHandler.parseResponse(resp, QQMusicPlaySongResponse.class);
            Assert.assertNotNull(data);
            QQMusicPlaySongResponse songs = (QQMusicPlaySongResponse)data;
            Assert.assertEquals(songs.code, 0);
            Assert.assertNotEquals(songs.req_0.data.midurlinfo, 0);
        }
    }

    @After
    public void testAlbum() {
        for (QQMusicSearchSongResponse.Data.Song.SongItem songItem : songItemList) {
            String albumUrl = QQMusic.getAlbumUrl(songItem.album.mid);
            Assert.assertNotEquals(albumUrl, "");
        }
    }
}
