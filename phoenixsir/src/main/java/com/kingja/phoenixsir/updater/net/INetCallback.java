package com.kingja.phoenixsir.updater.net;

/**
 * Description:TODO
 * Create Time:2020/4/9 0009 下午 2:09
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public interface INetCallback {
    void onUpdateSuccess(String response);
    void onUpdateFailed(Throwable throwable);
}
