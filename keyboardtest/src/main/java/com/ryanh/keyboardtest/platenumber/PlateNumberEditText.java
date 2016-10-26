package com.ryanh.keyboardtest.platenumber;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.ryanh.keyboardtest.R;

/**
 * Author:胡仲俊
 * Date: 2016 - 10 - 26
 * FIXME
 * Todo
 */

public class PlateNumberEditText extends LinearLayout {

    private static final String TAG = "J.Log";
    private Context mContext;
    private CityEditText mCityEditText;
    private NumberEditText mNumberEditText;

    public PlateNumberEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.mContext = context;
        initView(context,attrs);

        initEvent();
    }

    /**
     * 初始化控件
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {

        LayoutInflater.from(context).inflate(R.layout.view_common_plate_num, this);

        mCityEditText = (CityEditText) findViewById(R.id.common_license_city);

        mNumberEditText = (NumberEditText) findViewById(R.id.common_license_num);

        LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);

    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        Log.i(TAG, "initEvent");
        mCityEditText.addTextChangedListener(cityChangeListener);
        mNumberEditText.addTextChangedListener(numberChangeListener);
    }

    private TextWatcher cityChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            jumpNumKerboard();
        }
    };
    private TextWatcher numberChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            jumpNumKerboard();
        }
    };

    /**
     * 跳转到字母键盘
     */
    public void jumpNumKerboard() {
        mNumberEditText.setFocusable(true);
        mNumberEditText.setFocusableInTouchMode(true);
        mNumberEditText.requestFocus();
        mNumberEditText.requestFocusFromTouch();
    }


}
