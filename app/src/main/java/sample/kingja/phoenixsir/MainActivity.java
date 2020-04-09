package sample.kingja.phoenixsir;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kingja.phoenixsir.AppUpdater;
import com.kingja.phoenixsir.updater.net.INetCallback;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String TAG=getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppUpdater.getInstance().setNetManager(new OkhttpNetManager());
        final Map<String,String>paramMap=new HashMap<>();
        paramMap.put("version",String.valueOf(2019101001));
        findViewById(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUpdater.getInstance().getNetManager().post("http://app-api2.chinafwcq.com/android_update",paramMap, new INetCallback() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e(TAG, "onSuccess: "+response );

                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });
            }
        });
    }
}