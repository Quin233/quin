package com.schrondinger.quin.ui.mine.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.schrondinger.quin.base.activity.BaseFragment;
import com.schrondinger.quin.bean.common.CommMap;

import java.util.ArrayList;

/**
 * Created by HP on 2018/1/22.
 */

public class ContentPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<CommMap> fragments;

    public ContentPagerAdapter(FragmentManager fm, ArrayList<CommMap> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return (BaseFragment)Class.forName(fragments.get(position).getValue()).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getName();
    }
}
