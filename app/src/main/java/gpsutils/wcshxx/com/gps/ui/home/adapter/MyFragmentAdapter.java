package gpsutils.wcshxx.com.gps.ui.home.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import gpsutils.wcshxx.com.gps.base.view.BaseFragment;

public class MyFragmentAdapter extends FragmentStatePagerAdapter {

    private String[] title = {"GPS记录","历史记录"};
    private List<BaseFragment> fragments;

    public MyFragmentAdapter(FragmentManager fm,List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null ? 0 : fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }
}
