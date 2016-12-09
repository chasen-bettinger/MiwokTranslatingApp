package com.example.android.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.miwok.ColorsFragment;
import com.example.android.miwok.FamilyFragment;
import com.example.android.miwok.NumbersFragment;
import com.example.android.miwok.PhrasesFragment;

/**
 * Provides the appropriate {@link Fragment} for a view pager.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    // Initialize mContext variable to retrieve values from the activity
    private Context mContext;

    // FragmentAdapter constructor
    // @param context takes a context that is assigned to mContext variable
    // @param fm takes a FragmentManager type to create a new FragmentManager
    public FragmentAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // Gets the current fragment to display on the screen
    // @param position current position of the app
    // @return Fragment that contains information that will be displayed to user
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NumbersFragment();
        } else if (position == 1) {
            return new ColorsFragment();
        } else if (position == 2) {
            return new PhrasesFragment();
        } else {
            return new FamilyFragment();
        }
    }

    // Gets the number of pages in the app
    // @return number of pages in the app
    @Override
    public int getCount() {
        return 4;
    }

    // Retrieves the page title to be displayed on the tabs
    // @param position current position that the app is at
    // @return CharSequence title of each fragment that will be displayed on the tab
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_numbers);
        } else if (position == 1) {
            return mContext.getString(R.string.category_colors);
        } else if (position == 2) {
            return mContext.getString(R.string.category_phrases);
        } else {
            return mContext.getString(R.string.category_family);
        }
    }
}