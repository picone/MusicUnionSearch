package chien.com.musicunionsearch.util;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chien.com.musicunionsearch.models.SongItem;

public class ID3Test {
    private List<File> testFileList = new ArrayList<>();
    private static final String testName = "testName";
    private static final String testArtist = "testArtist";

    public ID3Test() {
        testFileList.add(new File("test/haha.mp3"));
    }

    @Test
    public void testSetTag() throws InvalidDataException, IOException, UnsupportedTagException, NotSupportedException {
        for (File testFile: testFileList) {
            ID3 id3 = new ID3(testFile);
            SongItem songItem = new SongItem();
            songItem.name = testName;
            songItem.artist = testArtist;
            boolean result = id3.setTag(songItem);
            Assert.assertEquals(result, true);
        }
    }
}
