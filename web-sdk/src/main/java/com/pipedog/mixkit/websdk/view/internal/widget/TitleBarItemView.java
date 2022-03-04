package com.pipedog.mixkit.websdk.view.internal.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBarItem;
import com.pipedog.mixkit.websdk.utils.DimensionUtils;

public class TitleBarItemView extends FrameLayout {

    private ITitleBarItem mTitleBarItem;
    private ImageView mImageView;
    private TextView mTextView;

    public TitleBarItemView(Context context, ITitleBarItem titleBarItem) {
        super(context);
        mTitleBarItem = titleBarItem;
        setupViews();
    }

    private void setupViews() {
        View view = null;
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT
        );

        if (mTitleBarItem.getDrawable() != null) {
            mImageView = new ImageView(getContext());
            mImageView.setImageDrawable(mTitleBarItem.getDrawable());
            mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            view = mImageView;

            int margin = DimensionUtils.dp2px(getContext(), 10);
            lp.setMargins(-margin, margin, -margin, margin);
        } else if (mTitleBarItem.getText() != null) {
            mTextView = new TextView(getContext());
            mTextView.setGravity(Gravity.CENTER);
            mTextView.setText(mTitleBarItem.getText());
            mTextView.setTextSize(16);
            view = mTextView;

            int margin = DimensionUtils.dp2px(getContext(), 5);
            lp.setMargins(margin, 0, margin, 0);
        } else {
            throw new RuntimeException("[TitleBarItemView] The view type could not be determined!");
        }

        view.setOnClickListener(mTitleBarItem.getListener());
        view.setLayoutParams(lp);
        addView(view);
    }

}
