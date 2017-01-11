package com.ryanh.keyboardtest;

import android.content.Intent;
import android.view.View;

import com.ryanh.keyboardtest.dialog.DialogActivity;
import com.ryanh.keyboardtest.edittext.EditTextActivity;
import com.ryanh.keyboardtest.platenumber.PlateNumberActivity;
import com.ryanh.ryanutils.activity.BaseRecyclerActivity;

public class MainActivity extends BaseRecyclerActivity {

    @Override
    protected String[] setDatas() {
        String[] str = {
                "PlateNumberActivity",
                "EditTextActivity",
                "DialogActivity"
        };
        return str;
    }

    @Override
    protected View.OnClickListener setOnItemClickListener(int position) {
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, PlateNumberActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, EditTextActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, DialogActivity.class));
                        break;

                    default:
                        break;
                }
            }
        };
        return onClickListener;
    }

    /*@Override
    protected AdapterView.OnItemClickListener setOnItemClickListener() {
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, PlateNumberActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, EditTextActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, DialogActivity.class));
                        break;

                    default:
                        break;
                }
            }
        };
        return listener;
    }*/

}
