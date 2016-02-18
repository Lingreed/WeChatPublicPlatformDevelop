package com.mintcode.wechat;


import com.qq.weixin.mp.aes.AesException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LarryXu on 2015/12/18.
 * description 微信xml格式
 */
public class WXUtility {

    /**
     * 提取出click消息的xml数据包中的加密消息
     *
     * @param clickMsg 待提取的xml字符串
     * @return 提取出的加密消息字符串
     * @throws AesException
     */
    public static Map<String, Object> pickRequestInfo(String clickMsg) throws Exception {
//        Object[] result = new Object[5];
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(clickMsg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);


            /** 参数	描述
             *  ToUserName	企业号CorpID
             *  FromUserName	成员UserID
             *  CreateTime	消息创建时间 （整型）
             *  MsgType	消息类型，event
             *  Event	事件类型，CLICK
             *  EventKey	事件KEY值，与自定义菜单接口中KEY值对应
             *  AgentID	应用代理ID
             */
            Element root = document.getDocumentElement();
            // 消息类型【event消息、text消息、image消息、voice消息、video消息、小视频消息、location消息、】
            NodeList msgTypeNode = root.getElementsByTagName("MsgType");
            String msgType = msgTypeNode.item(0).getTextContent();
            result.put("msgType", msgType);

            NodeList fromNode = root.getElementsByTagName("ToUserName");
            result.put("toUserName", fromNode.item(0).getTextContent());
            NodeList corpIdNode = root.getElementsByTagName("FromUserName");
            result.put("fromUserName", corpIdNode.item(0).getTextContent());
            NodeList createTime = root.getElementsByTagName("CreateTime");
            result.put("createTime", createTime.item(0).getTextContent());
            NodeList agentID = root.getElementsByTagName("AgentID");
            result.put("agentID", agentID.item(0).getTextContent());
            switch (msgType) {
                case "event":
                    // 事件类型【】
                    NodeList EventNode = root.getElementsByTagName("Event");
                    String Event = EventNode.item(0).getTextContent();
                    result.put("event", Event);
                    switch (Event) {
                        case "CLICK":
                            NodeList eventKey = root.getElementsByTagName("EventKey");
                            result.put("eventKey", eventKey.item(0).getTextContent());
                            break;
                        case "VIEW":
                            NodeList eventKey2 = root.getElementsByTagName("EventKey");
                            result.put("eventKey", eventKey2.item(0).getTextContent());
                            break;
                    }
                    break;
                case "text":
                    NodeList content = root.getElementsByTagName("Content");
                    result.put("content", content.item(0).getTextContent());
                    NodeList msgId = root.getElementsByTagName("MsgId");
                    result.put("msgId", msgId.item(0).getTextContent());
                    break;
                case "image":
                    break;
                case "voice":
                    break;
                case "video":
                    break;
                case "shortvideo":
                    break;
                case "location":
                    break;
                default:
                    break;
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
    }

    /**
     * 生成text消息
     *
     * @param toUserName   成员UserID
     * @param fromUserName 企业号CorpID
     * @param createTime   消息创建时间 （整型）
     * @param content      文本消息内容
     * @return 生成的xml字符串
     */
    public static String clickXml(String toUserName, String fromUserName, Long createTime, String content) {
        String format = "<xml>\n" +
                "<ToUserName><![CDATA[%1$s]]></ToUserName>\n" +
                "<FromUserName><![CDATA[%2$s]]></FromUserName>\n" +
                "<CreateTime>%3$s</CreateTime>\n" +
                "<MsgType><![CDATA[text]]></MsgType>\n" +
                "<Content><![CDATA[%4$s]]></Content>\n" +
                "</xml>";
        return String.format(format, toUserName, fromUserName, createTime, content);
    }

    /**
     * 创建消息推送包
     *
     * @param content
     * @param toUser
     * @param toParty
     * @param toTag
     * @param agentId
     * @param safe
     * @return
     */
    public static Map<String, Object> createMsgMap(String content, String toUser, String toParty, String toTag, String agentId, String safe) {
        Map<String, Object> requsetInfo = new HashMap<>();
        Map<String, String> requsetContext = new HashMap<>();
        requsetContext.put("content", content);
        requsetInfo.put("touser", toUser);
        requsetInfo.put("toparty", toParty);
        requsetInfo.put("totag", toTag);
        requsetInfo.put("msgtype", "text");
        requsetInfo.put("agentid", agentId);
        requsetInfo.put("safe", safe);
        requsetInfo.put("text", requsetContext);
        return requsetInfo;
    }
}
