package com.ryanh.designpatterntest.factorypattern.inter;

import android.content.Context;

import com.ryanh.designpatterntest.factorypattern.inter.base.Shape;
import com.ryanh.ryanutils.commonutils.ToastUtils;

/**
 * Author:胡仲俊
 * Date: 2017 - 02 - 10
 * Des:
 * FIXME
 * TODO
 */

public class Rectangle implements Shape {

    @Override
    public void draw(Context context) {
        ToastUtils.show(context,"Inside Rectangle::draw() method.");
    }

}
