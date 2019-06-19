package in.ac.spsu.placement.placementspsu.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    ArrayList<String> tiles = new ArrayList<String>();

    public void addFragment(Fragment fragments, String tiles){
        this.fragments.add(fragments);
        this.tiles.add(tiles);
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tiles.get(position);
    }
}
