package com.pipedog.mixkit.messenger.interfaces;

import android.os.Message;

import androidx.annotation.IntDef;

import com.pipedog.mixkit.messenger.interfaces.IMessageVerifier.VerifierType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 消息验证接口
 * @author liang
 * @time 2021/11/29
 */
public interface IMessageVerifier {

    @IntDef({
            VERIFIER_TYPE_SERVER,
            VERIFIER_TYPE_CLIENT,
            VERIFIER_TYPE_ALL,
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface VerifierType {}

    /** 用于服务端校验逻辑 */
    public static final int VERIFIER_TYPE_SERVER = 1 << 0;
    /** 用于客户端校验逻辑 */
    public static final int VERIFIER_TYPE_CLIENT = 1 << 1;
    /** 校验逻辑适用于双端 */
    public static final int VERIFIER_TYPE_ALL = VERIFIER_TYPE_SERVER | VERIFIER_TYPE_CLIENT;

    /**
     * 验证器类型（可以即为客户端类型又为服务端类型）
     * @return  VERIFIER_TYPE_SERVER 服务端验证，
     *          VERIFIER_TYPE_CLIENT 客户端验证，
     *          VERIFIER_TYPE_ALL 双端验证
     */
    @VerifierType
    int getVerifierType();

    /**
     * 校验消息的安全性等，来决定该消息是否应该被处理
     * @return true 继续处理该消息，false 忽略该消息（不予响应，不会进行回调等任何处理）
     */
    boolean isValidMessage(final Message message);

}
