package chien.com.musicunionsearch.http.handler;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import chien.com.musicunionsearch.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

public abstract class SimpleCallbackHandler<ResultType> implements Callback {

    private Activity activity;
    private Class<ResultType> clazz;

    @Override
    public void onFailure(Call call, IOException e) {
        showFailToast();
    }

    private void showFailToast() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity.getApplicationContext(), R.string.toast_network_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected SimpleCallbackHandler(Activity activity) {
        this.activity = activity;
        Class clazz = getClass();
        while (clazz != Object.class) {
            Type t = clazz.getGenericSuperclass();
            if (t instanceof ParameterizedType) {
                Type[] args = ((ParameterizedType) t).getActualTypeArguments();
                if (args[0] instanceof Class) {
                    this.clazz = (Class<ResultType>) args[0];
                    break;
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            String responseString = responseBody.string();
            Gson gson = new Gson();
            try {
                ResultType resp = gson.fromJson(responseString, clazz);
                onResult(call, resp);
            } catch (JsonSyntaxException e) {
                Log.w("JSON", "json decode fail:" + responseString);
                showFailToast();
            }
        }
    }

    public abstract void onResult(Call call, ResultType response);
}
