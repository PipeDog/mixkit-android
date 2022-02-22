package com.pipedog.mixkit.websdk.utils;

import com.pipedog.mixkit.websdk.config.IWebSDKConfiguration;
import com.pipedog.mixkit.websdk.config.UserAgentNode;

import java.util.List;

/**
 * UA 格式化工具
 * @author liang
 * @time 2022/02/11
 */
public class UserAgentFormat {

    /**
     * 获取格式化后的 UserAgent
     */
    public static String getUserAgent(String defaultUserAgent) {
        if (defaultUserAgent == null) {
            defaultUserAgent = "";
        }

        IWebSDKConfiguration.IFetcher fetcher = Configuration.getInstance().getFetcher();
        if (fetcher == null) {
            return defaultUserAgent;
        }

        List<UserAgentNode> userAgentList = fetcher.getUserAgentList();
        if (userAgentList == null || userAgentList.isEmpty()) {
            return defaultUserAgent;
        }

        if (defaultUserAgent.length() > 0 && !defaultUserAgent.endsWith(" ")) {
            defaultUserAgent = defaultUserAgent + " ";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(defaultUserAgent);

        for (UserAgentNode node : userAgentList) {
            String name = node.getName();
            if (name == null) { name = ""; }

            String value = node.getValue();
            if (value == null) { value = ""; }

            String userAgentString = String.format("%s/%s ", name, value);
            builder.append(userAgentString);
        }

        return builder.toString();
    }

}
