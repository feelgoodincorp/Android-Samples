package com.example.user.restart2;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import java.util.List;

/**
 * Created by User on 15.12.2017.
 */

public class NewPagerAdapter extends PagerAdapter {
    List<View> pages = null;

    public NewPagerAdapter(List<View>pages){
        this.pages = pages;
    }

    @Override
    public Object instantiateItem(View collection,int position){
        View v = pages.get(position);
        ((VerticalViewPager)collection).addView(v,0);
        return v;
    }

    @Override
    public void destroyItem(View collection, int position, Object view){
        ((VerticalViewPager)collection).removeView((View)view);


    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
