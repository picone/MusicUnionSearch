package chien.com.musicunionsearch.http;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chien.com.musicunionsearch.TestData;
import chien.com.musicunionsearch.http.request.NeteaseCloud;
import chien.com.musicunionsearch.http.response.NeteaseCloudPlayerUrlResponse;
import chien.com.musicunionsearch.http.response.NeteaseCloudSearchSongResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NeteaseCloudTest {

    private final static int testSongLen = 5;
    private List<NeteaseCloudSearchSongResponse.Result.SongItem> songItemList = new ArrayList<>();

    @Test
    public void testSearchSong() throws IOException {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        NeteaseCloud neteaseCloud = new NeteaseCloud();
        for (String songName : TestData.SongName) {
            Request req = neteaseCloud.searchSong(songName, 0, testSongLen);
            Response resp = httpClient.newCall(req).execute();
            Object data = ResponseHandler.parseResponse(resp, NeteaseCloudSearchSongResponse.class);
            Assert.assertNotNull(data);
            NeteaseCloudSearchSongResponse songs = (NeteaseCloudSearchSongResponse)data;
            Assert.assertEquals(songs.result.songs.size(), testSongLen);
            songItemList.addAll(songs.result.songs);
        }
    }

    @After
    public void testPlaySong() throws IOException {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();
        NeteaseCloud neteaseCloud = new NeteaseCloud();
        for (NeteaseCloudSearchSongResponse.Result.SongItem songItem : songItemList) {
            Assert.assertNotEquals(songItem.id, 0);
            Request req = neteaseCloud.playerUrl(songItem.id);
            Response resp = httpClient.newCall(req).execute();
            Object data = ResponseHandler.parseResponse(resp, NeteaseCloudPlayerUrlResponse.class);
            Assert.assertNotNull(data);
            NeteaseCloudPlayerUrlResponse song = (NeteaseCloudPlayerUrlResponse)data;
            Assert.assertNotEquals(song.data.size(), 0);
            Assert.assertNotEquals(song.data.get(0).url, "");
        }
    }
}
