package cl.magnet.vigia.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import cl.magnet.vigia.R;
import cl.magnet.vigia.fragments.ReportListFragment;
import cl.magnet.vigia.fragments.Section1Fragment;
import cl.magnet.vigia.fragments.Section2Fragment;

/**
 * Created by edwinperaza on 12/28/16.
 */

public class PagerTabAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;
    private String tabTitles[] = null;
    private Context context;

    public PagerTabAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        tabTitles = context.getResources().getStringArray(R.array.tabs_title);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return ReportListFragment.newInstance("", "");
            case 1:
                return Section2Fragment.newInstance("", "");
            default:
                return ReportListFragment.newInstance("", "");
        }
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
