package com.pipedog.mixkit.websdk.config;

import android.content.Context;

import com.pipedog.mixkit.websdk.interfaces.widget.IErrorView;
import com.pipedog.mixkit.websdk.interfaces.widget.ILoadingView;
import com.pipedog.mixkit.websdk.interfaces.widget.ITitleBar;
import com.pipedog.mixkit.websdk.view.internal.widget.ErrorView;
import com.pipedog.mixkit.websdk.view.internal.widget.LoadingView;
import com.pipedog.mixkit.websdk.view.internal.widget.TitleBar;

/**
 * WebSDK 配置入口，如：cookie、user-agent 等
 * @author liang
 * @time 2021/12/26
 */
public class WebSDKConfiguration implements IWebSDKConfiguration {

    private IFetcher mFetcher;
    private ILoadURLAction mLoadURLAction;
    private IBridgeValidation mBridgeValidation;
    private int mBrowserKernelType;
    private IWebSettingsConfiguration mWebSettingsConfiguration;
    private IWidgetCreator mWidgetCreator;


    // CONSTRUCTORS

    private static volatile WebSDKConfiguration sGlobalConfiguration;

    public static IWebSDKConfiguration getInstance() {
        if (sGlobalConfiguration == null) {
            synchronized (WebSDKConfiguration.class) {
                if (sGlobalConfiguration == null) {
                    sGlobalConfiguration = new WebSDKConfiguration();
                }
            }
        }
        return sGlobalConfiguration;
    }

    /**
     * 之所以开放这个方法，是为了处理为某些模块定制化 web 能力的 case，慎用
     */
    public WebSDKConfiguration() {
        registerDefaultConfiguration();
    }


    // METHODS FOR `IWebConfiguration`

    @Override
    public void setFetcher(IWebSDKConfiguration.IFetcher fetcher) {
        this.mFetcher = fetcher;
    }

    @Override
    public IWebSDKConfiguration.IFetcher getFetcher() {
        return mFetcher;
    }

    @Override
    public void setLoadURLAction(IWebSDKConfiguration.ILoadURLAction action) {
        this.mLoadURLAction = action;
    }

    @Override
    public IWebSDKConfiguration.ILoadURLAction getLoadURLAction() {
        return mLoadURLAction;
    }

    @Override
    public void setBridgeValidation(IWebSDKConfiguration.IBridgeValidation bridgeValidation) {
        this.mBridgeValidation = bridgeValidation;
    }

    @Override
    public IWebSDKConfiguration.IBridgeValidation getBridgeValidation() {
        return mBridgeValidation;
    }

    @Override
    public void setBrowserKernelType(int kernelType) {
        mBrowserKernelType = kernelType;
    }

    @Override
    public int getBrowserKernelType() {
        return mBrowserKernelType;
    }

    @Override
    public void setWebSettingsConfiguration(IWebSettingsConfiguration conf) {
        mWebSettingsConfiguration = conf;
    }

    @Override
    public IWebSettingsConfiguration getWebSettingsConfiguration() {
        return mWebSettingsConfiguration;
    }

    @Override
    public void setWidgetCreator(IWidgetCreator creator) {
        mWidgetCreator = creator;
    }

    @Override
    public IWidgetCreator getWidgetCreator() {
        return mWidgetCreator;
    }


    // PRIVATE METHODS

    private void registerDefaultConfiguration() {
        mBrowserKernelType = IWebSDKConfiguration.KERNEL_TYPE_WEBKIT;

        setWidgetCreator(new IWidgetCreator() {
            @Override
            public IErrorView getErrorView(Context context) {
                return new ErrorView(context);
            }

            @Override
            public ILoadingView getLoadingView(Context context) {
                return new LoadingView(context);
            }

            @Override
            public ITitleBar getTitleBar(Context context) {
                return new TitleBar(context);
            }
        });
    }

}
