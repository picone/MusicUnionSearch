package chien.com.musicunionsearch.activity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import chien.com.musicunionsearch.R;
import chien.com.musicunionsearch.adapter.KugouSongAdapter;
import chien.com.musicunionsearch.adapter.NeteaseCloudSongAdapter;
import chien.com.musicunionsearch.adapter.QQMusicSongAdapter;
import chien.com.musicunionsearch.http.handler.ImageViewCallbackHandler;
import chien.com.musicunionsearch.http.handler.SimpleCallbackHandler;
import chien.com.musicunionsearch.http.request.Kugou;
import chien.com.musicunionsearch.http.request.NeteaseCloud;
import chien.com.musicunionsearch.http.request.QQMusic;
import chien.com.musicunionsearch.http.response.KugouSearchSongResponse;
import chien.com.musicunionsearch.http.response.NeteaseCloudSearchSongResponse;
import chien.com.musicunionsearch.http.response.QQMusicSearchSongResponse;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    @ViewById(R.id.search_type)
    protected RadioGroup searchType;
    @ViewById(R.id.search_result)
    protected RecyclerView searchResult;
    @ViewById(R.id.player_cover)
    protected ImageView player_cover;//播放器封面
    @ViewById(R.id.song_name)
    protected TextView songName;
    @ViewById(R.id.singer_name)
    protected TextView singerName;
    @ViewById(R.id.player_button)
    protected ImageView playButton;
    @ViewById(R.id.player_seek_bar)
    protected AppCompatSeekBar seekBar;

    public OkHttpClient httpClient;
    private MediaPlayer mediaPlayer;
    private ProgressDialog searchProgressDialog;
    private boolean seekBarChanging = false;
    private Timer timer;
    private String downloadUrl = null;
    private String downloadFilename = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpClient = new OkHttpClient.Builder().build();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        timer = new Timer();
    }

    @AfterViews
    public void afterView() {
        searchResult.setLayoutManager(new LinearLayoutManager(this));
        searchResult.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!seekBarChanging && fromUser && seekBar.getMax() > 0) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                seekBarChanging = false;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                seekBarChanging = false;
            }
        });
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (seekBar.getMax() > 0 && !seekBarChanging) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                }
            }
        }, 0, 1000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.search);
        SearchView sv = (SearchView) searchMenuItem.getActionView();
        sv.setQueryHint(getString(R.string.activity_main_search_hint));
        sv.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 处理用户搜索请求
     * @param query 用户的搜索
     * @return 是否处理成功
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        searchProgressDialog = ProgressDialog.show(this, getString(R.string.dialog_title_wait), getString(R.string.dialog_message_search), true, false);
        switch (searchType.getCheckedRadioButtonId()) {
            case R.id.search_type_netease_cloud:
                return searchNeteaseCloud(query);
            case R.id.search_type_kugou:
                return searchKugou(query);
            case R.id.search_type_qq:
                return searchQQ(query);
        }
        return false;
    }

    protected boolean searchNeteaseCloud(String query) {
        Request req = new NeteaseCloud().searchSong(query, 1, 100);
        httpClient.newCall(req).enqueue(new SimpleCallbackHandler<NeteaseCloudSearchSongResponse>(MainActivity.this) {
            @Override
            public void onResult(Call call, final NeteaseCloudSearchSongResponse response) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchResult.setAdapter(new NeteaseCloudSongAdapter(response, MainActivity.this));
                        searchProgressDialog.cancel();
                    }
                });
            }
        });
        return true;
    }

    protected boolean searchKugou(String query) {
        Request req = Kugou.searchSong(query, 1, 100);
        httpClient.newCall(req).enqueue(new SimpleCallbackHandler<KugouSearchSongResponse>(MainActivity.this) {
            @Override
            public void onResult(Call call, final KugouSearchSongResponse response) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchResult.setAdapter(new KugouSongAdapter(response, MainActivity.this));
                        searchProgressDialog.cancel();
                    }
                });
            }
        });
        return true;
    }

    protected boolean searchQQ(String query) {
        Request req = QQMusic.searchSong(query, 1, 100);
        httpClient.newCall(req).enqueue(new SimpleCallbackHandler<QQMusicSearchSongResponse>(MainActivity.this) {
            @Override
            public void onResult(Call call, final QQMusicSearchSongResponse response) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchResult.setAdapter(new QQMusicSongAdapter(response, MainActivity.this));
                        searchProgressDialog.cancel();
                    }
                });
            }
        });
        return true;
    }

    /**
     * 播放音乐
     * @param url 音乐地址
     * @param name 歌曲名字
     * @param singer 歌手名字
     * @param album 专辑封面
     */
    public void playMusic(String url, String name, String singer, String album, String filename) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    seekBar.setMax(mp.getDuration());
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.stop);
                }
            });
            downloadUrl = url;
            downloadFilename = filename;
        } catch (IOException|NullPointerException e) {
            Toast.makeText(this, R.string.toast_player_url_error, Toast.LENGTH_SHORT).show();
        }
        songName.setText(name);
        singerName.setText(singer);
        if (album == null) {
            player_cover.setImageResource(R.drawable.ic_launcher_foreground);
        } else {
            httpClient.newCall(new Request.Builder().url(album).build())
                    .enqueue(new ImageViewCallbackHandler(MainActivity.this, player_cover));
        }
    }

    @Override
    protected void onPause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        searchProgressDialog.cancel();
        super.onDestroy();
    }

    @Click(R.id.player_button)
    public void onPlayButtonClick(ImageView v) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            v.setImageResource(R.drawable.play);
        } else if (seekBar.getMax() > 0) {
            mediaPlayer.start();
            v.setImageResource(R.drawable.stop);
        }
    }

    @Click(R.id.download_button)
    public void onDownloadButtonClick() {
        if (downloadUrl == null || TextUtils.isEmpty(downloadUrl)) {
            return;
        }
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(downloadUrl));
        req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downloadFilename);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        req.allowScanningByMediaScanner();
        DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(req);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
