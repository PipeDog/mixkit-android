package com.pipedog.mixkit.websdk.view.internal.widget;

import android.content.Context;
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
import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBar;
import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBarItem;
import com.pipedog.mixkit.websdk.utils.DimensionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TitleBar extends FrameLayout implements ITitleBar {

    private ImageView mGoBackImageView;
    private ImageView mCloseImageView;
    private TextView mTextView;
    private LinearLayout mRightItemContainer;
    private List<ITitleBarItem> mRightItems = new ArrayList<>();

    public TitleBar(@NonNull Context context) {
        this(context, null);
    }

    public TitleBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setupViews();
    }


    // PRIVATE METHODS

    private void setupViews() {
        inflate(getContext(), R.layout.mix_layout_view_title_bar, this);
        mGoBackImageView = findViewById(R.id.mix_tb_goback);
        mCloseImageView = findViewById(R.id.mix_tb_close);
        mTextView = findViewById(R.id.mix_tb_title);
        mRightItemContainer = findViewById(R.id.mix_tb_right_container);
    }


    // OVERRIDE METHODS

    @Override
    public void setTitle(String title) {
        mTextView.setText(title);
    }

    @Override
    public void setCloseButtonVisibility(int visibility) {
        mCloseImageView.setVisibility(visibility);
    }

    @Override
    public void setRightItems(List<ITitleBarItem> items) {
        if (items == null) {
            mRightItems = new ArrayList<>();
        }

        mRightItems = items;
        verifyRightItems();
        removeAllRightButtons();
        addRightButtons();
    }

    @Override
    public List<ITitleBarItem> getRightItems() {
        return mRightItems;
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


    // PRIVATE METHODS

    private void verifyRightItems() {
        Set<String> visitedSet = new HashSet<>();
        List<ITitleBarItem> rightItems = new ArrayList<>(mRightItems);

        for (ITitleBarItem item : rightItems) {
            String itemId = item.getItemId();
            if (itemId == null) {
                throw new RuntimeException(
                        "The method `getItemId()` from interface `ITitleBarItem` can not be null!");
            }

            if (visitedSet.contains(itemId)) {
                throw new RuntimeException(
                        "Duplicate itemId: \"" + itemId + "\", the itemId must be unique!");
            }

            visitedSet.add(itemId);
        }
    }

    private void addRightButtons() {
        List<ITitleBarItem> rightItems = new ArrayList<>(mRightItems);
        for (ITitleBarItem item : rightItems) {
            // Real item
            TitleBarItemView itemView = new TitleBarItemView(getContext(), item);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT
            ));
            mRightItemContainer.addView(itemView);

            // Item space
            View placeholderView = new View(getContext());
            placeholderView.setLayoutParams(new LinearLayout.LayoutParams(
                    DimensionUtils.dp2px(getContext(), 10), LinearLayout.LayoutParams.MATCH_PARENT
            ));
            mRightItemContainer.addView(placeholderView);
        }
    }

    private void removeAllRightButtons() {
        mRightItemContainer.removeAllViews();
    }

}
