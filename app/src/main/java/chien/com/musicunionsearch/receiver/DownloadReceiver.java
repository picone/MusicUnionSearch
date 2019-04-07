package chien.com.musicunionsearch.receiver;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.util.LongSparseArray;
import android.widget.Toast;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import chien.com.musicunionsearch.R;
import chien.com.musicunionsearch.models.SongItem;
import chien.com.musicunionsearch.util.ID3;

public class DownloadReceiver extends BroadcastReceiver {
    private LongSparseArray<SongItem> downloadList = new LongSparseArray<>();
    private static final String LOG_TAG = "download_receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && !intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            return;
        }
        long downloadId = intent.getLongExtra( DownloadManager.EXTRA_DOWNLOAD_ID , -1 );
        if (downloadId < 0) {
            return;
        }
        SongItem songItem = downloadList.get(downloadId);
        if (songItem == null) {
            return;
        }
        DownloadManager downloadManager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager == null) {
            return;
        }
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        Cursor cursor = downloadManager.query(query);
        if (cursor == null || !cursor.moveToFirst()) {
            return;
        }
        String localURI = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        try {
            ID3 id3 = new ID3(new File(new URI(localURI)));
            id3.setTag(songItem);
        } catch (InvalidDataException | IOException | UnsupportedTagException
                | NotSupportedException | URISyntaxException e) {
            Log.w(LOG_TAG, e);
        }
        Toast.makeText(context, R.string.toast_download_finish, Toast.LENGTH_SHORT).show();
    }

    public void enqueue(long downloadId, SongItem songItem) {
        downloadList.append(downloadId, songItem);
    }
}
