package com.pipedog.mixkit.websdk.config;

/**
 * WebSDK 配置入口，如：cookie、user-agent 等
 * @author liang
 * @time 2021/12/26
 */
public class Configuration implements IConfiguration {

    private IConfiguration.IFetcher mFetcher;
    private IConfiguration.ILoadURLAction mLoadURLAction;
    private IConfiguration.IBridgeValidation mBridgeValidation;
    private int mBrowserKernelType;
    private IConfiguration.IWebSettingsConfiguration mWebSettingsConfiguration;
    private static volatile Configuration sGlobalConfiguration;

    public static IConfiguration getInstance() {
        if (sGlobalConfiguration == null) {
            synchronized (Configuration.class) {
                if (sGlobalConfiguration == null) {
                    sGlobalConfiguration = new Configuration();
                }
            }
        }
        return sGlobalConfiguration;
    }

    /**
     * 之所以开放这个方法，是为了处理为某些模块定制化 web 能力的 case，慎用
     */
    public Configuration() {
        mBrowserKernelType = IConfiguration.KERNEL_TYPE_WEBKIT;
    }


    // METHODS FOR `IWebConfiguration`

    @Override
    public void setFetcher(IConfiguration.IFetcher fetcher) {
        this.mFetcher = fetcher;
    }

    @Override
    public IConfiguration.IFetcher getFetcher() {
        return mFetcher;
    }

    @Override
    public void setLoadURLAction(IConfiguration.ILoadURLAction action) {
        this.mLoadURLAction = action;
    }

    @Override
    public IConfiguration.ILoadURLAction getLoadURLAction() {
        return mLoadURLAction;
    }

    @Override
    public void setBridgeValidation(IConfiguration.IBridgeValidation bridgeValidation) {
        this.mBridgeValidation = bridgeValidation;
    }

    @Override
    public IConfiguration.IBridgeValidation getBridgeValidation() {
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
