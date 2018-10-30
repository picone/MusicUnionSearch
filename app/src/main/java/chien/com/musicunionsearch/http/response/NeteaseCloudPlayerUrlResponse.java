package chien.com.musicunionsearch.http.response;

import java.util.List;

public class NeteaseCloudPlayerUrlResponse {

    public List<DataItem> data;

    public static class DataItem {
        public String url;
    }
}
