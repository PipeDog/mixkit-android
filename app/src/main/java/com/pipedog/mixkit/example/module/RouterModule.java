package com.pipedog.mixkit.example.module;

import android.content.Context;
import android.content.Intent;

import com.pipedog.mixkit.annotation.MixMethod;
import com.pipedog.mixkit.annotation.MixModule;
import com.pipedog.mixkit.kernel.IBridge;
import com.pipedog.mixkit.kernel.IBridgeModule;
import com.pipedog.mixkit.web.utils.ThreadUtils;
import com.pipedog.mixkit.websdk.constants.RouteDef;
import com.pipedog.mixkit.websdk.utils.WebViewBridgeUtils;
import com.pipedog.mixkit.websdk.view.WebViewActivity;

@MixModule(name = "RouterModule")
public class RouterModule implements IBridgeModule {

    private Context mContext;

    @Override
    public void setBridge(IBridge bridge) {
        mContext = WebViewBridgeUtils.getContext(bridge);
    }

    @MixMethod(name = "loadUrl")
    public void loadUrl(String url) {
        ThreadUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(RouteDef.KEY_URL, url);
                mContext.startActivity(intent);
            }
        });
    }

}
