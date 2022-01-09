package com.pipedog.mixkit.websdk.config;

/**
 * WebSDK 配置入口，如：cookie、user-agent 等
 * @author liang
 * @time 2021/12/26
 */
public class Configuration implements IConfiguration {

    private IFetcher mFetcher;
    private ILoadURLAction mLoadURLAction;
    private IBridgeValidation mBridgeValidation;
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

    }


    // METHODS FOR `IWebConfiguration`

    @Override
    public void setFetcher(IFetcher fetcher) {
        this.mFetcher = fetcher;
    }

    @Override
    public IFetcher getFetcher() {
        return mFetcher;
    }

    @Override
    public void setLoadURLAction(ILoadURLAction action) {
        this.mLoadURLAction = action;
    }

    @Override
    public ILoadURLAction getLoadURLAction() {
        return mLoadURLAction;
    }

    @Override
    public void setBridgeValidation(IBridgeValidation bridgeValidation) {
        this.mBridgeValidation = bridgeValidation;
    }

    @Override
    public IBridgeValidation getBridgeValidation() {
        return mBridgeValidation;
    }

}
