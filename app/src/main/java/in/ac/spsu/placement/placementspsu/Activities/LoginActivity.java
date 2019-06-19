package in.ac.spsu.placement.placementspsu.Activities;

import android.annotation.TargetApi;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import in.ac.spsu.placement.placementspsu.Fragments.Login;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Fragments.Register;
import in.ac.spsu.placement.placementspsu.Adapters.ViewPagerAdapter;
import in.ac.spsu.placement.placementspsu.Transformers.FadeOutTransformation;
import in.ac.spsu.placement.placementspsu.Transformers.ZoomOutPageTransformer;

public class LoginActivity extends BaseActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            setSupportActionBar(toolbar);

            setSupportActionBar(toolbar);
            toolbar.setTitle("Placement Cell");

            final Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }

        changeFragment(new Login(),"login");

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.container);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Login(),"Login");
        adapter.addFragment(new Register(),"Register");

        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
                /*switch(tab.getPosition()){
                    case 0:
                        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        toolbar.setTitleTextColor(getResources().getColor(R.color.black));
                        tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.black));
                        break;
                    case 1:
                        window.setStatusBarColor(getResources().getColor(R.color.blueDark));
                        toolbar.setBackgroundColor(getResources().getColor(R.color.blueDark));
                        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                        tabLayout.setTabTextColors(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                        tabLayout.setBackgroundColor(getResources().getColor(R.color.blueDark));
                        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.white));
                        break;
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void changeFragment(Fragment fragment, String tmpName) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container,fragment,tmpName);
        ft.addToBackStack(tmpName);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, getString(R.string.press_again), Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }
}
