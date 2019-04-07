package chien.com.musicunionsearch.models;

import android.support.annotation.Nullable;

import java.util.Locale;

public class SongItem {
    public String name;
    public String artist;
    public String album;
    public @Nullable String albumImage;
    public String downloadUrl;
    public String extensionName;

    public String getFilename() {
        return String.format(Locale.getDefault(), "%s - %s.%s", name, artist, extensionName);
    }
}
