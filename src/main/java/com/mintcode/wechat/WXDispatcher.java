package com.mintcode.wechat;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BryanLin on 2016/2/18.
 */
@Component
public class WXDispatcher {
    public String dispatcher(Map msgMap){
        String msgType = msgMap.get("MsgType").toString();
        String result = "";
        switch(msgType){
            case "event":
                String event = msgMap.get("Event").toString();
                switch(event){
                    case "CLICK":
                        result = clickEvent(msgMap);
                        break;
                    case "VIEW":
                        break;
                    default:
                        break;
                }
                break;
            case "text":
                result = textReply(msgMap);
                break;
            default:
                break;
        }
        return result;
    }

    //点击菜单拉取消息的事件
    public String clickEvent(Map msgMap){
        String toUserName = msgMap.get("toUserName").toString();
        String fromUserName = msgMap.get("fromUserName").toString();
        String eventKey = msgMap.get("EventKey").toString();
        Long createTime = System.currentTimeMillis();
        String content = "";
        // 返回数据
        switch (eventKey){
            case "key1":
                content = "制霸铃兰！";
                break;
            case "key2":
                content = "打倒大魔王";
                break;
            case "key3":
                content = "暴力膜蛤";
                break;
            case "key4":
                content = "冷如冰，寒如雪，剑客西门吹雪";
                break;
            default:
                break;
        }

        return WXUtility.clickXml(toUserName, fromUserName, createTime, content);
    }

    //收到文本消息后的回复
    public String textReply(Map msgMap){
        String toUserName = msgMap.get("toUserName").toString();
        String fromUserName = msgMap.get("fromUserName").toString();
        String msgContent = msgMap.get("Content").toString();
        Long createTime = System.currentTimeMillis();
        String content = "别以为我听不清，你丫BB的是这个：" + msgContent;

        return WXUtility.clickXml(toUserName, fromUserName, createTime, content);
    }
}
