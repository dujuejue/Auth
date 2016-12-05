package com.example.aator.auth.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.aator.auth.Utils.WebUtil;
import com.example.aator.auth.bean.AuthChild;
import com.example.aator.auth.bean.AuthGroup;
import com.example.aator.auth.R;
import com.example.aator.auth.adapter.MyExpandableListAdapter;
import com.example.aator.auth.bean.AuthItem;
import com.example.aator.auth.bean.RoleUser;
import com.example.aator.auth.view.CustomDialog;

import org.ksoap2.serialization.SoapObject;

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
 * Created by dutingjue on 2016/11/9.
 */
public class AuthFragment extends BaseFragment implements View.OnClickListener {
    private Spinner role, user;
    private View view;
    private List<String> roleList = new ArrayList<String>(), userList = new ArrayList<String>();
    private ExpandableListView EListView;
    private List<AuthGroup> groupTitle = new ArrayList<AuthGroup>();
    private MyExpandableListAdapter expandableListAdapter;
    private ImageButton roleBtn, userBtn;
    private List<AuthItem> authItems = new ArrayList<AuthItem>();
    private List<AuthItem> childAuthItems = new ArrayList<AuthItem>();
    private List<RoleUser> roleUsers = new ArrayList<RoleUser>();
    private static final int AUTHLIST = 1, ROLE = 0, DATAPRAPER = 2, AUTHLISTSECOND = 3;
    private ArrayAdapter<String> roleAdapter;
    private ArrayAdapter<String> userAdapter;
    private UpDataThread dataThread;
    private WebThread webThread;
    private Handler mDataHandler;
    private Handler mWebHandler;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AUTHLIST:
                    setExpandableListView(view);
                    authItems = (List<AuthItem>) msg.obj;
                    Message message = mDataHandler.obtainMessage();
                    message.what = 0;
                    message.obj = authItems;
                    mDataHandler.sendMessage(message);
                    break;
                case AUTHLISTSECOND:
                    List<AuthGroup> groups = (List<AuthGroup>) msg.obj;
                    groupTitle.clear();
                    for (AuthGroup authGroup : groups) {
                        groupTitle.add(authGroup);
                    }
                    expandableListAdapter.notifyDataSetChanged();
                    break;
                case ROLE:
                    roleUsers = (List<RoleUser>) msg.obj;
                    roleList.clear();
                    for (RoleUser roleUser : roleUsers) {
                        roleList.add(roleUser.getRole());
                    }
                    roleAdapter.notifyDataSetChanged();
                    int i = role.getSelectedItemPosition();
                    if (i != -1) {
                        userList.clear();
                        for (String userName : roleUsers.get(i).getUser()) {
                            userList.add(userName);
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                    break;
                case DATAPRAPER:
                    break;
            }
        }
    };


    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.auth, container, false);
        webThread = new WebThread();
        webThread.start();
        dataThread = new UpDataThread();
        dataThread.start();
        setSpinner(view);
        setImageBtn(view);
        return view;
    }

    private void setImageBtn(View view) {
        roleBtn = (ImageButton) view.findViewById(R.id.add_role);
        userBtn = (ImageButton) view.findViewById(R.id.add_user);
        roleBtn.setOnClickListener(this);
        userBtn.setOnClickListener(this);
    }

    private void setSpinner(View view) {
        role = (Spinner) view.findViewById(R.id.role);
        user = (Spinner) view.findViewById(R.id.user);
        for (RoleUser roleUser : roleUsers) {
            roleList.add(roleUser.getRole());
        }
        roleAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, roleList);
        userAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, userList);
        roleAdapter.setDropDownViewResource(R.layout.spinner_window);
        userAdapter.setDropDownViewResource(R.layout.spinner_window);
        role.setAdapter(roleAdapter);
        user.setAdapter(userAdapter);

        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userList.clear();
                for (String userName : roleUsers.get(i).getUser()) {
                    userList.add(userName);
                }
                userAdapter.notifyDataSetChanged();
                //处理expandlistview
                List<AuthItem> items = new ArrayList<AuthItem>();
                items = roleUsers.get(i).getAuthItems();
                Message message = mDataHandler.obtainMessage();
                message.what = 1;
                message.obj = items;
                mDataHandler.sendMessage(message);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void setExpandableListView(View view) {
        expandableListAdapter = new MyExpandableListAdapter(getActivity(), groupTitle, webThread, mWebHandler);
        EListView = (ExpandableListView) view.findViewById(R.id.expandableListView);
        EListView.setGroupIndicator(null);
        EListView.setDivider(null);
        EListView.setAdapter(expandableListAdapter);
        EListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                ImageView indicator = (ImageView) view.findViewById(R.id.group_indicator);
                if (expandableListView.isGroupExpanded(i)) {
                    indicator.setImageDrawable(getResources().getDrawable(R.drawable.dropdown));
                } else {
                    indicator.setImageDrawable(getResources().getDrawable(R.drawable.dropup));
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_role:
                setDialog("添加角色", 0);
                break;
            case R.id.add_user:
                setDialog("添加用户", 1);
                break;

        }
    }

    private void setDialog(String title, final int type) {
        final String negative = getActivity().getResources().getString(R.string.cancel);
        final View customView = View.inflate(getActivity(), R.layout.role_user, null);
        CustomDialog.Builder dialog = new CustomDialog.Builder(getActivity());
        dialog.setTitle(title)
                .setContentView(customView)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText getName = (EditText) customView.findViewById(R.id.get_name);
                        String name = getName.getText().toString();
                        dialogInterface.dismiss();
                        Message message = mWebHandler.obtainMessage();
                        if (type == 0) {
                            message.what = 0;
                            message.obj = name;
                        }
                        if (type == 1) {
                            Bundle bundle = new Bundle();
                            bundle.putString("user", name);
                            int position = role.getSelectedItemPosition();
                            String role_name = roleUsers.get(position).getRole();
                            bundle.putString("role", role_name);
                            message.what = 1;
                            message.obj = bundle;
                        }
                        mWebHandler.sendMessage(message);
                    }
                }).setNegativeButton(negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                Toast.makeText(getActivity(), negative, Toast.LENGTH_SHORT).show();
            }
        }).create().show();
    }


    public class WebThread extends Thread {
        @Override
        public void run() {
            Looper.prepare();
            mWebHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    String role_name;
                    int position;
                    switch (msg.what) {
                        case 0:
                            String parentName = (String) msg.obj;
                            getBuildAuth(parentName, "", "", 0, 2);
                            upDataRoleUser();
                            break;
                        case 1:
                            Bundle bundle = (Bundle) msg.obj;
                            role_name = bundle.getString("role");
                            String user = bundle.getString("user");
                            getBuildAuth(role_name, user, "", 0, 3);
                            upDataRoleUser();
                            break;
                        case 2:
                            String menu = (String) msg.obj;
                            position = role.getSelectedItemPosition();
                            role_name = roleUsers.get(position).getRole();
                            getBuildAuth(role_name, menu, "", 1, 2);
                            upDataRoleUser();
                            break;
                        case 3:
                            String auth = (String) msg.obj;
                            position = role.getSelectedItemPosition();
                            role_name = roleUsers.get(position).getRole();
                            deleteMenuTransaction_List(role_name, auth);
                            upDataRoleUser();
                            break;
                    }
                }
            };
            List<AuthItem> authItems = getAuthManagement();
            Message messageFirst = mHandler.obtainMessage();
            messageFirst.obj = authItems;
            messageFirst.what = AUTHLIST;
            mHandler.sendMessage(messageFirst);
            upDataRoleUser();
            Looper.loop();
        }
    }

    private void upDataRoleUser() {
        List<RoleUser> roleUsers = getRole();
        Message messageSecond = mHandler.obtainMessage();
        messageSecond.obj = roleUsers;
        messageSecond.what = ROLE;
        mHandler.sendMessage(messageSecond);
    }

    private class UpDataThread extends Thread {
        List<AuthItem> authList = new ArrayList<AuthItem>();
        List<AuthGroup> groups = new ArrayList<AuthGroup>();
        List<AuthItem> childs = new ArrayList<AuthItem>();
        List<AuthItem> items;

        @Override
        public void run() {
            Looper.prepare();
            mDataHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    switch (msg.what) {
                        case 0:
                            authList = (List<AuthItem>) msg.obj;
                            groups.clear();
                            childs.clear();
                            for (AuthItem authItem : authList) {
                                if (authItem.getParentId().equals("-1")) {
                                    groups.add(new AuthGroup(authItem.getTitle(), authItem.getWether(), authItem.getId()));
                                } else {
                                    childs.add(authItem);
                                }
                            }
                            for (AuthItem authItem : childs) {
                                for (AuthGroup authGroup : groups) {
                                    if (authItem.getParentId().equals(authGroup.getID())) {
                                        authGroup.addAuthChild(new AuthChild(authItem.getTitle(), authItem.getWether()));
                                    }
                                }
                            }
                            Message message = mHandler.obtainMessage();
                            message.what = AUTHLISTSECOND;
                            message.obj = groups;
                            mHandler.sendMessage(message);
                            break;
                        case 1:
                            items = (List<AuthItem>) msg.obj;
                            groups.clear();
                            childs.clear();
                            for (AuthItem authItem : authList) {
                                authItem.setWether(false);
                            }
                            for (AuthItem item : items) {
                                for (int j = 0; j < authList.size(); j++) {
                                    if (item.getTitle().equals(authList.get(j).getTitle())) {
                                        authList.get(j).setWether(true);
                                        Log.i("data", item.getTitle());
                                    }
                                }
                            }
                            for (AuthItem authItem : authList) {
                                if (authItem.getParentId().equals("-1")) {
                                    groups.add(new AuthGroup(authItem.getTitle(), authItem.getWether(), authItem.getId()));
                                } else {
                                    childs.add(authItem);
                                }
                            }
                            for (AuthItem authItem : childs) {
                                for (AuthGroup authGroup : groups) {
                                    if (authItem.getParentId().equals(authGroup.getID())) {
                                        authGroup.addAuthChild(new AuthChild(authItem.getTitle(), authItem.getWether()));
                                    }
                                }
                            }
                            Message message2 = mHandler.obtainMessage();
                            message2.what = AUTHLISTSECOND;
                            message2.obj = groups;
                            mHandler.sendMessage(message2);
                            break;
                    }
                }
            };
            mHandler.sendEmptyMessage(DATAPRAPER);
            Looper.loop();
        }
    }

    //网络获取数据
    private List<AuthItem> getAuthManagement() {
        List<AuthItem> authItemList = new ArrayList<AuthItem>();
        SoapObject authManagement = new SoapObject(WebUtil.SERVICE_NS, WebUtil.AuthorityManagementData);
        authManagement.addProperty("Type", 1);
        authManagement.addProperty("FormSYS", "BI");
        SoapObject response = WebUtil.getWebData(authManagement);
        SoapObject detail1 = (SoapObject) response.getProperty(0);
        SoapObject detail2 = (SoapObject) detail1.getProperty(2);
        SoapObject detail3 = (SoapObject) detail2.getProperty(0);
        SoapObject item = null;
        String name = "";
        String ID = "";
        String parentID = "";
        AuthItem authItem = null;
        for (int i = 0; i < detail3.getPropertyCount(); i++) {
            item = (SoapObject) detail3.getProperty(i);
            name = item.getProperty("MenuName").toString();
            ID = item.getProperty("ID").toString();
            parentID = item.getProperty("ParentID").toString();
            authItem = new AuthItem(name, ID, parentID, false);
            authItemList.add(authItem);
        }

        return authItemList;
    }

    //网络获取数据
    private List<RoleUser> getRole() {
        List<RoleUser> roleUserList = new ArrayList<RoleUser>();
        SoapObject authManagement = new SoapObject(WebUtil.SERVICE_NS, WebUtil.AuthorityManagementData);
        authManagement.addProperty("Type", 0);
        authManagement.addProperty("FormSYS", "BI");
        SoapObject response = WebUtil.getWebData(authManagement);
        SoapObject detail1 = (SoapObject) response.getProperty(0);
        SoapObject detail2 = (SoapObject) detail1.getProperty(2);
        SoapObject detail3 = (SoapObject) detail2.getProperty(0);
        for (int j = 0; j < detail3.getPropertyCount(); j++) {
            SoapObject role = (SoapObject) detail3.getProperty(j);
            String roleName = role.getProperty("RuleName").toString();
            String accounts;
            String[] accountss;
            List<String> accountList = new ArrayList<String>();

            try {
                accounts = role.getProperty("Account").toString();
                accountss = accounts.split(",");
                for (int i = 0; i < accountss.length; i++) {
                    accountList.add(accountss[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String menuNames = role.getProperty("MenuName").toString();
                String menuIDs = role.getProperty("MenuID").toString();
                String menuParentIDs = role.getProperty("MenuParentID").toString();
                String[] menuNamess = menuNames.split(",");
                String[] menuIDss = menuIDs.split(",");
                String[] menuParentIDss = menuParentIDs.split(",");
                List<AuthItem> authItemList = new ArrayList<AuthItem>();
                for (int i = 0; i < menuNamess.length; i++) {
                    authItemList.add(new AuthItem(menuNamess[i], menuIDss[i], menuParentIDss[i], true));
                }
                roleUserList.add(new RoleUser(roleName, accountList, authItemList));
            } catch (Exception e) {
                e.printStackTrace();
                roleUserList.add(new RoleUser(roleName, accountList));
            }
        }
        return roleUserList;
    }

    private void getBuildAuth(String ParentName, String ChildName, String LinkURL, int Signal, int TransSignal) {
        SoapObject buildAuth = new SoapObject(WebUtil.SERVICE_NS, WebUtil.BuildMenuTransaction_List);
        buildAuth.addProperty("ParentName", ParentName);
        buildAuth.addProperty("ChildName", ChildName);
        buildAuth.addProperty("LinkURL", LinkURL);
        buildAuth.addProperty("FromSYS", "BI");
        buildAuth.addProperty("Signal", Signal);
        buildAuth.addProperty("TransSignal", TransSignal);
        SoapObject response = WebUtil.getWebData(buildAuth);
        SoapObject detail1 = (SoapObject) response.getProperty(0);
        try {
            SoapObject detail2 = (SoapObject) detail1.getProperty(2);
            SoapObject detail3 = (SoapObject) detail2.getProperty(0);
            SoapObject item = (SoapObject) detail3.getProperty(0);
            String responseDetail = item.getProperty("ReturnMsg").toString().split(":")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteMenuTransaction_List(String role_name, String auth_name) {
        SoapObject deleteAuth = new SoapObject(WebUtil.SERVICE_NS, WebUtil.DeleteMenuTransaction_List);
        deleteAuth.addProperty("ParentName", role_name);
        deleteAuth.addProperty("ChildName", auth_name);
        deleteAuth.addProperty("FromSYS", "BI");
        deleteAuth.addProperty("TransSignal", 3);
        SoapObject response = WebUtil.getWebData(deleteAuth);
        SoapObject detail1 = (SoapObject) response.getProperty(0);
        try {
            SoapObject detail2 = (SoapObject) detail1.getProperty(2);
            SoapObject detail3 = (SoapObject) detail2.getProperty(0);
            SoapObject item = (SoapObject) detail3.getProperty(0);
            String responseDetail = item.getProperty("ReturnMsg").toString().split(":")[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
