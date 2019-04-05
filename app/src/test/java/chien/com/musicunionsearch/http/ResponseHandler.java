package chien.com.musicunionsearch.http;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Response;
import okhttp3.ResponseBody;

public class ResponseHandler {
    public static @Nullable Object parseResponse(Response resp, Class clazz) throws IOException {
        if (resp.isSuccessful() && resp.code() == 200) {
            ResponseBody respBody = resp.body();
            if (respBody != null) {
                String respString = respBody.string();
                Gson gson = new Gson();
                return gson.fromJson(respString, clazz);
            }
        }
        return null;
    }
}
