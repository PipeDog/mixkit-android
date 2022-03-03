package com.pipedog.mixkit.websdk.view.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pipedog.mixkit.websdk.R;
import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBar;
import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBarItem;
import com.pipedog.mixkit.websdk.interfaces.widget.IWidget;

import java.util.List;

public class TitleBar extends FrameLayout implements ITitleBar {

    private ImageView mGoBackImageView;
    private ImageView mCloseImageView;
    private TextView mTextView;

    public TitleBar(@NonNull Context context) {
        this(context, null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupViews();
    }


    // PRIVATE METHODS

    private void setupViews() {
        inflate(getContext(), R.layout.mix_view_title_bar, this);
        mGoBackImageView = findViewById(R.id.mix_tb_goback);
        mCloseImageView = findViewById(R.id.mix_tb_close);
        mTextView = findViewById(R.id.mix_tb_title);
    }


    // OVERRIDE METHODS

    @Override
    public void setTitle(String title) {
        mTextView.setText(title);
    }

    @Override
    public void setCloseButtonVisibility(boolean visibility) {
        mCloseImageView.setVisibility(visibility ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setRightItems(List<ITitleBarItem> items) {

    }

    @Override
    public void setGoBackButtonListener(OnClickListener listener) {
        mGoBackImageView.setOnClickListener(listener);
    }

    @Override
    public void setCloseButtonListener(OnClickListener listener) {
        mCloseImageView.setOnClickListener(listener);
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
        return 64;
    }

}
