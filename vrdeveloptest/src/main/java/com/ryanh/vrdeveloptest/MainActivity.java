package com.ryanh.vrdeveloptest;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import com.ryanh.ryanutils.activity.BaseListActivity;


public class MainActivity extends BaseListActivity {

    @Override
    protected String[] setDatas() {
        String[] str = {
                "VRImageActivity",
                "VRVideoActivity"
        };
        return str;
    }

    @Override
    protected AdapterView.OnItemClickListener setOnItemClickListener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, VRImageActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, VRVideoActivity.class));
                        break;

                    default:
                        break;
                }
            }
        };
        return listener;
    }

}
