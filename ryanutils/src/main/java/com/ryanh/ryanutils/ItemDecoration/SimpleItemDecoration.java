package com.ryanh.ryanutils.ItemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.ryanh.ryanutils.R;

/**
 * Author:胡仲俊
 * Date: 2017 - 01 - 04
 * FIXME
 * Todo
 */

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {

    public final int DECORATION_LINEAR = 0;
    public final int DECORATION_LABEL = 1;
    public final int DECORATION_EXPANDABLE = 2;

    private Paint dividerPaint;
    private int dividerHeight;
    private int mDecorationType = -1;

    private int tagWidth;
    private Paint leftPaint;
    private Paint rightPaint;

    private DecorationCallback callback;
    private TextPaint textPaint;
    private Paint paint;
    private int topGap;
    private Paint.FontMetrics fontMetrics;

    public void initDecorationType(Context context, int decorationType) {
        mDecorationType = decorationType;
        if (decorationType == DECORATION_LINEAR) {
            dividerPaint = new Paint();
            dividerPaint.setColor(context.getResources().getColor(R.color.gray));
            dividerHeight = 10;
        } else if (decorationType == DECORATION_LABEL) {
            leftPaint = new Paint();
            leftPaint.setColor(context.getResources().getColor(R.color.gray));
            rightPaint = new Paint();
            rightPaint.setColor(context.getResources().getColor(R.color.black));
            tagWidth = 20;
        }
    }

    public void initDecorationType(Context context, int decorationType, DecorationCallback decorationCallback) {
        mDecorationType = decorationType;
        if (decorationType == DECORATION_EXPANDABLE) {
            this.callback = decorationCallback;

            paint = new Paint();
            paint.setColor(context.getResources().getColor(R.color.gray));

            textPaint = new TextPaint();
            textPaint.setTypeface(Typeface.DEFAULT_BOLD);
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(20);
            textPaint.setColor(Color.BLACK);
            textPaint.getFontMetrics(fontMetrics);
            textPaint.setTextAlign(Paint.Align.LEFT);
            fontMetrics = new Paint.FontMetrics();
            topGap = 20;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mDecorationType == DECORATION_LINEAR) {
            int childCount = parent.getChildCount();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();

            for (int i = 0; i < childCount - 1; i++) {
                View view = parent.getChildAt(i);
                float top = view.getBottom();
                float bottom = view.getBottom() + dividerHeight;
                c.drawRect(left, top, right, bottom, dividerPaint);
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        if (mDecorationType == DECORATION_LABEL) {
            int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                int pos = parent.getChildAdapterPosition(child);
                boolean isLeft = pos % 2 == 0;
                if (isLeft) {
                    float left = child.getLeft();
                    float right = left + tagWidth;
                    float top = child.getTop();
                    float bottom = child.getBottom();
                    c.drawRect(left, top, right, bottom, leftPaint);
                } else {
                    float right = child.getRight();
                    float left = right - tagWidth;
                    float top = child.getTop();
                    float bottom = child.getBottom();
                    c.drawRect(left, top, right, bottom, rightPaint);

                }
            }
        } else if (mDecorationType == DECORATION_EXPANDABLE) {
            int itemCount = state.getItemCount();
            int childCount = parent.getChildCount();
            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            float lineHeight = textPaint.getTextSize() + fontMetrics.descent;

            long preGroupId, groupId = -1;
            for (int i = 0; i < childCount; i++) {
                View view = parent.getChildAt(i);
                int position = parent.getChildAdapterPosition(view);

                preGroupId = groupId;
                groupId = callback.getGroupId(position);
                if (groupId < 0 || groupId == preGroupId) continue;

                String textLine = callback.getGroupFirstLine(position).toUpperCase();
                if (TextUtils.isEmpty(textLine)) continue;

                int viewBottom = view.getBottom();
                float textY = Math.max(topGap, view.getTop());
                if (position + 1 < itemCount) { //下一个和当前不一样移动当前
                    long nextGroupId = callback.getGroupId(position + 1);
                    if (nextGroupId != groupId && viewBottom < textY) {//组内最后一个view进入了header
                        textY = viewBottom;
                    }
                }
                c.drawRect(left, textY - topGap, right, textY, paint);
                c.drawText(textLine, left, textY, textPaint);
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mDecorationType == DECORATION_LINEAR) {
            outRect.bottom = dividerHeight;
        } else if (mDecorationType == DECORATION_EXPANDABLE) {
            int pos = parent.getChildAdapterPosition(view);
            long groupId = callback.getGroupId(pos);
            if (groupId < 0) return;
            if (pos == 0 || isFirstInGroup(pos)) {
                outRect.top = topGap;
            } else {
                outRect.top = 0;
            }
        }
    }

    private boolean isFirstInGroup(int pos) {
        if (pos == 0) {
            return true;
        } else {
            long prevGroupId = callback.getGroupId(pos - 1);
            long groupId = callback.getGroupId(pos);
            return prevGroupId != groupId;
        }
    }

    public interface DecorationCallback {

        long getGroupId(int position);

        String getGroupFirstLine(int position);
    }

}
