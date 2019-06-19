package in.ac.spsu.placement.placementspsu.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import in.ac.spsu.placement.placementspsu.Adapters.UserAdapter;
import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.Model.UserData;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManageCompany extends BaseActivity {
    private ListView listView;
    private TextView tv;
    private Server server;
    private ProgressDialog progressDialog,progress;
    private UserAdapter userAdapter;
    private SwipeRefreshLayout swipeStd;
    private Toolbar toolbar;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_company);

        toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            setSupportActionBar(toolbar);

            setSupportActionBar(toolbar);
            toolbar.setTitle("Manage Recruiter");

            final Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }

        swipeStd = findViewById(R.id.swipeStd);
        server = Client.getClient().create(Server.class);
        listView = findViewById(R.id.companyList);
        tv = findViewById(R.id.noReq);
        progressDialog = new ProgressDialog(ManageCompany.this);
        progressDialog.setMessage(getString(R.string.load));
        progressDialog.setCancelable(false);
        progress = new ProgressDialog(ManageCompany.this);
        progress.setMessage(getString(R.string.load));
        progress.setCancelable(false);
        userAdapter = new UserAdapter(getApplicationContext(),R.layout.request_style);
        listView.setAdapter(userAdapter);
        registerForContextMenu(listView);

        swipeStd.setColorSchemeColors(getResources().getColor(R.color.blueDark),getResources().getColor(R.color.colorPrimaryDark));

        swipeStd.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createStudentList();
                swipeStd.setRefreshing(false);
            }
        });

        createStudentList();

        builder = new AlertDialog.Builder(ManageCompany.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.action_logout)+" ?");
        builder.setMessage(getString(R.string.do_log));
        builder.setPositiveButton(getString(R.string.action_logout),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPref.setUserName(getApplicationContext(),"");
                SharedPref.setUserType(getApplicationContext(),9);
                dialog.dismiss();
                startActivity(new Intent(ManageCompany.this,LoginActivity.class));
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

        FloatingActionButton fab = findViewById(R.id.fabAdmin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.developer, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void createStudentList() {
        if(CheckNetwork.isNetworkConnected(getApplicationContext())){
            progressDialog.show();
            Call<List<UserData>> call = server.getAllUserByType(1);
            call.enqueue(new Callback<List<UserData>>() {
                @Override
                public void onResponse(Call<List<UserData>> call, Response<List<UserData>> response) {
                    userAdapter.clear();
                    List<UserData> data = response.body();
                    if(data.size()==0){
                        listView.setVisibility(View.GONE);
                        tv.setText(getString(R.string.recruiters_not_found));
                        tv.setVisibility(View.VISIBLE);
                    } else {
                        tv.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                        for(UserData item : data){
                            userAdapter.add(item);
                        }
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<List<UserData>> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    }

    private void deleteSelectedCompany(final String name, int id, final int pos) {
        if(CheckNetwork.isNetworkConnected(getApplicationContext())){
            progress.show();
            Call<String> call = server.doRemoveUser(id,1);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    String data = response.body();
                    if(data.equals(getString(R.string.success))){
                        userAdapter.remove(pos);
                        userAdapter.notifyDataSetChanged();
                        Toast.makeText(ManageCompany.this, name+" "+getString(R.string.removed), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ManageCompany.this, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(ManageCompany.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    progress.dismiss();
                }
            });
        } else {
            Toast.makeText(ManageCompany.this, getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            dialog.show();
            return true;
        } else if (id == R.id.action_manage) {
            startActivity(new Intent(ManageCompany.this,ManageStudent.class));
            finish();
            return true;
        } else if (id == R.id.action_manage_2) {
            return true;
        } else if (id == R.id.action_password) {
            startActivity(new Intent(ManageCompany.this,ChangePassword.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        this.getMenuInflater().inflate(R.menu.company, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.action_delete:
                TextView tv = info.targetView.findViewById(R.id.item_no);
                TextView tm = info.targetView.findViewById(R.id.item_1);
                deleteSelectedCompany(tm.getText().toString(),Integer.parseInt(tv.getText().toString()),info.position);
                break;
        }

        return super.onContextItemSelected(item);
    }
}
