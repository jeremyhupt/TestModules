package com.ryanh.designpatterntest.util;

import android.content.Context;
import android.content.Intent;

import com.ryanh.designpatterntest.factorypattern.inter.activity.FactoryPatternActivity;

/**
 * Author:胡仲俊
 * Date: 2017 - 02 - 10
 * Des:
 * FIXME
 * TODO
 */

public class ToActivityUtils {

    public static void toFactoryPatternActivity(Context context) {
        context.startActivity(new Intent(context, FactoryPatternActivity.class));
    }
}
