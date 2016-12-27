package com.ryanh.keyboardtest.edittext;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ryanh.keyboardtest.KeyboardUtil;
import com.ryanh.keyboardtest.R;


public class EditTextActivity extends AppCompatActivity {

    private EditText viewById;
    private EditText viewById1;
    private KeyboardView viewById2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewById = (EditText) findViewById(R.id.edit);
        viewById1 = (EditText) findViewById(R.id.editone);
        viewById2 = (KeyboardView) findViewById(R.id.keyboard_view);

        viewById1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(!viewById1.hasFocus()){
                    int inputback = viewById1.getInputType();
                    viewById1.setInputType(InputType.TYPE_NULL);
                    new KeyboardUtil(viewById2, viewById1).showKeyboard();
                    viewById1.setInputType(inputback);
                }
                return false;
            }
        });
    }
}
