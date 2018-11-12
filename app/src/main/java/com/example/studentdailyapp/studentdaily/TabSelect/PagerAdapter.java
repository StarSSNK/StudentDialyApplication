package com.example.studentdailyapp.studentdaily.TabSelect;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: return new Incomplete_Fragment();

            case 1:
                return new CompleteTask_Fragment();

            case 2: return new Overdue_fragment();

            default:
                return new Incomplete_Fragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String tab[] = {"Incomplete", "Completed", "Overdue "};
        return tab[position];
    }
}
