package com.kingja.phoenixsir.updater.net;

/**
 * Description:TODO
 * Create Time:2020/4/9 0009 下午 2:09
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public interface INetCallback {
    void onSuccess(String response);
    void onFailed(Throwable throwable);
}
