package com.materialdesigndemo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoya on 2017/3/27.
 */

public class TabLayoutActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private List<String> titleList = new ArrayList<>();
    private List<PageFragmet> PageViewList = new ArrayList<>();
    private FragmentPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout);

        this.tabLayout = (TabLayout) super.findViewById(R.id.pages_tab);
        this.viewPager = (ViewPager) super.findViewById(R.id.pages_pager);

        this.tabLayout.setTabMode(TabLayout.MODE_FIXED);
        titleList = new ArrayList<>();
        PageViewList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            titleList.add("title" + i);
            PageViewList.add(new PageFragmet());
        }

        adapter = new FragmentPagerAdapter(this.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PageViewList.get(position);
            }

            @Override
            public int getCount() {
                return PageViewList.size();
            }

            // 此方法用来显示tab上的title
            @Override
            public CharSequence getPageTitle(int position) {
                return titleList.get(position);
            }
        };


        this.viewPager.setAdapter(adapter);
        this.tabLayout.setupWithViewPager(viewPager); // TabLayout加载viewpager
    }

    @SuppressLint("ValidFragment")
    public static class PageFragmet extends Fragment {
        public PageFragmet() {
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_test, null);
//            return pageTag.createView(inflater.getContext());
            return view;
        }
    }
}
