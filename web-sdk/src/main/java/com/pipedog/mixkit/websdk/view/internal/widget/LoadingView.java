package com.pipedog.mixkit.websdk.view.internal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pipedog.mixkit.websdk.R;
import com.pipedog.mixkit.websdk.interfaces.widget.ILoadingView;

public class LoadingView extends FrameLayout implements ILoadingView {

    private ProgressBar mProgressBar;

    public LoadingView(@NonNull Context context) {
        this(context, null);
    }

    public LoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupViews();
    }


    // PRIVATE METHODS

    private void setupViews() {
        inflate(getContext(), R.layout.mix_layout_view_loading, this);
        mProgressBar = findViewById(R.id.mix_loading_progress_bar);
    }


    // OVERRIDE METHODS

    @Override
    public void startLoading() {
        // Do nothing
    }

    @Override
    public void stopLoading() {
        // Do nothing
    }

    @Override
    public void setProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }

    @Override
    public int getWidgetWidth() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public int getWidgetHeight() {
        return 4;
    }

    @Override
    public void setTheme(int theme) {

    }

}
