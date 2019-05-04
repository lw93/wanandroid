package com.xygit.note.notebook.main;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.MenuItem;

import com.xygit.note.notebook.R;
import com.xygit.note.notebook.adapter.ViewPagerAdapter;
import com.xygit.note.notebook.base.BaseActivity;
import com.xygit.note.notebook.main.fragment.HomeFragment;
import com.xygit.note.notebook.main.fragment.MineFragment;
import com.xygit.note.notebook.main.fragment.NavigationFragment;
import com.xygit.note.notebook.main.fragment.ProjectFragment;
import com.xygit.note.notebook.view.BottomNavigationViewHelper;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener {

    private ViewPager vpReplaceActivityMain;
    private BottomNavigationView btNavigationActivityMain;
    private SparseArray<Fragment> fragmentSparse;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    public int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        btNavigationActivityMain = findViewById(R.id.bt_navigation_activity_main);
        BottomNavigationViewHelper.disableShiftMode(btNavigationActivityMain);
        vpReplaceActivityMain = findViewById(R.id.vp_replace_activity_main);
    }

    @Override
    public void initAction() {
        btNavigationActivityMain.setOnNavigationItemSelectedListener(this);
        vpReplaceActivityMain.addOnPageChangeListener(this);
    }

    @Override
    public void initData() {
        fragmentSparse = new SparseArray<>(4);
        fragmentSparse.append(0, new HomeFragment());
        fragmentSparse.append(1, new ProjectFragment());
        fragmentSparse.append(2, new NavigationFragment());
        fragmentSparse.append(3, new MineFragment());
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragmentSparse);
        vpReplaceActivityMain.setAdapter(viewPagerAdapter);
        vpReplaceActivityMain.setOffscreenPageLimit(4);
        btNavigationActivityMain.setSelectedItemId(R.id.tab_home);
    }

    @Override
    public void clearData() {
        fragmentSparse.clear();
        fragmentSparse = null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tab_home) {
            vpReplaceActivityMain.setCurrentItem(0);
        } else if (id == R.id.tab_project) {
            vpReplaceActivityMain.setCurrentItem(1);
        } else if (id == R.id.tab_navigation) {
            vpReplaceActivityMain.setCurrentItem(2);
        } else if (id == R.id.tab_mine) {
            vpReplaceActivityMain.setCurrentItem(3);
        }
        return false;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        btNavigationActivityMain.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
