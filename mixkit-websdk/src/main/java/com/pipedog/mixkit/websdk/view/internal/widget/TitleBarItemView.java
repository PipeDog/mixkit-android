package com.pipedog.mixkit.websdk.view.internal.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBarItem;
import com.pipedog.mixkit.websdk.utils.DimensionUtils;

public class TitleBarItemView extends LinearLayout {

    private ITitleBarItem mTitleBarItem;
    private ImageView mImageView;
    private TextView mTextView;

    public TitleBarItemView(Context context, ITitleBarItem titleBarItem) {
        super(context);

        mTitleBarItem = titleBarItem;
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        setupViews();
    }

    private void setupViews() {
        View view = null;
        FrameLayout.LayoutParams lp = null;

        if (mTitleBarItem.getDrawable() != null) {
            mImageView = new ImageView(getContext());
            mImageView.setImageDrawable(mTitleBarItem.getDrawable());
            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            view = mImageView;

            lp = new FrameLayout.LayoutParams(
                    DimensionUtils.dp2px(getContext(), 40), DimensionUtils.dp2px(getContext(), 40)
            );
        } else if (mTitleBarItem.getText() != null) {
            mTextView = new TextView(getContext());
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setText(mTitleBarItem.getText());
            mTextView.setTextSize(16);
            view = mTextView;

            lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
            );
        } else {
            throw new RuntimeException("[TitleBarItemView] The view type could not be determined!");
        }

        view.setOnClickListener(mTitleBarItem.getListener());
        view.setLayoutParams(lp);
        addView(view);
    }

}
