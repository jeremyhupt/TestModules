package com.ryanh.recyclerviewtest;

import android.widget.AdapterView;

import com.ryanh.ryanutils.activity.BaseListActivity;

public class MainActivity extends BaseListActivity {


    @Override
    protected String[] setDatas() {
        return new String[0];
    }

    @Override
    protected AdapterView.OnItemClickListener setOnItemClickListener() {
        return null;
    }
}
