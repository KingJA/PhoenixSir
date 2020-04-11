package sample.kingja.phoenixsir;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kingja.phoenixsir.AppUpdater;
import com.kingja.phoenixsir.updater.client.OkhttpNetManager;
import com.kingja.phoenixsir.updater.net.INetCallback;
import com.kingja.phoenixsir.updater.net.INetDownloadCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AppUpdater.getInstance().setNetManager(new OkhttpNetManager());
        final Map<String, String> paramMap = new HashMap<>();
        paramMap.put("version", String.valueOf(2019101001));
        findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpdater.getInstance().getNetManager().post("http://app-api2.chinafwcq.com/android_update",
                        paramMap, new INetCallback() {
                            @Override
                            public void onUpdateSuccess(String response) {
                                Log.e(TAG, "onDownloadSuccess: " + response);
                                ResultData resultData = ResultData.parse(response);
                                Log.e(TAG, "getDownload_url: " + resultData.getData().getDownload_url());
                                File targetFile = new File(getCacheDir(), "target.apk");
                                AppUpdater.getInstance().getNetManager().download(resultData.getData().getDownload_url(), targetFile,
                                        new INetDownloadCallback() {
                                            @Override
                                            public void onDownloadSuccess(File apkFile) {
                                                Log.e(TAG, "onDownloadSuccess: " + apkFile.getAbsolutePath());
                                                installApk(MainActivity.this, apkFile);

                                            }

                                            @Override
                                            public void onProgress(int progress) {
                                                Log.e(TAG, "onProgress: " + progress);
                                            }

                                            @Override
                                            public void onDownloadFailed(Throwable throwable) {
                                                Log.e(TAG, "onDownloadFailed: " + throwable.getMessage());
                                            }
                                        });

                            }

                            @Override
                            public void onUpdateFailed(Throwable throwable) {

                            }
                        });
            }
        });
    }

    public static void installApk(Activity activity, File apkFile) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        // N FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(activity, activity.getPackageName() + ".fileprovider", apkFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(apkFile);
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        activity.startActivity(intent);
        // O Inatall Permission适配
    }
}
