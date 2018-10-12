package com.cxyzj.cxyzjback.Utils;


import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Package com.cxyzj.cxyzjback.Utils
 * @Author Yaser
 * @Date 2018/09/19 11:27
 * @Description: 工具类
 */

@Component
public class Utils {

    /**
     * @param str     传入的手机或邮箱
     * @param isPhone 是否是手机
     * @return 返回对邮箱或手机进行掩码操作后的邮箱或手机
     */
    public String maskEmailPhone(String str, boolean isPhone) {
        StringBuilder sb = new StringBuilder(str);
        int end;
        int start = 3;
        if (isPhone) {
            end = str.length() - 2;
        } else {
            end = str.indexOf('@');
        }
        if (end < start) {
            start = end;
        }
        StringBuilder maskCode = new StringBuilder();
        for (int i = start; i < end; i++) {
            maskCode.append("*");
        }
        sb.replace(start, end, maskCode.toString());
        return sb.toString();
    }


    /**
     * 随机字符组成的邮箱验证码
     */
    public String mailCode() {
        char[] ch = "abcdefghijkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXY3456789".toCharArray();

        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            index = r.nextInt(len);
            sb.append(ch[index]);
        }
        return sb.toString();
    }

    /**
     * 随机六位数的手机验证码
     */
    public String phoneCode() {
        char[] ch = "0123456789".toCharArray();

        Random r = new Random();
        int len = ch.length;
        int index;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            index = r.nextInt(len);
            sb.append(ch[index]);
        }
        return sb.toString();  //每次调用生成一次六位数的随机数
    }

    /**
     * 手机验证码发送
     *
     * @param phone 手机号
     * @param code  验证码
     * @return sendSuccess||sendFailure
     */
    public boolean phoneSend(String phone, String code) throws ClientException {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        final String product = "Dysmsapi";
        final String domain = "dysmsapi.aliyuncs.com";

        final String accessKeyId = "LTAItPR3gWB0a0XI";
        final String accessKeySecret = "3n3jqbwzIQZRXyjpKtQWLQQ53xRNRi";
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);

        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        request.setPhoneNumbers(phone);
        request.setSignName("夏庆涛");
        request.setTemplateCode("SMS_142090416");
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        request.setOutId("yourOutId");

        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            System.out.println("验证码：---------------------------------------------" + code);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 邮箱验证码发送  (支持qq邮箱)
     *
     * @param email 邮箱
     * @param code  验证码
     * @return sendSuccess||sendFailure
     */
    public boolean mailSend(String email, String code, String text) {
        try {
            Properties prop = new Properties();
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.host", "smtp.sina.cn");
            prop.setProperty("mail.smtp.auth", "true");
            prop.setProperty("mail.debug", "true");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getInstance(prop);
            MimeMessage msg = new MimeMessage(session);
            String from = "cxyzj203@qq.com";
            String nick = "";
            try {
                nick = javax.mail.internet.MimeUtility.encodeText("CXYZJ");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            msg.setFrom(new InternetAddress(nick + " <" + from + ">"));
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            msg.setSubject("来自程序员之家的邮件");
            msg.setText(text + code);
            Transport trans = session.getTransport();
            trans.connect("smtp.qq.com", from, "ylxcfkgmbwrxdajf");
            trans.sendMessage(msg, msg.getAllRecipients());

            System.out.println("验证码是————————————————————————————————————————————————" + code);
            return true;

        } catch (Exception e) {

            e.printStackTrace();
            return false;
        }
    }
}
