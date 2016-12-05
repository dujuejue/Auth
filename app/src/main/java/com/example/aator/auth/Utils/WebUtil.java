package com.example.aator.auth.Utils;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.NtlmTransport;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * 　　　　　　　　┏┓　　　┏┓
 * 　　　　　　　┏┛┻━━━┛┻┓
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　━　　　┃
 * 　　　　　　 ████━████     ┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┃　　　┻　　　┃
 * 　　　　　　　┃　　　　　　　┃
 * 　　　　　　　┗━┓　　　┏━┛
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　　　┃
 * 　　　　　　　　　┃　 　 ┗━━━┓
 * 　　　　　　　　　┃ 神兽保佑　　 ┣┓
 * 　　　　　　　　　┃ 代码无BUG   ┏┛
 * 　　　　　　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　　　　　　┃┫┫　┃┫┫
 * 　　　　　　　　　　┗┻┛　┗┻┛
 * Created by dutingjue on 2016/11/16.
 */

public class WebUtil {
    //WebService命名空间
    public static final String SERVICE_NS = "http://localhost:80/BIendpoint";

    //WebService提供服务的URL
    public static final String SERVICE_URL = "http://192.168.30.103/BIendpoint";
    //调用方法
    public static final String LoadGetData = "LoadGetData";
    public static final String AuthorityManagementData = "AuthorityManagementData";
    public static final String BuildMenuTransaction_List = "BuildMenuTransaction_List";
    public static final String DeleteMenuTransaction_List = "DeleteMenuTransaction_List";
    public static final String FAILDATA = "FailData";
    //调用的soapaction
    static final String username = "vmm";
    static final String password = "Password1";

    public static SoapObject getWebData(SoapObject soapObject) {
        NtlmTransport ntlmTransport = new NtlmTransport(SERVICE_URL);
        ntlmTransport.setCredentials(username, password, "", "");
        ntlmTransport.debug = true;
        //使用SOAP1.2协议创建Envelop对象
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        //设置与.Net提供的Web Service保持较好的兼容性
        envelope.dotNet = true;
        envelope.implicitTypes = true;
        envelope.bodyOut = soapObject;
        try {
            ntlmTransport.call(null, envelope);
            SoapObject response = (SoapObject) envelope.bodyIn;
            return response;
        } catch (IOException e) {
            e.printStackTrace();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }


}
