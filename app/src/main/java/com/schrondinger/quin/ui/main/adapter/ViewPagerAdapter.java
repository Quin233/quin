package com.schrondinger.quin.ui.main.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.schrondinger.quin.R;
import com.schrondinger.quin.Utils.Util;

import java.util.ArrayList;

/**
 * Created by HP on 2018/1/5.
 */

public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<View> views;
    private int[] imgs;
    private Context context;
    public ViewPagerAdapter(ArrayList<View> views, int[] imgs, Context context){
        this.views=views;
        this.imgs=imgs;
        this.context=context;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        ImageView iv_welcome=views.get(position).findViewById(R.id.iv_welcome);
        Util.loadImage(iv_welcome,imgs[position],context);
        return views.get(position);
    }
}
