package in.ac.spsu.placement.placementspsu.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.spsu.placement.placementspsu.Fragments.Application;
import in.ac.spsu.placement.placementspsu.Fragments.Candidate;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.Helper.SwitchFragment;
import in.ac.spsu.placement.placementspsu.R;

public class MainActivity extends BaseActivity implements Application.OnShowCandidateProfile {
    private Toolbar toolbar;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            setSupportActionBar(toolbar);

            setSupportActionBar(toolbar);
            toolbar.setTitle("Requests");

            final Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }

        String temp = getIntent().getExtras().getString("id");

        SwitchFragment.replaceFragment(newInstance(Integer.parseInt(temp)),getSupportFragmentManager());

        builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.action_logout)+" ?");
        builder.setMessage(getString(R.string.do_log));
        builder.setPositiveButton(getString(R.string.action_logout),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPref.setUserName(getApplicationContext(),"");
                SharedPref.setUserType(getApplicationContext(),9);
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
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

        FloatingActionButton fab = findViewById(R.id.fabMain);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.developer, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            dialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showCandidateProfile(int std_id,int id) {
        Candidate cd = new Candidate();
        cd.setId(std_id,id);
        SwitchFragment.addFragment(cd,getSupportFragmentManager());
    }

    public static Application newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id",id);
        Application fragment = new Application();
        fragment.setArguments(args);
        return fragment;
    }
}
