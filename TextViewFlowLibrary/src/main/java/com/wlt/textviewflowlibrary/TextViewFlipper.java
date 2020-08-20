package com.wlt.textviewflowlibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.wlt.listener.TextViewFlipperClickListener;
import com.wlt.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

public class TextViewFlipper extends ViewFlipper {


    private boolean isSingleLine = false;
    /**
     * 文字是否为单行,默认false
     */
    private int mTextColor = 0xff000000;
    /**
     * 设置文字颜色,默认黑色
     */
    private int mTextSize = 16;
    /**
     * 设置文字尺寸,默认16px
     */
    private int mGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    /**
     * 文字显示位置,默认左边居中
     */
    private static final int GRAVITY_LEFT = 0;
    private static final int GRAVITY_CENTER = 1;
    private static final int GRAVITY_RIGHT = 2;
    private int animDuration = 1500;
    /**
     * 默认1.5s
     */
    private int mFlags = -1;
    /**
     * 文字划线
     */
    private static final int STRIKE = 0;
    private static final int UNDER_LINE = 1;
    private int mTypeface = Typeface.NORMAL;
    /**
     * 设置字体类型：加粗、斜体、斜体加粗
     */
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_BOLD = 1;
    private static final int TYPE_ITALIC = 2;
    private static final int TYPE_ITALIC_BOLD = 3;

    private List<String> mDatas = new ArrayList<>();
    private TextViewFlipperClickListener mListener;

    public TextViewFlipper(Context context) {
        super(context, null);
    }

    public TextViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewFlipperStyle, 0, 0);

        mTextColor = typedArray.getColor(R.styleable.TextViewFlipperStyle_setTextColor, mTextColor);//设置文字颜色
        if (typedArray.hasValue(R.styleable.TextViewFlipperStyle_setTextSize)) {//设置文字尺寸
            mTextSize = (int) typedArray.getDimension(R.styleable.TextViewFlipperStyle_setTextSize, mTextSize);
            mTextSize = DisplayUtils.px2sp(context, mTextSize);
        }
        int gravityType = typedArray.getInt(R.styleable.TextViewFlipperStyle_set_Gravity, GRAVITY_LEFT);//显示位置
        switch (gravityType) {
            case GRAVITY_LEFT:
                mGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
                break;
            case GRAVITY_CENTER:
                mGravity = Gravity.CENTER;
                break;
            case GRAVITY_RIGHT:
                mGravity = Gravity.RIGHT | Gravity.CENTER_VERTICAL;
                break;
        }
        mFlags = typedArray.getInt(R.styleable.TextViewFlipperStyle_set_Flags, mFlags);//字体划线
        switch (mFlags) {
            case STRIKE:
                mFlags = Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG;
                break;
            case UNDER_LINE:
                mFlags = Paint.UNDERLINE_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG;
                break;
            default:
                mFlags = 0 | Paint.ANTI_ALIAS_FLAG;
                break;
        }
        mTypeface = typedArray.getInt(R.styleable.TextViewFlipperStyle_set_Typeface, mTypeface);//字体样式
        switch (mTypeface) {
            case TYPE_BOLD:
                mTypeface = Typeface.BOLD;
                break;
            case TYPE_ITALIC:
                mTypeface = Typeface.ITALIC;
                break;
            case TYPE_ITALIC_BOLD:
                mTypeface = Typeface.ITALIC | Typeface.BOLD;
                break;
            case TYPE_NORMAL:
            default:
                mTypeface = Typeface.NORMAL;
                break;
        }
        setDatas(mDatas);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getDisplayedChild();//当前显示的子视图的索引位置
                if (mListener != null) {
                    mListener.onItemClick(mDatas.get(position), position);
                }
            }
        });
    }
    /**
     * 设置数据集合
     */
    public void setDatas(List<String> mDatas) {
        setDatas(mDatas, null);
    }

    public void setDatas(List<String> datas, final TextViewFlipperClickListener textViewFlipperClickListener) {
        setDatasWithDrawableIconLeft(datas, null, textViewFlipperClickListener);
    }

    public void setDatasWithDrawableIconLeft(List<String> datas, Drawable drawable, final TextViewFlipperClickListener textViewFlipperClickListener) {
        setDatasWithDrawableIcon(datas, drawable, -1, Gravity.LEFT, textViewFlipperClickListener);
    }

    public void setDatasWithDrawableIconLeft(List<String> datas, Drawable drawable, int size, final TextViewFlipperClickListener textViewFlipperClickListener) {
        setDatasWithDrawableIcon(datas, drawable, size, Gravity.LEFT, textViewFlipperClickListener);
    }

    public void setDatasWithDrawableIcon(List<String> datas, Drawable drawable, int size, int direction, final TextViewFlipperClickListener textViewFlipperClickListener) {
        this.mDatas = datas;
        this.mListener = textViewFlipperClickListener;
        if (DisplayUtils.isEmpty(mDatas)) {
            return;
        }
        removeAllViews();
        for (int i = 0; i < mDatas.size(); i++) {
            TextView textView = new TextView(getContext());
            setTextView(textView, i);
            if (drawable != null) {//有图片
                textView.setCompoundDrawablePadding(8);
                if (size > 0) {//不设置图片宽高 本身宽高
                    float scale = getResources().getDisplayMetrics().density;// 屏幕密度 ;
                    int muchDp = (int) (size * scale + 0.5f);
                    drawable.setBounds(0, 0, muchDp, muchDp);
                }
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

                if (direction == Gravity.LEFT) {
                    textView.setCompoundDrawables(drawable, null, null, null);//左边
                } else if (direction == Gravity.TOP) {
                    textView.setCompoundDrawables(null, drawable, null, null);//顶部
                } else if (direction == Gravity.RIGHT) {
                    textView.setCompoundDrawables(null, null, drawable, null);//右边
                } else if (direction == Gravity.BOTTOM) {
                    textView.setCompoundDrawables(null, null, null, drawable);//底部
                }
            }
            addView(textView, i);//添加子view,并标识子view位置
        }
    }

    /**
     * 设置TextView
     */
    private void setTextView(TextView textView, int position) {
        textView.setText(mDatas.get(position));
        //任意设置你的文字样式，在这里
        textView.setSingleLine(isSingleLine);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(mTextColor);
        textView.setTextSize(mTextSize);
        textView.setGravity(mGravity);
        textView.getPaint().setFlags(mFlags);//字体划线
        textView.setTypeface(null, mTypeface);//字体样式
    }



}
