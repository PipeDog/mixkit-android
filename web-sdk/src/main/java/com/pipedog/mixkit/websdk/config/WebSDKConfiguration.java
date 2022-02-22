package com.pipedog.mixkit.websdk.config;

/**
 * WebSDK 配置入口，如：cookie、user-agent 等
 * @author liang
 * @time 2021/12/26
 */
public class WebSDKConfiguration implements IWebSDKConfiguration {

    private IWebSDKConfiguration.IFetcher mFetcher;
    private IWebSDKConfiguration.ILoadURLAction mLoadURLAction;
    private IWebSDKConfiguration.IBridgeValidation mBridgeValidation;
    private int mBrowserKernelType;
    private IWebSDKConfiguration.IWebSettingsConfiguration mWebSettingsConfiguration;
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
        mBrowserKernelType = IWebSDKConfiguration.KERNEL_TYPE_WEBKIT;
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

}
