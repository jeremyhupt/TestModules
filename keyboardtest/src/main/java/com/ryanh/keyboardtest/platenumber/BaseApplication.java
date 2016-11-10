package com.ryanh.keyboardtest.platenumber;

import android.app.Application;

import com.antfortune.freeline.FreelineCore;

/**
 * Author:胡仲俊
 * Date: 2016 - 11 - 10
 * FIXME
 * Todo
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FreelineCore.init(this);
    }
}
