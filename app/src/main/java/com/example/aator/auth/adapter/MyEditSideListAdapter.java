package com.example.aator.auth.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.aator.auth.R;
import com.example.aator.auth.bean.EditGroup;
import com.example.aator.auth.bean.EditItem;

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
 * Created by dutingjue on 2016/12/2.
 */

public class MyEditSideListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<EditGroup> editGroups = new ArrayList<>();

    public MyEditSideListAdapter(Context context, List<EditGroup> editGroups) {
        this.context = context;
        this.editGroups = editGroups;
    }

    @Override
    public int getGroupCount() {
        return editGroups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return editGroups.get(i).getEditItems().size();
    }

    @Override
    public Object getGroup(int i) {
        return editGroups.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return editGroups.get(i).getEditItems().get(i1);
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
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        GroupHolder group = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.edit_side_group, viewGroup, false);
            group = new GroupHolder();
            group.groupText = (TextView) view.findViewById(R.id.edit_expand_group_text);
            view.setTag(group);
        } else {
            group = (GroupHolder) view.getTag();
        }
        group.groupText.setText(editGroups.get(i).getName());
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ChildrenHolder child = null;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.edit_side_item_children, viewGroup, false);
            child = new ChildrenHolder();
            child.childrenText = (TextView) view.findViewById(R.id.edit_item_name);
            child.editText = (EditText) view.findViewById(R.id.edit_item_edit);
            child.spinner = (Spinner) view.findViewById(R.id.edit_item_spinner);
            view.setTag(child);
        } else {
            child = (ChildrenHolder) view.getTag();
        }
        EditItem item = editGroups.get(i).getEditItems().get(i1);
        child.childrenText.setText(item.getName());
        if (item.getWether()) {
            child.editText.setVisibility(View.VISIBLE);
            child.spinner.setVisibility(View.GONE);
        } else {
            child.editText.setVisibility(View.GONE);
            child.spinner.setVisibility(View.VISIBLE);
            ArrayAdapter<String> mEditSpinner = new ArrayAdapter<String>(context, R.layout.spinner_item, item.getValues());
            mEditSpinner.setDropDownViewResource(R.layout.spinner_window);
            child.spinner.setAdapter(mEditSpinner);
            child.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private class GroupHolder {
        TextView groupText;
    }

    private class ChildrenHolder {
        TextView childrenText;
        EditText editText;
        Spinner spinner;
    }
}
