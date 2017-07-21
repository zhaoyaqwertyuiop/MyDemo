package com.materialdesigndemo;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private DrawerLayout drawerLayout;
    private List<Card> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) super.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false); // 不显示toobar的title
        ((TextView) super.findViewById(R.id.toolbar_title)).setText(R.string.MainActivity);

        this.drawerLayout = (DrawerLayout) super.findViewById(R.id.drawer_layout);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
            //将侧边栏顶部延伸至status bar
            drawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            drawerLayout.setClipToPadding(false);
        }

        NavigationView navigationView = (NavigationView) super.findViewById(R.id.nav_view);
        // ActionBarDrawerToggle是一个开关，用于打开/关闭DrawerLayout抽屉
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();  // 该方法会自动和actionBar关联, 将开关的图片显示在了action上，如果不设置，也可以有抽屉的效果，不过是默认的图标
        drawerLayout.addDrawerListener(mDrawerToggle);
        setupDrawerContent(navigationView);

        initData();
        initRecyclerView();
    }

    private void initData() {
        list = new ArrayList<>();
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_01));
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_02));
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_03));
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_04));
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_05));
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_06));
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_07));
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_08));
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_09));
        list.add(getCard("name" + list.size(), R.mipmap.ic_recyclerview_10));
    }

    private Card getCard(String name, int imageId) {
        Card card = new Card();
        card.setImageId(imageId);
        card.setName(name);
        return card;
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) super.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setAdapter(new CardAdapter(list));
    }

    /** 设置DrawerView的内容 */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_1:
                                ToastUtil.show("nav1");
                                startActivity(new Intent(MainActivity.this, TabLayoutActivity.class));
                                break;
                            case R.id.nav_2:
                                ToastUtil.show("nav2");
                                break;
                            case R.id.nav_3:
                                ToastUtil.show("nav3");
                                break;
                            case R.id.nav_4:
                                ToastUtil.show("nav4");
                                break;
                            case R.id.nav_5:
                                ToastUtil.show("nav5");
                                break;
                            default: break;
                        }
                        menuItem.setChecked(true); // 设置成选中状态
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        navigationView.setCheckedItem(R.id.nav_1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return false; // true 显示menu, false 不显示
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.backup: ToastUtil.show("You Click Backup"); break;
            case R.id.delete: ToastUtil.show("You Click Delete"); break;
            case R.id.settings: ToastUtil.show("You Click Settings"); break;
            default: break;
        }
        return super.onOptionsItemSelected(item);
    }

    private long exitTime = 0;

    public void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtil.show("再按一次退出程序");
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
//        if (selectedFragment == null || !selectedFragment.onBackPressed()) {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            doExitApp();
        }
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                ToastUtil.showSanckbar(v, "点击FloatingActionButton了!", "我是按钮", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtil.show("点击了Snackbar");
                    }
                });
//                Snackbar.make(v, "Data deleted", Snackbar.LENGTH_SHORT)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                ToastUtil.show("Data restored");
//                            }
//                        }).show();
                break;
            case R.id.rightTV:
                ToastUtil.show("点击了按钮");
                break;
            default: break;
        }
    }
}
