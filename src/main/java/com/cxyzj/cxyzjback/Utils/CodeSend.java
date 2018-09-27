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
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * 验证码发送类
 *
 * @author 夏
 */

public class CodeSend {

    /**
     * 手机验证码发送
     *
     * @param phone
     * @param code
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
     * @param email
     * @param code
     * @return sendSuccess||sendFailure
     */
    public boolean mailSend(String email, String code,String text) {
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
            msg.setText(text + code );
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
