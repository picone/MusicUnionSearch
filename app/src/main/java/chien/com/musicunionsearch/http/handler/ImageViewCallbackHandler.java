package chien.com.musicunionsearch.http.handler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ImageViewCallbackHandler implements Callback{

    private Activity activity;
    private ImageView imageView;

    public ImageViewCallbackHandler(Activity activity, ImageView imageView) {
        this.activity = activity;
        this.imageView = imageView;
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            InputStream is = responseBody.byteStream();
            final Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageBitmap(bitmap);
                }
            });
        }
    }
}
