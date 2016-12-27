package com.ryanh.keyboardtest.platenumber;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ActionMode;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.ryanh.keyboardtest.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Author:胡仲俊
 * Date: 2016 - 12 - 21
 * FIXME
 * Todo
 */

public class PlateNumEditText extends EditText implements KeyboardView.OnKeyboardActionListener{

    private Keyboard mKeyboard;
    private KeyboardView mKeyboardView;

    private Window mWindow;
    private View mDecorView;
    private View mContentView;
    private Context mContext;

    private PopupWindow mKeyboardWindow;

    private boolean isNeedCustomKeyboard = true; // 是否启用自定义键盘
    /**
     * 输入框在被键盘弹出时，要被推上的距离
     */
    private int mScrollDistance = 0;

    public static int screenWidth = -1; // 未知宽高
    public static int screenHeight = -1;

    /**
     * 不包含导航栏的高度
     */
    public static int screenHeightNoNavBar = -1;
    /**
     * 实际内容高度，  计算公式:屏幕高度-导航栏高度-电量栏高度
     */
    public static int screenContentHeight = -1;
    public static float density = 1.0f;
    public static int densityDpi = 160;

    public PlateNumEditText(Context context) {
        this(context, null);
    }

    public PlateNumEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlateNumEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initAttributes(context);
        this.addTextChangedListener(textChangedListener);
    }

    private void initKeyboard(int xmlLayoutResId) {
        mKeyboard = new Keyboard(mContext, xmlLayoutResId);

        mKeyboardView = (KeyboardView) LayoutInflater.from(mContext).inflate(R.layout.view_keyboard, null);

        mKeyboardView.setKeyboard(mKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(this);

        mKeyboardWindow = new PopupWindow(mKeyboardView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mKeyboardWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mScrollDistance > 0) {
                    int temp = mScrollDistance;
                    mScrollDistance = 0;
                    if (null != mContentView) {
                        mContentView.scrollBy(0, -temp);
                    }
                }
            }
        });

        hideSysInput();
        showKeyboard();
    }

    private void initAttributes(Context context) {
        initScreenParams(context);
        this.setLongClickable(false); // 设置EditText不可长按
        this.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        removeCopyAbility();

        if (this.getText() != null) {
            this.setSelection(this.getText().length());
        }

        /*this.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard();
                } else {
                    showKeyboard();
                }
            }
        });*/
    }

    @TargetApi(11)
    private void removeCopyAbility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            this.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }
    }

    private void initScreenParams(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(metrics);

        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        density = metrics.density;
        densityDpi = metrics.densityDpi;

        screenHeightNoNavBar = screenHeight;

        int version = Build.VERSION.SDK_INT;
        // 新版本的android 系统有导航栏，造成无法正确获取高度
        if (version == 13) {
            try {
                Method method = display.getClass().getMethod("getRealHeight");
                screenHeightNoNavBar = (Integer) method.invoke(display);
            } catch (Exception e) {
                // do nothing
            }
        } else if (version > 13) {
            try {
                Method method = display.getClass().getMethod("getRawHeight");
                screenHeightNoNavBar = (Integer) method.invoke(display);
            } catch (Exception e) {
                // do nothing
            }
        }

        screenContentHeight = screenHeightNoNavBar - getStatusBarHeight(context);
    }

    /**
     * 获取状态栏高度
     */
    private int getStatusBarHeight(Context context) {
        Class<?> clazz = null;
        Object object = null;
        Field field = null;
        int x = 0;
        int statusBarHeight = 0;
        try {
            clazz = Class.forName("com.android.internal.R$dimen");
            object = clazz.newInstance();
            field = clazz.getField("status_bar_height");
            x = Integer.parseInt(field.get(object).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusBarHeight;
    }


    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = this.getText();
            int start = this.getSelectionStart();
            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 隐藏键盘
                hideKeyboard();
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            } else if (0x0 <= primaryCode && primaryCode <= 0x7f) {
                //可以直接输入的字符(如0-9,.)，他们在键盘映射xml中的keycode值必须配置为该字符的ASCII码
                editable.insert(start, Character.toString((char) primaryCode));
            } else if (primaryCode > 0x7f) {
                Keyboard.Key key = getKeyByKeyCode(primaryCode);
                //可以直接输入的字符(如0-9,.)，他们在键盘映射xml中的keycode值必须配置为该字符的ASCII码
                editable.insert(start, key.label);

            } else if (primaryCode == EditorInfo.IME_ACTION_DONE) {
                //其他一些暂未开放的键指令，如next到下一个输入框等指令

            }
    }

    private Keyboard.Key getKeyByKeyCode(int primaryCode) {
        if (null != mKeyboard) {
            List<Keyboard.Key> keyList = mKeyboard.getKeys();
            for (int i = 0, size = keyList.size(); i < size; i++) {
                Keyboard.Key key = keyList.get(i);

                int codes[] = key.codes;

                if (codes[0] == primaryCode) {
                    return key;
                }
            }
        }

        return null;
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        requestFocus();
        requestFocusFromTouch();
        if (TextUtils.isEmpty(getText().toString())) {
            initKeyboard(R.xml.licence_city);
        } else {
            if (TextUtils.isEmpty(getText().toString().substring(0, 1))) {
                initKeyboard(R.xml.licence_city);
            } else {
                initKeyboard(R.xml.licence_num);
                hideSysInput();
                showKeyboard();
            }
        }
        if (isNeedCustomKeyboard) {
            hideSysInput();
            showKeyboard();
        }

        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (null != mKeyboardWindow) {
                if (mKeyboardWindow.isShowing()) {
                    mKeyboardWindow.dismiss();
                    return true;
                }
            }
        }

        return super.onKeyDown(keyCode, event);

    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        this.mWindow = ((Activity) mContext).getWindow();
        this.mDecorView = this.mWindow.getDecorView();
        this.mContentView = this.mWindow.findViewById(Window.ID_ANDROID_CONTENT);

        hideSysInput();
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        hideKeyboard();

        mKeyboardWindow = null;
        mKeyboardView = null;
        mKeyboard = null;

        mDecorView = null;
        mContentView = null;
        mWindow = null;
    }

    public void showKeyboard() {
        if (mKeyboardWindow != null) {
            if (!mKeyboardWindow.isShowing()) {

                if (mDecorView != null) {
                    mKeyboardView.setKeyboard(mKeyboard);
                    mKeyboardWindow.setBackgroundDrawable(new BitmapDrawable());
                    mKeyboardWindow.setOutsideTouchable(true);
                    mKeyboardWindow.showAtLocation(this.mDecorView, Gravity.BOTTOM, 0, 0);
                    mKeyboardWindow.update();
                }

                /*if (null != mDecorView && null != mContentView) {
                    int[] pos = new int[2];
                    getLocationOnScreen(pos);
                    float height = dpToPx(getContext(), 240);

                    int[] hsmlpos = new int[2];
                    mDecorView.getLocationOnScreen(hsmlpos);

                    Rect outRect = new Rect();
                    mDecorView.getWindowVisibleDisplayFrame(outRect);

                    int screen = screenContentHeight;
                    mScrollDistance = (int) ((pos[1] + getMeasuredHeight() - outRect.top) - (screen - height));

                    if (mScrollDistance > 0) {
                        mContentView.scrollBy(0, mScrollDistance);
                    }
                }*/
            }
        }
    }

    public void hideKeyboard() {
        if (null != mKeyboardWindow) {
            if (mKeyboardWindow.isShowing()) {
                mKeyboardWindow.dismiss();
            }
        }
    }

    /**
     * 密度转换为像素值
     */
    private float dpToPx(Context context, int dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private void hideSysInput() {
        if (this != null) {
            if (this.getWindowToken() != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(this.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private TextWatcher textChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(!TextUtils.isEmpty(s.toString())) {
                initKeyboard(R.xml.licence_num);
                hideSysInput();
                showKeyboard();
            } else {
                initKeyboard(R.xml.licence_city);
                hideSysInput();
                showKeyboard();
            }
        }
    };
}
