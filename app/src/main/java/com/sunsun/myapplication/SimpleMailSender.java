package com.sunsun.myapplication;

import com.sunsun.nativelogger.NLogger;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * 简单邮件（不带附件的邮件）发送器
 */
public class SimpleMailSender {


    /**
     * 普通模式
     *
     * @param mailInfo
     * @return
     */
    public static boolean sendTextMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties(false);
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 普通模式带附件
     *
     * @param mailInfo
     * @return
     */
    public static boolean sendAtachmentTextMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties(true);
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            //创建 Mimemultipart添加内容(可包含多个附件)
            MimeMultipart multipart = new MimeMultipart();
            //MimeBodyPart(用于信件内容/附件)
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(mailInfo.getContent());
            //添加到MimeMultipart对象中
            multipart.addBodyPart(bodyPart);
            for (int i = 0; i < mailInfo.getAttachFileNames().size(); i++) {
                String fname = mailInfo.getAttachFileNames().get(i);
                File file = new File(fname);
                //创建FileDAtaSource(用于添加附件)
                if (file.exists()) {
                    FileDataSource fds = new FileDataSource(file);
                    BodyPart fileBodyPart = new MimeBodyPart();
                    // 字符流形式装入文件
                    fileBodyPart.setDataHandler(new DataHandler(fds));
                    // 设置附件文件名
                    fileBodyPart.setFileName(new File(fname).getName());
                    multipart.addBodyPart(fileBodyPart);
                }
            }
            mailMessage.setContent(multipart);
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 存储邮件信息
            mailMessage.saveChanges();
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            NLogger.e("SimpleMailSender", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            NLogger.e("SimpleMailSender", ex);
        }
        return false;
    }


    /**
     * ssl
     *
     * @param mailInfo
     * @return
     */
    public static boolean sendSSLTextMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties(true);
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            //创建 Mimemultipart添加内容(可包含多个附件)
            MimeMultipart multipart = new MimeMultipart();
            //MimeBodyPart(用于信件内容/附件)
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(mailInfo.getContent());
            //添加到MimeMultipart对象中
            multipart.addBodyPart(bodyPart);
            for (int i = 0; i < mailInfo.getAttachFileNames().size(); i++) {
                String fname = mailInfo.getAttachFileNames().get(i);
                File file = new File(fname);
                //创建FileDAtaSource(用于添加附件)
                if (file.exists()) {
                    FileDataSource fds = new FileDataSource(file);
                    BodyPart fileBodyPart = new MimeBodyPart();
                    // 字符流形式装入文件
                    fileBodyPart.setDataHandler(new DataHandler(fds));
                    // 设置附件文件名
                    fileBodyPart.setFileName(new File(fname).getName());
                    multipart.addBodyPart(fileBodyPart);
                }
            }
            mailMessage.setContent(multipart);
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 存储邮件信息
            mailMessage.saveChanges();
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            NLogger.e("SimpleMailSender", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            NLogger.e("SimpleMailSender", ex);
        }
        return false;
    }


    /**
     * ssl
     *
     * @param mailInfo
     * @return
     */
    public static boolean sendSSLCCAndBCCTextMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties(true);
        // 如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(),
                    mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro, authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO, to);
            // 创建邮件的抄送地址，并设置到邮件消息中
            Address cc = new InternetAddress(mailInfo.getToAddresscc());
            mailMessage.setRecipient(Message.RecipientType.CC, cc);
            // 创建邮件的密送地址，并设置到邮件消息中
            Address bcc = new InternetAddress(mailInfo.getToAddressbcc());
            mailMessage.setRecipient(Message.RecipientType.BCC, bcc);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            //创建 Mimemultipart添加内容(可包含多个附件)
            MimeMultipart multipart = new MimeMultipart();
            //MimeBodyPart(用于信件内容/附件)
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setText(mailInfo.getContent());
            //添加到MimeMultipart对象中
            multipart.addBodyPart(bodyPart);
            for (int i = 0; i < mailInfo.getAttachFileNames().size(); i++) {
                String fname = mailInfo.getAttachFileNames().get(i);
                File file = new File(fname);
                //创建FileDAtaSource(用于添加附件)
                if (file.exists()) {
                    FileDataSource fds = new FileDataSource(file);
                    BodyPart fileBodyPart = new MimeBodyPart();
                    // 字符流形式装入文件
                    fileBodyPart.setDataHandler(new DataHandler(fds));
                    // 设置附件文件名
                    fileBodyPart.setFileName(new File(fname).getName());
                    multipart.addBodyPart(fileBodyPart);
                }
            }
            mailMessage.setContent(multipart);
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 存储邮件信息
            mailMessage.saveChanges();
            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
            NLogger.e("SimpleMailSender", ex);
        } catch (Exception ex) {
            ex.printStackTrace();
            NLogger.e("SimpleMailSender", ex);
        }
        return false;
    }
}
