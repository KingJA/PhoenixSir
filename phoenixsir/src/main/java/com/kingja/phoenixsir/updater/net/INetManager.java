package com.kingja.phoenixsir.updater.net;

import java.io.File;
import java.util.Map;

/**
 * Description:TODO
 * Create Time:2020/4/9 0009 下午 2:09
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public interface INetManager {
    void get(String url, INetCallback callback);
    void post(String url, Map<String, String > paramsMap, INetCallback callback);

    void download(String url, File targetFile, INetDownloadCallback callback);
}
