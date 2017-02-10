package com.ryanh.designpatterntest;

import android.view.View;
import android.widget.AdapterView;

import com.ryanh.designpatterntest.util.ToActivityUtils;
import com.ryanh.ryanutils.activity.BaseListActivity;

public class MainActivity extends BaseListActivity {

    @Override
    protected String[] setDatas() {
        String[] str = {"Factory Pattern"};
        return str;
    }

    @Override
    protected AdapterView.OnItemClickListener setOnItemClickListener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        ToActivityUtils.toFactoryPatternActivity(MainActivity.this);
                        break;
                    default:
                        break;
                }
            }
        };
        return listener;
    }
}
