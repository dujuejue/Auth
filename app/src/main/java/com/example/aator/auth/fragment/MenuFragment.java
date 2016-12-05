package com.example.aator.auth.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aator.auth.adapter.MyExpandMenuAdapter;
import com.example.aator.auth.bean.AuthChild;
import com.example.aator.auth.bean.AuthGroup;
import com.example.aator.auth.bean.AuthItem;
import com.example.aator.auth.activity.MainActivity;
import com.example.aator.auth.R;


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
 * Created by dutingjue on 2016/11/8.
 */
public class MenuFragment extends BaseFragment implements View.OnClickListener {

    private ExpandableListView lv_item;
    private String name;
    private TextView tv_login;
    private LinearLayout ll_menu;
    private List<AuthItem> items = new ArrayList<AuthItem>();
    private List<AuthGroup> groups = new ArrayList<AuthGroup>();
    private List<AuthItem> childs = new ArrayList<AuthItem>();
    //    private ListTypeAdapter listTypeAdapter;
    private MyExpandMenuAdapter listAdapter;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.menu, container, false);
        ll_menu = (LinearLayout) view.findViewById(R.id.ll_menu);
        tv_login = (TextView) view.findViewById(R.id.tv_login);
        tv_login.setText(name);
        lv_item = (ExpandableListView) view.findViewById(R.id.lv_item);
        lv_item.setGroupIndicator(null);
        lv_item.setDivider(null);
        lv_item.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                if (groups.get(i).getAuthChildList().size() > 0) {
                    return false;
                } else if (groups.get(i).getTitle().equals("权限管理")) {
//                    getFragmentManager().beginTransaction().
//                            setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left).
//                            replace(R.id.fl_content, new AuthFragment(), "auth").
//                            commit();
                    ((MainActivity) mActivity).hideShowSideEdit(false);
                    AuthFragment authFragment = (AuthFragment) fm.findFragmentByTag("auth");
                    PlatoFormFragment platoFormFragment = (PlatoFormFragment) fm.findFragmentByTag("plato");
                    if (authFragment != null) {
                        ft.hide(authFragment);
                    }
                    if (platoFormFragment != null) {
                        ft.hide(platoFormFragment);
                    }
                    if (authFragment == null) {
                        authFragment = new AuthFragment();
                        ft.add(R.id.fl_content, authFragment, "auth");
                    } else {
                        ft.show(authFragment);
                    }
                    ft.commit();
                    ((MainActivity) mActivity).closeMenu();
                    return true;
                } else if (groups.get(i).getTitle().equals("test")) {
                    ((MainActivity) mActivity).hideShowSideEdit(true);
                    AuthFragment authFragment = (AuthFragment) fm.findFragmentByTag("auth");
                    PlatoFormFragment platoFormFragment = (PlatoFormFragment) fm.findFragmentByTag("plato");
                    EditSideFragment editSideFragment = (EditSideFragment) fm.findFragmentByTag("edit");
                    if (authFragment != null) {
                        ft.hide(authFragment);
                    }
                    if (platoFormFragment != null) {
                        ft.hide(platoFormFragment);
                    }
                    if (editSideFragment == null) {
                        editSideFragment = new EditSideFragment();
                        ft.add(R.id.edit_side_layout, editSideFragment, "edit");
                    } else {
                        ft.show(editSideFragment);
                    }
                    ft.commit();
                    ((MainActivity) mActivity).closeMenu();
                    return true;
                } else {
                    return true;
                }
            }
        });
        lv_item.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                android.support.v4.app.FragmentManager fm = getFragmentManager();
                android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
                if (groups.get(i).getAuthChildList().get(i1).getTitle().equals("柏拉图")) {
//                    getFragmentManager().beginTransaction()
//                            .setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left)
//                            .replace(R.id.fl_content, new PlatoFormFragment(), "plato")
//                            .commit();
                    AuthFragment authFragment = (AuthFragment) fm.findFragmentByTag("auth");
                    PlatoFormFragment platoFormFragment = (PlatoFormFragment) fm.findFragmentByTag("plato");
                    ((MainActivity) mActivity).hideShowSideEdit(false);
                    if (authFragment != null) {
                        ft.hide(authFragment);
                    }
                    if (platoFormFragment != null) {
                        ft.hide(platoFormFragment);
                    }
                    if (platoFormFragment == null) {
                        platoFormFragment = new PlatoFormFragment();
                        ft.add(R.id.fl_content, platoFormFragment, "plato");
                    } else {
                        ft.show(platoFormFragment);
                    }
                    ft.commit();
                    ((MainActivity) mActivity).closeMenu();
                }
                return false;
            }
        });


        return view;
    }

    @Override
    public void onClick(View view) {

    }


    public void updateData(List<AuthItem> authItemList, String name) {
        this.name = name;
        tv_login.setText(name);
        items = authItemList;
        for (AuthItem authItem : items) {
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
        listAdapter = new MyExpandMenuAdapter(getActivity(), groups);
        lv_item.setAdapter(listAdapter);
    }


    public android.support.v4.app.FragmentTransaction hideFragment() {
        android.support.v4.app.FragmentManager fm = getFragmentManager();
        android.support.v4.app.FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
        AuthFragment authFragment = (AuthFragment) fm.findFragmentByTag("auth");
        PlatoFormFragment platoFormFragment = (PlatoFormFragment) fm.findFragmentByTag("plato");
        if (authFragment != null) {
            ft.hide(authFragment);
        }
        if (platoFormFragment != null) {
            ft.hide(platoFormFragment);
        }
        return ft;
    }


}
