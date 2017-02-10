package com.ryanh.designpatterntest.factorypattern.inter.activity;

import android.view.View;
import android.widget.AdapterView;

import com.ryanh.designpatterntest.factorypattern.inter.factory.ShapeFactory;
import com.ryanh.ryanutils.activity.BaseListActivity;

/**
 * Author:胡仲俊
 * Date: 2017 - 02 - 10
 * Des: 工厂模式测试页面
 * FIXME
 * TODO
 */

public class FactoryPatternActivity extends BaseListActivity {

    private ShapeFactory mShapeFactory;

    @Override
    protected void onResume() {
        super.onResume();
        mShapeFactory = new ShapeFactory();
    }

    @Override
    protected String[] setDatas() {
        String[] strings = {"Rectangle",
                "Square",
                "Circle"};
        return strings;
    }

    @Override
    protected AdapterView.OnItemClickListener setOnItemClickListener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mShapeFactory.getShape("RECTANGLE").draw(FactoryPatternActivity.this);
                        break;
                    case 1:
                        mShapeFactory.getShape("SQUARE").draw(FactoryPatternActivity.this);
                        break;
                    case 2:
                        mShapeFactory.getShape("CIRCLE").draw(FactoryPatternActivity.this);
                        break;
                    default:
                        break;
                }
            }
        };
        return listener;
    }
}
