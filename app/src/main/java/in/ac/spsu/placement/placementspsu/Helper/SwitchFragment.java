package in.ac.spsu.placement.placementspsu.Helper;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import in.ac.spsu.placement.placementspsu.R;

public class SwitchFragment {
    public static void replaceFragment(Fragment fragment,FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container,fragment);
        ft.commit();
    }

    public static void addFragment(Fragment fragment,FragmentManager fm) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container,fragment).addToBackStack("monu");
        ft.commit();
    }
}
