package com.example.aator.auth.activity;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;


import com.example.aator.auth.R;
import com.example.aator.auth.bean.AuthItem;
import com.example.aator.auth.fragment.MenuFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FrameLayout fl_content, side_edit;
    private MenuFragment menuFragment;
    private DrawerLayout mDrawerLayout;
    private long firstTime;
    private String curId;
    private Toolbar toolbar;
    private MenuFragment menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        loadLatest();

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.light_toolbar));
        setSupportActionBar(toolbar);
        setStatusBarColor(getResources().getColor(R.color.light_toolbar));
        fl_content = (FrameLayout) findViewById(R.id.fl_content);
        side_edit = (FrameLayout) findViewById(R.id.edit_side_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        menu = (MenuFragment) getSupportFragmentManager().findFragmentById(R.id.menu_fragment);
        Intent intent = getIntent();
        List<AuthItem> authItemList = (List<AuthItem>) intent.getSerializableExtra("menu");
        String name = intent.getStringExtra("user");
        menu.updateData(authItemList, name);
    }

    public void closeMenu() {
        mDrawerLayout.closeDrawers();
    }

    public void loadLatest() {

        curId = "latest";
    }

    @TargetApi(21)
    private void setStatusBarColor(int statusBarColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // If both system bars are black, we can remove these from our layout,
            // removing or shrinking the SurfaceFlinger overlay required for our views.
            Window window = this.getWindow();
            if (statusBarColor == Color.BLACK && window.getNavigationBarColor() == Color.BLACK) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            }
            window.setStatusBarColor(statusBarColor);
        }
    }

    public void hideShowSideEdit(Boolean wether) {
        if (wether) {
            side_edit.setVisibility(View.VISIBLE);
        } else {
            side_edit.setVisibility(View.GONE);
        }
    }
}
