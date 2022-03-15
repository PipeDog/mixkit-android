package com.pipedog.mixkit.example.module;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.example.R;
import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IBridgeModule;
import com.pipedog.mixkit.tool.MixLogger;
import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBar;
import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBarItem;
import com.pipedog.mixkit.websdk.utils.WebViewBridgeUtils;
import com.pipedog.mixkit.websdk.view.WebViewActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

@MixModule(name = "PageModule")
public class PageModule implements IBridgeModule {

    private WebViewActivity mActivity;

    @Override
    public void setBridge(IBridge bridge) {
        mActivity = WebViewBridgeUtils.getWebViewActivity(bridge);
    }

    @MixMethod(name = "testSetRightItems")
    public void testSetRightItems() {
        MixLogger.error("Call `testSetRightItems` success!");
        ITitleBar titleBar = mActivity.getTitleBar();

        List<ITitleBarItem> items = new ArrayList<>();

        items.add(new CustomItem("item1", "测试按钮1", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixLogger.error(">>>>> 点击了按钮 1");
            }
        }));

        items.add(new CustomItem("item2", "测试按钮2", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixLogger.error(">>>>> 点击了按钮 2");
            }
        }));

        items.add(new CustomItem("item3", null, mActivity.getResources().getDrawable(R.mipmap.ic_launcher_round), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixLogger.error(">>>>> 点击了按钮 3");
            }
        }));

        items.add(new CustomItem("item4", null, mActivity.getResources().getDrawable(R.mipmap.ic_launcher_round), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MixLogger.error(">>>>> 点击了按钮 4");
            }
        }));

        titleBar.setRightItems(items);
    }

    private class CustomItem implements ITitleBarItem {

        private String mItemId;
        private String mText;
        private Drawable mD;
        private OnClickListener mL;

        public CustomItem(String itemId,
                          String text,
                          Drawable d,
                          View.OnClickListener l) {
            mItemId = itemId;
            mText = text;
            mD = d;
            mL = l;
        }

        @Override
        public String getItemId() {
            return mItemId;
        }

        @Override
        public String getText() {
            return mText;
        }

        @Override
        public Drawable getDrawable() {
            return mD;
        }

        @Override
        public View.OnClickListener getListener() {
            return mL;
        }
    }

}
