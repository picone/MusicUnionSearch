package chien.com.musicunionsearch.util;

import com.mpatric.mp3agic.ID3v23Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import chien.com.musicunionsearch.models.SongItem;

public class ID3 {
    private File file;
    private Mp3File mp3File;
    private final static int BUFFER_SIZE = 4096;

    public ID3(File file) throws InvalidDataException, IOException, UnsupportedTagException {
        this.file = file;
        mp3File = new Mp3File(file);
    }

    public boolean setTag(SongItem songItem) throws IOException, NotSupportedException {
        ID3v23Tag tag = new ID3v23Tag();
        tag.setTitle(songItem.name);
        tag.setArtist(songItem.artist);
        tag.setAlbumArtist(songItem.artist);
        tag.setAlbum(songItem.album);
        mp3File.setId3v2Tag(tag);
        //写入临时文件
        File tmpFile = File.createTempFile("id3_", ".tmp");
        mp3File.save(tmpFile.getAbsolutePath());
        //临时文件替换回来
        FileInputStream inputStream = new FileInputStream(tmpFile);
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file, false));
        byte buffer[] = new byte[BUFFER_SIZE];
        int len;
        while ((len = inputStream.read(buffer, 0, BUFFER_SIZE)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        return tmpFile.delete();
    }
}
