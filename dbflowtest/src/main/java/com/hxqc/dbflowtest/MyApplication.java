package com.hxqc.dbflowtest;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Author:胡仲俊
 * Date: 2016 - 10 - 13
 * FIXME
 * Todo
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //Stetho工具初始化
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        FlowManager.init(new FlowConfig.Builder(this).build());

    }
}
