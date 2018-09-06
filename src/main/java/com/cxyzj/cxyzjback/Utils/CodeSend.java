package com.cxyzj.cxyzjback.Utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.sun.mail.util.MailSSLSocketFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.Random;


/**
 * 验证码发送类
 * @author 夏
 */

public class CodeSend {

    @Autowired
    private Response response;


    /**
     * 手机验证码发送
     * @param phone
     * @param code
     * @return sendSuccess||sendFailure
     */
    public String phoneSend(String phone, String code) throws Exception {
        response = new Response();
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

        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            System.out.println("验证码：---------------------------------------------"+code);
            return response.sendSuccess();
        }else {
            return response.sendFailure(Status.CODE_SEND_FAILURE,"验证码发送失败！");
        }


    }

    /**
     * 邮箱验证码发送  (qq邮箱会拦截)
     * @param email
     * @param code
     * @return sendSuccess||sendFailure
     */
    public String mail2Send(String email, String code){

        response = new Response();
        try{
            Properties prop = new Properties();
            prop.setProperty("mail.transport.protocol", "smtp");//协议
            prop.setProperty("mail.smtp.host", "smtp.sina.cn");//主机名
            prop.setProperty("mail.smtp.auth", "true");//是否开启权限控制
            prop.setProperty("mail.debug", "true");//如果设置为true,则在发送邮件时会打印发送时的信息
            //--获取从程序到邮件服务器的一次会话
            Session session = Session.getInstance(prop);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("kitor_summer@sina.com"));
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            msg.setSubject("来自CXYZJ的邮件");
            msg.setText("您的验证码是：" + code+"；");

            Transport trans = session.getTransport();
            trans.connect("kitor_summer@sina.com", "19980506xqt");
            trans.sendMessage(msg, msg.getAllRecipients());

            System.out.println("验证码是————————————————————————————————————————————————" + code);
            return new Response().sendSuccess();

        } catch (Exception e) {
            e.printStackTrace();
            return response.sendFailure(Status.CODE_SEND_FAILURE, "验证码发送失败");
        }

    }

    /**
     * 邮箱验证码发送  (支持qq邮箱)
     * @param email
     * @param code
     * @return sendSuccess||sendFailure
     */
    public String mailSend(String email, String code){

        response = new Response();
        try{
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
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("kitor_summer@qq.com"));
            msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            msg.setSubject("来自CXYZJ的邮件");
            msg.setText("您的验证码是：" + code+"；");

            Transport trans = session.getTransport();
            trans.connect("smtp.qq.com","kitor_summer@qq.com", "datcwecvzkiygfbi");
            trans.sendMessage(msg, msg.getAllRecipients());

            System.out.println("验证码是————————————————————————————————————————————————" + code);
            return new Response().sendSuccess();

        } catch (Exception e) {
            e.printStackTrace();
            return response.sendFailure(Status.CODE_SEND_FAILURE, "验证码发送失败");
        }

    }

}
