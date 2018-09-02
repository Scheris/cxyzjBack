package com.cxyzj.cxyzjback;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import org.junit.Test;

public class MsgDemo {

    /**
     * 发送短信消息方法,返回验证码
     * @param phone
     * @return
     */
    public static String sendMsg(String phone) throws Exception {

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
        request.setTemplateParam("{\"code\":\"" + "123456" + "\"}");
        request.setOutId("yourOutId");

        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);

        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            System.out.println(sendSmsResponse.getCode());
        }
        return null;
    }
    //测试发送短信
    @Test
    public void test1() throws Exception {
        String s = this.sendMsg("13814375214");//手机号
        System.out.println(s);
    }


}
