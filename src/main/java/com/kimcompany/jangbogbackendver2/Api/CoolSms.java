package com.kimcompany.jangbogbackendver2.Api;

import com.kimcompany.jangbogbackendver2.Text.PropertiesText;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import java.util.HashMap;

import static com.kimcompany.jangbogbackendver2.Text.PropertiesText.*;

public class CoolSms {
    public static  void sendMessage(String phoneNum,String messege) throws CoolsmsException {
        Message coolsms = new Message(coolSmsKey, coolSmsSecret);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNum);
        params.put("from", companyNum);
        params.put("type", "SMS");
        params.put("text", messege);
        coolsms.send(params);
    }
}
