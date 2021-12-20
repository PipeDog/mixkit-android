package com.pipedog.mixkit.parser;

import java.util.List;

/**
 * 消息解析器抽象接口（负责对消息原始数据的解析，并最终输出统一结构）
 * @author liang
 */
public interface IMixMessageParser {

    /**
     * 消息体数据抽象
     */
    interface IMixMessageBody {

        /**
         * 自定义 module 名（匹配注解导出的自定义名称）
         */
        public String getModuleName();

        /**
         * 自定义方法名（匹配注解导出的自定义方法名）
         */
        public String getMethodName();

        /**
         * 参数列表
         * 参数类型除 MixResultCallback 回调外，仅支持 List、Map、String 以及基本数据类型及其包装类
         */
        public List<Object> getArguments();

    }

    /**
     * 是否能够解析 metaData
     * @param metaData 原始消息数据
     * @return true 可以解析，false 不能解析
     */
    public static boolean canParse(Object metaData) {
        return false;
    }

    /**
     * 创建新的消息解析器
     * @param metaData 原始消息数据
     * @return 能够解析该数据的消息解析器，可能为 null，即匹配解析器失败
     */
    public static IMixMessageParser newParser(Object metaData) {
        return null;
    }

    /**
     * 获取解析后的消息体数据
     * @return 解析后的统一抽象数据
     */
    public IMixMessageBody messageBody();

}
