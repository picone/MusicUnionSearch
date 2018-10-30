package chien.com.musicunionsearch.http.handler;

import android.app.Activity;
import android.widget.Toast;

import com.google.gson.Gson;

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
            Gson gson = new Gson();
            onResult(call, gson.fromJson(responseBody.string(), clazz));
        }
    }

    public abstract void onResult(Call call, ResultType response);
}
