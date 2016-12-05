package com.example.aator.auth.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aator.auth.bean.AuthChild;
import com.example.aator.auth.bean.AuthGroup;
import com.example.aator.auth.R;
import com.example.aator.auth.fragment.AuthFragment;

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
public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<AuthGroup> groupTitle;
    private AuthFragment.WebThread webThread;
    private Handler webHandler;

    public MyExpandableListAdapter(Context mContext, List<AuthGroup> groupTitle, AuthFragment.WebThread webThread, Handler webHandler) {
        this.mContext = mContext;
        this.groupTitle = groupTitle;
        this.webThread = webThread;
        this.webHandler = webHandler;
    }

    @Override
    public int getGroupCount() {
        return groupTitle.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupTitle.get(i).getAuthChildList().size();
    }

    @Override
    public Object getGroup(int i) {
        return groupTitle.get(i).getAuthChildList();
    }

    @Override
    public Object getChild(int i, int i1) {
        return groupTitle.get(i).getAuthChildList().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        final int pos = i;
        GroupHolder groupHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.expand_group, null);
            groupHolder = new GroupHolder();
            groupHolder.groupTextSwitch = (TextView) convertView.findViewById(R.id.expand_group_text_switch);
            groupHolder.groupText = (TextView) convertView.findViewById(R.id.expand_group_text);
            groupHolder.groupImage = (ImageView) convertView.findViewById(R.id.group_indicator);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.groupText.setText(groupTitle.get(i).getTitle());
        groupHolder.groupText.setTextColor(mContext.getResources().getColor(R.color.light_menu_listview_textcolor));
        if (groupTitle.get(i).getWhether()) {
            groupHolder.groupTextSwitch.setBackgroundResource(R.drawable.switch_yes);
        } else groupHolder.groupTextSwitch.setBackgroundResource(R.drawable.switch_no);
        if (groupTitle.get(i).getAuthChildList().size() == 0) {
            groupHolder.groupImage.setVisibility(View.INVISIBLE);
        } else {
            groupHolder.groupImage.setVisibility(View.VISIBLE);
        }
        groupHolder.groupTextSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("click", "group");
                if (groupTitle.get(pos).getWhether()) {
                    groupTitle.get(pos).setWhether(false);
                    Message message = webHandler.obtainMessage();
                    message.what = 3;
                    message.obj = groupTitle.get(pos).getTitle();
                    webHandler.sendMessage(message);
                    for (int num = 0; num < groupTitle.get(pos).getAuthChildList().size(); num++) {
                        groupTitle.get(pos).getAuthChildList().get(num).setWhether(false);
                        Message messageChild = webHandler.obtainMessage();
                        messageChild.what = 3;
                        messageChild.obj = groupTitle.get(pos).getAuthChildList().get(num).getTitle();
                        webHandler.sendMessage(messageChild);
                    }

                } else {
                    Message message = webHandler.obtainMessage();
                    groupTitle.get(pos).setWhether(true);
                    message.what = 2;
                    message.obj = groupTitle.get(pos).getTitle();
                    webHandler.sendMessage(message);
                    for (int num = 0; num < groupTitle.get(pos).getAuthChildList().size(); num++) {
                        groupTitle.get(pos).getAuthChildList().get(num).setWhether(true);
                        Message messageChild = webHandler.obtainMessage();
                        messageChild.what = 2;
                        messageChild.obj = groupTitle.get(pos).getAuthChildList().get(num).getTitle();
                        webHandler.sendMessage(messageChild);
                    }
                }
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildHolder childHolder = null;
        final int posGroup = i;
        final int posChild = i1;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.expand_child, null);
            childHolder = new ChildHolder();
            childHolder.childText = (TextView) view.findViewById(R.id.expand_child_text);
            childHolder.childTextSwitch = (TextView) view.findViewById(R.id.expand_child_text_switch);
            view.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) view.getTag();
        }
        childHolder.childText.setTextColor(mContext.getResources().getColor(R.color.light_menu_listview_textcolor));
        childHolder.childText.setText(groupTitle.get(i).getAuthChildList().get(i1).getTitle());
        if (groupTitle.get(i).getAuthChildList().get(i1).getWhether()) {
            childHolder.childTextSwitch.setBackgroundResource(R.drawable.switch_yes);
        } else childHolder.childTextSwitch.setBackgroundResource(R.drawable.switch_no);
        childHolder.childTextSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("click", "childview");
                if (groupTitle.get(posGroup).getAuthChildList().get(posChild).getWhether()) {
                    groupTitle.get(posGroup).getAuthChildList().get(posChild).setWhether(false);
                    Message message = webHandler.obtainMessage();
                    message.what = 3;
                    message.obj = groupTitle.get(posGroup).getAuthChildList().get(posChild).getTitle();
                    webHandler.sendMessage(message);
                    Boolean wether = false;
                    for (AuthChild authChild : groupTitle.get(posGroup).getAuthChildList()) {
                        if (authChild.getWhether()) {
                            wether = true;
                            break;
                        }
                    }
                    if (!wether) {
                        groupTitle.get(posGroup).setWhether(false);
                        Message messageGroup = webHandler.obtainMessage();
                        messageGroup.what = 3;
                        messageGroup.obj = groupTitle.get(posGroup).getTitle();
                        webHandler.sendMessage(messageGroup);
                    }
                } else {
                    groupTitle.get(posGroup).getAuthChildList().get(posChild).setWhether(true);
                    Message message = webHandler.obtainMessage();
                    message.what = 2;
                    message.obj = groupTitle.get(posGroup).getAuthChildList().get(posChild).getTitle();
                    webHandler.sendMessage(message);
                    if (!groupTitle.get(posGroup).getWhether()) {
                        groupTitle.get(posGroup).setWhether(true);
                        Message messageGroup = webHandler.obtainMessage();
                        messageGroup.what = 2;
                        messageGroup.obj = groupTitle.get(posGroup).getTitle();
                        webHandler.sendMessage(messageGroup);
                    }
                }
                notifyDataSetChanged();
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private class GroupHolder {
        TextView groupTextSwitch;
        TextView groupText;
        ImageView groupImage;
    }

    private class ChildHolder {
        TextView childTextSwitch;
        TextView childText;
    }


}
