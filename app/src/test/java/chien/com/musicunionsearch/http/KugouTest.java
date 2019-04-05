package chien.com.musicunionsearch.http;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chien.com.musicunionsearch.TestData;
import chien.com.musicunionsearch.http.request.Kugou;
import chien.com.musicunionsearch.http.response.KugouPlaySongResponse;
import chien.com.musicunionsearch.http.response.KugouSearchSongResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class KugouTest {

    private final static int testSongLen = 5;
    private List<KugouSearchSongResponse.Data.Info> songItemList = new ArrayList<>();

    @Test
    public void testSearchSong() throws IOException {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        for (String songName : TestData.SongName) {
            Request req = Kugou.searchSong(songName, 0, testSongLen);
            Response resp = httpClient.newCall(req).execute();
            Object data = ResponseHandler.parseResponse(resp, KugouSearchSongResponse.class);
            Assert.assertNotNull(data);
            KugouSearchSongResponse songs = (KugouSearchSongResponse)data;
            Assert.assertEquals(songs.data.info.size(), testSongLen);
            songItemList.addAll(songs.data.info);
        }
    }

    @After
    public void testPlaySong() throws IOException {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        for (KugouSearchSongResponse.Data.Info songItem : songItemList) {
            Assert.assertNotEquals(songItem.hash, "");
            Request req = Kugou.playSong(songItem.hash);
            Response resp = httpClient.newCall(req).execute();
            Object data = ResponseHandler.parseResponse(resp, KugouPlaySongResponse.class);
            Assert.assertNotNull(data);
            KugouPlaySongResponse song = (KugouPlaySongResponse)data;
            Assert.assertNotEquals(song.url, "");
        }
    }
}
