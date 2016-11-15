package com.blanke.bottomnavigationview_viewpager_demo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private ViewPager mainViewpager;
    private BottomNavigationView mainBottomnavigation;
    private FragmentPagerAdapter mFragmentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainViewpager = (ViewPager) findViewById(R.id.main_viewpager);
        mainBottomnavigation = (BottomNavigationView) findViewById(R.id.main_bottomnavigation);
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return TestFragment.newInstance
                        (mainBottomnavigation.getMenu().getItem(position).getTitle() + "");
            }

            @Override
            public int getCount() {
                return mainBottomnavigation.getMenu().size();
            }
        };
        mainViewpager.setAdapter(mFragmentPagerAdapter);
        mainViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            private int lastPosition = 0;

            @Override
            public void onPageSelected(int position) {
//                mainBottomnavigation.getMenu().getItem(lastPosition).setChecked(false);
//                mainBottomnavigation.getMenu().getItem(position).setChecked(true);
//                lastPosition = position;

                View temp = mainBottomnavigation.getChildAt(0);
                if (temp instanceof BottomNavigationMenuView) {
                    BottomNavigationMenuView bmv = (BottomNavigationMenuView) temp;
                    bmv.getChildAt(position).performClick();
                }

            }
        });
        mainBottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int position = 0;
                switch (item.getItemId()) {
                    case R.id.menu_call:
                        break;
                    case R.id.menu_desc:
                        position = 1;
                        break;
                    case R.id.menu_grade:
                        position = 2;
                        break;
                    default:
                        position = 3;
                        break;
                }
                if(mainViewpager.getCurrentItem()!=position) {
                    mainViewpager.setCurrentItem(position);
                    //do something
                }
                return false;
            }
        });
    }
}
