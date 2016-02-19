package com.mintcode.wechat.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.mintcode.wechat.WXDispatcher;
import com.mintcode.wechat.WXUtility;
import com.mintcode.wechat.config.WeChatConfig;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by LarryXu on 2015/12/01.
 * description 微信消息推送
 */
@RestController
@ResponseBody
@RequestMapping(value = "/wxRoute", produces = "text/plain;charset=utf-8")
public class WXPushMessageController {
    //private static org.slf4j.Logger logger = LoggerFactory.getLogger(WXPushMessageController.class);

    private ObjectMapper objectMapper;

    @Autowired
    private WeChatConfig weChatConfig;

    @Autowired
    private WXDispatcher wxDispatcher;

    public WXPushMessageController() {
        objectMapper = new ObjectMapper();
        // 忽略未知属性
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

//    @InitBinder(value = "request")
//    protected void initAccountRequestBinder(WebDataBinder binder) {
//        binder.setValidator(new RequestValidator());
//    }

    /**
     * 验证回调地址
     *
     * @param msgSignature 微信加密签名
     * @param timeStamp    时间戳
     * @param nonce        随机数
     * @param echostr      加密的随机字符串
     */
//    @Action("wxRoute-validate")
//    @ActionLogger(value = "微信回调验证", type = LoggerType.WeChat)
    @RequestMapping(value = "/push", method = RequestMethod.GET)
    public String validate(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("msg_signature") String msgSignature,
                           @RequestParam("timestamp") String timeStamp, @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) throws Exception {
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(this.weChatConfig.getToken(), this.weChatConfig.getAesKey(), this.weChatConfig.getCorpid());
            return wxBizMsgCrypt.VerifyURL(msgSignature, timeStamp, nonce, echostr);
        } catch (AesException e) {
            //验证URL失败，错误原因请查看异常
//            this.logger.error(e.toString());
//            e.printStackTrace();
            return e.toString();
        }
    }

    /**
     * 微信端回调
     *
     * @param msgSignature 微信加密签名
     * @param timeStamp    时间戳
     * @param nonce        随机数
     * @param request      微信加密的请求数据
     */
//    @Action("wxRoute-push")
//    @ActionLogger(value = "微信回调请求", type = LoggerType.WeChat)
    @RequestMapping(value = "/push", method = RequestMethod.POST)
    public String postPush(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @RequestParam("msg_signature") String msgSignature,
                           @RequestParam("timestamp") String timeStamp, @RequestParam("nonce") String nonce, @RequestBody String request) throws Exception {
        String sEchoStr = "";
        try {
            WXBizMsgCrypt wxBizMsgCrypt = new WXBizMsgCrypt(this.weChatConfig.getToken(), this.weChatConfig.getAesKey(), this.weChatConfig.getCorpid());
            //解密
            String msg = wxBizMsgCrypt.DecryptMsg(msgSignature, timeStamp, nonce, request);
            Map<String, Object> result = WXUtility.pickRequestInfo(msg);

            //分发
            String response =  wxDispatcher.dispatcher(result);
            //返回加密后的用户xml消息
            return wxBizMsgCrypt.EncryptMsg(response, timeStamp, nonce);
        } catch (AesException e) {
            //验证URL失败，错误原因请查看异常
            //this.logger.error(e.toString(), e);
//            e.printStackTrace();
            sEchoStr = e.toString();
        }
        return sEchoStr;
    }
}
