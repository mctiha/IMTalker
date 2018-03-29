package com.italker.push;

import com.igexin.sdk.PushManager;
import com.italker.common.app.Application;
import com.italker.factory.Factory;

/**
 *
 * Created by mth on 2018/1/1.
 */

public class App extends Application {
//    Application


    @Override
    public void onCreate() {
        super.onCreate();

        // 调用Factory进行初始化
        Factory.setup();
        // 推送进行初始化
        PushManager.getInstance().initialize(this);
    }
}
