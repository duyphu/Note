package com.example.note.custom.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.example.note.activity.fragment.NotePageFragment;

import java.util.ArrayList;

/**
 * Created by phund on 3/1/2016.
 */
public class NotePagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
    ArrayList<Integer> mIds;

    public NotePagerAdapter(FragmentManager fm, ArrayList<Integer> ids) {
        super(fm);
        mIds = ids;
    }

    @Override
    public Fragment getItem(int position) {
        return NotePageFragment.create(position, mIds);
    }

    @Override
    public int getCount() {
        return mIds.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}