package com.pipedog.mixkit.example.module;

import android.graphics.drawable.Drawable;
import android.view.View;

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
        ITitleBar titleBar = mActivity.getTitleBar();

        List<ITitleBarItem> items = new ArrayList<>();

        items.add(new ITitleBarItem() {
            @Override
            public String getText() {
                return "测试按钮1";
            }

            @Override
            public Drawable getDrawable() {
                return null;
            }

            @Override
            public View.OnClickListener getListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MixLogger.error(">>>>> 点击了按钮 1");
                    }
                };
            }
        });

        items.add(new ITitleBarItem() {
            @Override
            public String getText() {
                return "测试按钮2";
            }

            @Override
            public Drawable getDrawable() {
                return null;
            }

            @Override
            public View.OnClickListener getListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MixLogger.error(">>>>> 点击了按钮 2");
                    }
                };
            }
        });


        items.add(new ITitleBarItem() {
            @Override
            public String getText() {
                return null;
            }

            @Override
            public Drawable getDrawable() {
                return mActivity.getResources().getDrawable(R.mipmap.ic_launcher_round);
            }

            @Override
            public View.OnClickListener getListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MixLogger.error(">>>>> 点击了按钮 3333");
                    }
                };
            }
        });

        items.add(new ITitleBarItem() {
            @Override
            public String getText() {
                return null;
            }

            @Override
            public Drawable getDrawable() {
                return mActivity.getResources().getDrawable(R.mipmap.ic_launcher_round);
            }

            @Override
            public View.OnClickListener getListener() {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        MixLogger.error(">>>>> 点击了按钮 4444");
                    }
                };
            }
        });


        titleBar.setRightItems(items);
    }

}
