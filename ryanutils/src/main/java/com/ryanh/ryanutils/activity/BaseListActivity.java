package com.ryanh.ryanutils.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Author:胡仲俊
 * Date: 2017 - 01 - 03
 * Des:
 * FIXME
 * Todo
 */

public abstract class BaseListActivity extends AppCompatActivity {

    protected ListView mListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListView = new ListView(this);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, setDatas()));
        setContentView(mListView);
        mListView.setOnItemClickListener(setOnItemClickListener());
    }

    protected abstract String[] setDatas();

    protected abstract AdapterView.OnItemClickListener setOnItemClickListener();

}
