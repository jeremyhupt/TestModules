package com.hxqc.dbflowtest;

import android.app.Activity;
import android.os.Bundle;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;


public class DemoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlowManager.init(new FlowConfig.Builder(getApplicationContext()).build());

        OtherModel otherModel = new OtherModel();
        otherModel.name = "1";
        otherModel.save();
        AModel aModel = new AModel();
        aModel.name = "123";
        aModel.model =otherModel;
        aModel.time = 123456789;
        aModel.parseOnly = "dskdfjkd";
        aModel.parseOnly2 = 451231545;

        aModel.save();


    }

}
