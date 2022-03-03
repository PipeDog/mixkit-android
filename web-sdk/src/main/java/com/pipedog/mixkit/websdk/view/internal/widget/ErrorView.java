package com.pipedog.mixkit.websdk.view.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pipedog.mixkit.websdk.R;
import com.pipedog.mixkit.websdk.interfaces.widget.IErrorView;

import org.jetbrains.annotations.NotNull;

public class ErrorView extends FrameLayout implements IErrorView {

    private LinearLayout mContainerView;
    private ImageView mImageView;
    private TextView mTextView;

    public ErrorView(@NonNull Context context) {
        this(context, null);
    }

    public ErrorView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupViews();
    }


    // PRIVATE METHODS

    private void setupViews() {
        inflate(getContext(), R.layout.mix_view_error, this);
        mContainerView = findViewById(R.id.mix_err_container);
        mImageView = findViewById(R.id.mix_err_img);
        mTextView = findViewById(R.id.mix_err_tv);
    }


    // OVERRIDE METHODS

    @Override
    public void setImage(Drawable drawable) {
        mImageView.setImageDrawable(drawable);
    }

    @Override
    public void setText(String text) {
        mTextView.setText(text);
    }

    @Override
    public void setRetryButtonListener(OnClickListener listener) {
        mContainerView.setOnClickListener(listener);
    }

    @Override
    public void setVisibility(boolean visibility) {
        super.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setTheme(int theme) {

    }

    @Override
    public int getWidgetWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getWidgetHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

}
