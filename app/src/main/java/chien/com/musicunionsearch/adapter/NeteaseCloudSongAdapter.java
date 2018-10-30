package chien.com.musicunionsearch.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import chien.com.musicunionsearch.activity.MainActivity;
import chien.com.musicunionsearch.holder.SongViewHolder;
import chien.com.musicunionsearch.http.handler.SimpleCallbackHandler;
import chien.com.musicunionsearch.http.request.NeteaseCloud;
import chien.com.musicunionsearch.http.response.NeteaseCloudPlayerUrlResponse;
import chien.com.musicunionsearch.http.response.NeteaseCloudSearchSongResponse;
import okhttp3.Call;
import okhttp3.Request;

public class NeteaseCloudSongAdapter extends RecyclerView.Adapter {

    private NeteaseCloudSearchSongResponse searchResult;
    private Activity activity;

    public NeteaseCloudSongAdapter(NeteaseCloudSearchSongResponse searchResult, Activity activity) {
        this.searchResult = searchResult;
        this.activity = activity;
        // 过滤没有版权的歌曲
        List<NeteaseCloudSearchSongResponse.Result.SongItem> newList = new ArrayList<>();
        for (NeteaseCloudSearchSongResponse.Result.SongItem song : searchResult.result.songs) {
            if (song.privilege.st >= 0) {
                newList.add(song);
            }
        }
        this.searchResult.result.songs = newList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = activity.getLayoutInflater().inflate(android.R.layout.simple_list_item_2, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NeteaseCloudSearchSongResponse.Result.SongItem song = searchResult.result.songs.get(position);
        ((SongViewHolder)holder).name.setText(song.name);
        ((SongViewHolder)holder).singer.setText(song.ar.get(0).name);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playMusic(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchResult.result.songs.size();
    }

    private void playMusic(final NeteaseCloudSearchSongResponse.Result.SongItem song) {
        Request req = new NeteaseCloud().playerUrl(song.id);
        ((MainActivity)activity).httpClient.newCall(req).enqueue(new SimpleCallbackHandler<NeteaseCloudPlayerUrlResponse>(activity) {
            @Override
            public void onResult(Call call, final NeteaseCloudPlayerUrlResponse response) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String url = response.data.get(0).url;
                        String filename = String.format(Locale.getDefault(), "%s - %s.%s", song.name, song.ar.get(0).name, response.data.get(0).type);
                        ((MainActivity)activity).playMusic(url, song.name, song.ar.get(0).name, song.al.picUrl, filename);
                    }
                });
            }
        });
    }
}
