package com.kingja.phoenixsir;

import com.kingja.phoenixsir.updater.client.OkhttpNetManager;
import com.kingja.phoenixsir.updater.net.INetManager;

/**
 * Description:TODO
 * Create Time:2020/4/9 0009 下午 2:06
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class AppUpdater {

    private static AppUpdater sInstance = new AppUpdater();
    private INetManager netManager = new OkhttpNetManager();

    public void setNetManager(INetManager netManager) {
        this.netManager = netManager;
    }

    public INetManager getNetManager() {
        return netManager;
    }

    public static AppUpdater getInstance() {
        return sInstance;
    }
}
