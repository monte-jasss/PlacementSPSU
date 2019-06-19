package in.ac.spsu.placement.placementspsu.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.spsu.placement.placementspsu.Fragments.JobDetails;
import in.ac.spsu.placement.placementspsu.Fragments.JobFeed;
import in.ac.spsu.placement.placementspsu.Fragments.Login;
import in.ac.spsu.placement.placementspsu.Fragments.Profile;
import in.ac.spsu.placement.placementspsu.Fragments.Request;
import in.ac.spsu.placement.placementspsu.Fragments.UpdateProfile;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.Helper.SwitchFragment;
import in.ac.spsu.placement.placementspsu.R;

public class Home extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, JobFeed.OnShowJobDetails {

    private TextView tv;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            setSupportActionBar(toolbar);

            setSupportActionBar(toolbar);
            toolbar.setTitle("Job List");

            final Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.developer, Snackbar.LENGTH_LONG).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SwitchFragment.replaceFragment(new JobFeed(),getSupportFragmentManager());

        builder = new AlertDialog.Builder(Home.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.action_logout)+" ?");
        builder.setMessage(getString(R.string.do_log));
        builder.setPositiveButton(getString(R.string.action_logout),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPref.setUserName(getApplicationContext(),"");
                SharedPref.setUserType(getApplicationContext(),9);
                dialog.dismiss();
                startActivity(new Intent(Home.this,LoginActivity.class));
                finish();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();

        View header = navigationView.getHeaderView(0);
        TextView email = header.findViewById(R.id.showEmail);
        email.setText(SharedPref.getUserName(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            dialog.show();
            return true;
        } else if(id == R.id.action_edit) {
                SwitchFragment.addFragment(new UpdateProfile(),getSupportFragmentManager());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.nav_home:
                toolbar.setTitle("Job List");
                SwitchFragment.replaceFragment(new JobFeed(),getSupportFragmentManager());
            break;
            case R.id.nav_profile:
                toolbar.setTitle("My Profile");
                SwitchFragment.replaceFragment(new Profile(),getSupportFragmentManager());
            break;
            case R.id.nav_request:
                toolbar.setTitle("Job Status");
                SwitchFragment.replaceFragment(new Request(),getSupportFragmentManager());
            break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    public void showJobDetail(int id) {
        JobDetails jd = new JobDetails();
        jd.setJobId(id);
        SwitchFragment.addFragment(jd,getSupportFragmentManager());
    }
}
