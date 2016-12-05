package com.example.aator.auth.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aator.auth.R;
import com.example.aator.auth.Utils.WebUtil;
import com.example.aator.auth.bean.AuthItem;

import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

public class LoginActivity extends Activity implements View.OnClickListener {
    private EditText edit_user, edit_pass;
    private Button btn_login;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Intent intent = (Intent) msg.obj;
                    startActivity(intent);
                    finish();
                    break;
                case 2:
                    Toast.makeText(LoginActivity.this, "用户名/密码错误", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();
    }

    private void initView() {
        edit_user = (EditText) findViewById(R.id.editUser);
        edit_pass = (EditText) findViewById(R.id.editPassword);
        btn_login = (Button) findViewById(R.id.login_btn);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_btn:
                WebThread webThread = new WebThread();
                webThread.start();
                break;
        }
    }

    private class WebThread extends Thread {
        @Override
        public void run() {
            String name = edit_user.getText().toString();
            String pass = edit_pass.getText().toString();
            SoapObject soapObject = new SoapObject(WebUtil.SERVICE_NS, WebUtil.LoadGetData);
            soapObject.addProperty("userName", name);
            soapObject.addProperty("passWord", pass);
            soapObject.addProperty("FormSYS", "BI");
            SoapObject response = WebUtil.getWebData(soapObject);
            List<AuthItem> authItemList = parseData(response);
            Message message = mHandler.obtainMessage();
            if (authItemList != null) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.putExtra("menu", (Serializable) authItemList);
                intent.putExtra("user", name);
                message.obj = intent;
                message.what = 1;
            } else {
                message.what = 2;
            }
            mHandler.sendMessage(message);
        }
    }

    private List<AuthItem> parseData(SoapObject response) {
        List<AuthItem> authItemList = new ArrayList<AuthItem>();
        SoapObject detail1 = (SoapObject) response.getProperty(0);
        SoapObject detail2 = (SoapObject) detail1.getProperty(2);
        SoapObject detail3 = (SoapObject) detail2.getProperty(0);
        SoapObject detail4 = (SoapObject) detail3.getProperty(0);
        try {
            String menuName = detail4.getProperty("MenuName").toString();
            String menuID = detail4.getProperty("MenuID").toString();
            String menuParentID = detail4.getProperty("MenuParentID").toString();
            String[] menuNames = menuName.split(",");
            String[] menuIDs = menuID.split(",");
            String[] menuParentIDs = menuParentID.split(",");
            try {
                for (int i = 0; i < menuNames.length; i++) {
                    authItemList.add(new AuthItem(menuNames[i], menuIDs[i], menuParentIDs[i], true));
                }
                authItemList.add(new AuthItem("test", "6", "-1", true));
                return authItemList;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
