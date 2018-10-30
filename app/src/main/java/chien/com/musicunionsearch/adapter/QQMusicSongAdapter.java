package chien.com.musicunionsearch.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import chien.com.musicunionsearch.activity.MainActivity;
import chien.com.musicunionsearch.holder.SongViewHolder;
import chien.com.musicunionsearch.http.handler.SimpleCallbackHandler;
import chien.com.musicunionsearch.http.request.QQMusic;
import chien.com.musicunionsearch.http.response.QQMusicPlaySongResponse;
import chien.com.musicunionsearch.http.response.QQMusicSearchSongResponse;
import okhttp3.Call;
import okhttp3.Request;

public class QQMusicSongAdapter extends RecyclerView.Adapter {

    private QQMusicSearchSongResponse searchResult;
    private Activity activity;

    public QQMusicSongAdapter(QQMusicSearchSongResponse searchResult, Activity activity) {
        this.searchResult = searchResult;
        this.activity = activity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        final QQMusicSearchSongResponse.Data.Song.SongItem song = searchResult.data.song.list.get(position);
        ((SongViewHolder)holder).name.setText(song.name);
        ((SongViewHolder)holder).singer.setText(song.singer.get(0).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResult.data.song.list.size();
    }

    private void playMusic(final QQMusicSearchSongResponse.Data.Song.SongItem song) {
        final String guid = QQMusic.getGuid();
        Request req = QQMusic.playSong(song.mid, guid);
        ((MainActivity)activity).httpClient.newCall(req).enqueue(new SimpleCallbackHandler<QQMusicPlaySongResponse>(activity) {
            @Override
            public void onResult(Call call, QQMusicPlaySongResponse response) {
                QQMusicPlaySongResponse.Data.Item item = response.data.items.get(0);
                final String url = QQMusic.getPlayerUrl(item.filename, item.vkey, guid);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)activity).playMusic(url, song.name, song.singer.get(0).name, QQMusic.getAlbumUrl(song.album.mid));
                    }
                });
            }
        });
    }
}
