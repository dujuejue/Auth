package com.example.aator.auth.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.aator.auth.R;
import com.example.aator.auth.adapter.MyEditSideListAdapter;
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

public class EditSideFragment extends Fragment {
    private ExpandableListView eListView;
    private List<EditGroup> editGroups = new ArrayList<>();
    private MyEditSideListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_sidebar, container, false);
        initData();
        initExpandableList(view);
        return view;
    }

    //初始化expandableListview控件
    private void initExpandableList(View view) {
        eListView = (ExpandableListView) view.findViewById(R.id.edit_side_expandable);
        adapter = new MyEditSideListAdapter(getActivity(), editGroups);
        eListView.setAdapter(adapter);
    }

    //初始化数据
    private void initData() {
        List<EditItem> items1 = new ArrayList<>();
        items1.add(new EditItem("aa", ""));
        List<String> values1 = new ArrayList<>();
        values1.add("11");
        values1.add("22");
        items1.add(new EditItem("bb", values1));
        editGroups.add(new EditGroup("00", items1));
    }
}
