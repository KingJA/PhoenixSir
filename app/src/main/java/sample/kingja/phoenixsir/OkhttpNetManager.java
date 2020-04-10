package sample.kingja.phoenixsir;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.kingja.phoenixsir.updater.net.INetCallback;
import com.kingja.phoenixsir.updater.net.INetDownloadCallback;
import com.kingja.phoenixsir.updater.net.INetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Description:TODO
 * Create Time:2020/4/9 0009 下午 2:13
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class OkhttpNetManager implements INetManager {
    private static OkHttpClient sOkhttpClient;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(15, TimeUnit.SECONDS);
        sOkhttpClient = builder.build();
    }

    private String TAG = getClass().getSimpleName();

    @Override
    public void get(String url, INetCallback callback) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().addHeader("Accept-Encoding", "identity").build();
        setCallback(request, callback);

    }

    @Override
    public void post(String url, Map<String, String> paramsMap, INetCallback callback) {
        Log.e(TAG, "post: " + url);
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        Set<String> keySet = paramsMap.keySet();
        for (String key : keySet) {
            String value = paramsMap.get(key);
            formBodyBuilder.add(key, value);
        }
        FormBody formBody = formBodyBuilder.build();
        Request request = new Request
                .Builder()
                .post(formBody)
                .url(url)
                .addHeader("Accept-Encoding", "identity")
                .build();
        setCallback(request, callback);

    }

    private void setCallback(Request request, final INetCallback callback) {
        Call call = sOkhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailed(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(string);
                        }
                    });
                } catch (Throwable e) {
                    callback.onFailed(e);
                }
            }
        });
    }

    @Override
    public void download(String url, final File targetFile, final INetDownloadCallback callback) {
        if (!targetFile.exists()) {
            targetFile.getParentFile().mkdirs();
        }
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Call call = sOkhttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                sHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFailed(e);
                    }
                });

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                OutputStream os = null;
                try {
                    final long totalLen = response.body().contentLength();
                    Log.e(TAG, "contentLength: " + totalLen);
                    is = response.body().byteStream();
                    os = new FileOutputStream(targetFile);
                    byte[] buffer = new byte[8 * 1024];
                    long curLen = 0;
                    int bufferLen = 0;
                    while ((bufferLen = is.read(buffer)) != -1) {
                        os.write(buffer, 0, bufferLen);
                        os.flush();
                        curLen += bufferLen;
                        final long finalCurLen = curLen;
                        sHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.e(TAG, "finalCurLen: " + finalCurLen + " totalLen: " + totalLen);
                                callback.onProgress((int) (finalCurLen * 1.0f / totalLen * 100));
                            }
                        });
                    }
                    try {
                        targetFile.setExecutable(true, false);
                        targetFile.setReadable(true, false);
                        targetFile.setWritable(true, false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onSuccess(targetFile);
                        }
                    });
                } catch (final Throwable e) {
                    e.printStackTrace();
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFailed(e);
                        }
                    });
                } finally {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                }

            }
        });


    }


}
