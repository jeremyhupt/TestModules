package com.ryanh.ryanutils.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.ryanh.ryanutils.R;

/**
 * Author:胡仲俊
 * Date: 2017 - 01 - 04
 * Des:
 * FIXME
 * Todo
 */

public class CleanEditText extends EditText implements View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private Drawable mRight;
    private OnTouchListener mTouchListener;
    private OnFocusChangeListener mFocusChangeListener;

    public CleanEditText(Context context) {
        super(context);
        init();
    }

    public CleanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CleanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRight = getCompoundDrawables()[2];
        if (mRight == null) {
            mRight = getResources().getDrawable(R.mipmap.et_delete);
        }
        mRight.setBounds(0, 0, mRight.getIntrinsicWidth(), mRight.getIntrinsicHeight());
        setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.margin_micro));
        super.setOnFocusChangeListener(this);
        super.setOnTouchListener(this);
        addTextChangedListener(this);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.mFocusChangeListener = l;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.mTouchListener = l;
    }

    private void setClearIconVisible(boolean visible) {
        Drawable temp = visible ? mRight : null;
        Drawable[] drawables = getCompoundDrawables();
        setCompoundDrawables(drawables[0], drawables[1], temp, drawables[3]);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        setClearIconVisible(hasFocus && !TextUtils.isEmpty(getText()));
        if (mFocusChangeListener != null) {
            mFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getCompoundDrawables()[2] != null) {
            boolean tapped = event.getX() > (getWidth() - getPaddingRight() - mRight.getIntrinsicWidth());
            if (tapped) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                }
                return true;
            }
        }
        if (mTouchListener != null) {
            return mTouchListener.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //ignore
    }

    @Override
    public void afterTextChanged(Editable s) {
        setClearIconVisible(isFocused() && !TextUtils.isEmpty(s));
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
    }
}
