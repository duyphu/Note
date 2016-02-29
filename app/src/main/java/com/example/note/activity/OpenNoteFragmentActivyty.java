package com.example.note.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.ImageButton;

import com.example.note.R;
import com.example.note.activity.base.BaseActivity;
import com.example.note.activity.fragment.ScreenSlidePageFragment;

import java.util.ArrayList;

/**
 * Created by phund on 2/29/2016.
 */
public class OpenNoteFragmentActivyty extends BaseActivity {
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    private static final int NUM_PAGES = 5;
    private Toolbar mToolbarBottom;
    private int mNoteId;
    private ArrayList<Integer> mIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_note);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.t_top_note);
        setSupportActionBar(toolbarTop);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.notes);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mToolbarBottom = (Toolbar) findViewById(R.id.t_bottom_note);

        mNoteId = getIntent().getIntExtra("noteId", 0);
        mIds = getIntent().getIntegerArrayListExtra("listId");

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When changing pages, reset the action bar actions since they are dependent
                // on which page is currently active. An alternative approach is to have each
                // fragment expose actions itself (rather than the activity exposing actions),
                // but for simplicity, the activity provides the actions in this sample.
//                invalidateOptionsMenu();
                int resId;
                if(mPager.getCurrentItem() == mPagerAdapter.getCount() - 1){
                    resId = R.drawable.ic_navigate_next_disable;
                } else {
                    resId = R.drawable.ic_navigate_next;
                }
                ImageButton mImageButton = (ImageButton)mToolbarBottom.findViewById(R.id.ib_next);
                mImageButton.setImageResource(resId);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_top_note, menu);
        Log.i("menu length", menu.size()+"");
//        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);
//
//        // Add either a "next" or "finish" button to the action bar, depending on which page
//        // is currently selected.
//        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE, "Next");
////                (mPager.getCurrentItem() == mPagerAdapter.getCount() - 1)
////                        ? R.string.action_finish
////                        : R.string.action_next);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
//        item.setEnabled(mPager.getCurrentItem() < mPagerAdapter.getCount() - 1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Navigate "up" the demo structure to the launchpad activity.
                // See http://developer.android.com/design/patterns/navigation.html for more.
                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return mIds.size();
        }
    }

    public void ibNextOnClick(View v){
        mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    }

    public void ibPreviousOnClick(View v){
        mPager.setCurrentItem(mPager.getCurrentItem() - 1);
    }

    public void ibShareOnClick(View v){

    }

    public void ibDeleteOnClick(View v){

    }
}
