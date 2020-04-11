package com.kingja.phoenixsir.updater.net;

import java.io.File;

/**
 * Description:TODO
 * Create Time:2020/4/9 0009 下午 2:09
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public interface INetDownloadCallback {
    void onDownloadSuccess(File apkFile);

    void onProgress(int progress);

    void onDownloadFailed(Throwable throwable);
}
