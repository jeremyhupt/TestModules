package com.ryanh.keyboardtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ryanh.keyboardtest.dialog.DialogActivity;
import com.ryanh.keyboardtest.edittext.EditTextActivity;
import com.ryanh.keyboardtest.platenumber.PlateNumberActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "J.Log";
    private ListView listView;
    private String[] str = {
            "PlateNumberActivity",
            "EditTextActivity",
            "DialogActivity"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = new ListView(this);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, str));
        setContentView(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        Log.i(TAG,"start PlateNumberActivity");
                        startActivity(new Intent(MainActivity.this,PlateNumberActivity.class));
                        break;
                    case 1:
                        Log.i(TAG,"start EditTextActivity");
                        startActivity(new Intent(MainActivity.this,EditTextActivity.class));
                        break;
                    case 2:
                        Log.i(TAG,"start DialogActivity");
                        startActivity(new Intent(MainActivity.this,DialogActivity.class));
                        break;

                    default:
                        break;
                }
            }
        });
    }

    private void jumpPage(Class<?> cls) {
        startActivity(new Intent(MainActivity.this, cls));
    }


}
