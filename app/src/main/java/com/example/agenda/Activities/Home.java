package com.example.agenda.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.agenda.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        prepareViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void prepareViewPager(final ViewPager viewPager) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        Today today = new Today();
        AllActivities allActivities = new AllActivities();
        TomorrowActivity tomorrowActivity = new TomorrowActivity();
        adapter.add();
        adapter.addFragment(today, tomorrowActivity, allActivities);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class MainAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        ArrayList<String> titleList = new ArrayList<>();

        public void add() {
            titleList.add("Today");
            titleList.add("Tomorrow");
           titleList.add("All Activities");
        }

        public void addFragment(Fragment fragment, Fragment fragment2, Fragment fragment3) {
            fragmentList.add(fragment);
            fragmentList.add(fragment2);
            fragmentList.add(fragment3);
        }

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }
}
