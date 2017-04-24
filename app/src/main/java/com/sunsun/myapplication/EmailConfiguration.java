package com.sunsun.myapplication;

import android.content.Context;
import android.text.TextUtils;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;

/**
 * Created by lidexian on 2017/4/6.
 */

public class EmailConfiguration {

    public static final String smtp = "smtp.qq.com";
    public static final String MailServerSSLPort = "465"; //端口
    public static final String MailServerPort = "25";//端口
    public static final String userid = "841654725@qq.com";//账号
    public static final String userpsd = "youjianfankui";//密码
    public static final String from = "841654725@qq.com";//发送账号
    public static final String to = "247755180@qq.com";//发送地址
    public static final String cc = "2643418514@qq.com";//抄送地址
    public static final String bcc =  "942514496@qq.com";//密送地址
    public static final String subject = "【Android摇一摇反馈】 xxx ";//标题
    public static final String format = "\\w+(\\.\\w)*@\\w+(\\.\\w{2,3}){1,3}";//邮箱格式

    /**
     * 配置MailcapCommandMap
     */
    public static void configurationCommandMap() {
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content- handler=com.sun.mail.handlers.message_rfc822");
    }

    /**
     * 验证邮箱的正则表达式,邮箱名合法，返回true, 邮箱名不合法，返回false
     *
     * @param email
     * @return
     */
    public static boolean checkEmail(String email) {
        if (email.matches(format)) {
            return true;
        } else {
            return false;
        }
    }


    public static String getEmailBody(Context context, String userAccount, String content) {
        StringBuffer sb = new StringBuffer();
        sb.append("xxx app");
        sb.append("\n");
        sb.append("VersionCode = ");
        sb.append(PlatformUtils.getVersionCode(context));

        sb.append("\n");
        sb.append("VersionName = ");
        sb.append(PlatformUtils.getVersionName(context));

        sb.append("\n");
        sb.append("isdebug = ");
        sb.append(PlatformUtils.isDebugVersion(context));

        sb.append("\n");
        sb.append("devicesid = ");
        sb.append(PlatformUtils.getDeviceId(context));

        sb.append("\n");
        sb.append("mac = ");
        sb.append(PlatformUtils.getMac(context));

        sb.append("\n");
        sb.append("sdkint = ");
        sb.append(PlatformUtils.getSDKInt());

        sb.append("\n");
        sb.append("systemversion = ");
        sb.append(PlatformUtils.getSystemVersion());

        sb.append("\n");
        sb.append("systemmodel = ");
        sb.append(PlatformUtils.getSystemModel());

        sb.append("\n");
        sb.append("devicebrand = ");
        sb.append(PlatformUtils.getDeviceBrand());

        sb.append("\n");
        sb.append("\n");
        sb.append("用户：");
        sb.append(userAccount);
        sb.append("\n");

        sb.append("反馈：");
        if (!TextUtils.isEmpty(content)) {
            sb.append(content);
        }

        return sb.toString();
    }

}
