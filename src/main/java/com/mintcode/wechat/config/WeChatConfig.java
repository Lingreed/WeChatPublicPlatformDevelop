package com.mintcode.wechat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by LarryXu on 2015/10/27.
 * description
 */
@Component
public class WeChatConfig {
    // 企业Id
    @Value("${wechat.corpid}")
    private String corpid;
    // 管理组的凭证密钥
    @Value("${wechat.corpsecrt}")
    private String corpsecrt;
    // 回调签名
    @Value("${wechat.token}")
    private String token;
    // AES密钥的Base64编码
    @Value("${wechat.aesKey}")
    private String aesKey;

    // 推送消息内容
    @Value("${wechat.content}")
    private String content;
    // 消息接收者、成员ID列表
    @Value("${wechat.toUser}")
    private String toUser;
    // 消息接收者
    @Value("${wechat.toParty}")
    private String toParty;
    // 企业应用id
    @Value("${wechat.agentid}")
    private String agentid;

    public String getCorpid() {
        return corpid;
    }

    public String getCorpsecrt() {
        return corpsecrt;
    }

    public String getToken() {
        return token;
    }

    public String getAesKey() {
        return aesKey;
    }

    public String getContent() {
        return content;
    }

    public String getToUser() {
        return toUser;
    }

    public String getToParty() {
        return toParty;
    }

    public String getAgentid() {
        return agentid;
    }

//    @PostConstruct
//    public void initializer() {
//        corpid = _corpid;
//        corpsecrt = _corpsecrt;
//        content = _content;
//        toUser = _toUser;
//        toParty = _toParty;
//        agentid = _agentid;
//    }
}
