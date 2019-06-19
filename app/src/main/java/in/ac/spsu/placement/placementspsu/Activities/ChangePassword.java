package in.ac.spsu.placement.placementspsu.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassword extends BaseActivity {
    private TextInputLayout admEmail, admPassword;
    private Button admSubmit;
    private ProgressDialog progressDialog;
    private Server server;
    private Toolbar toolbar;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            setSupportActionBar(toolbar);

            setSupportActionBar(toolbar);
            toolbar.setTitle("Change Password");

            final Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }

        admEmail = findViewById(R.id.admEmail);
        admPassword = findViewById(R.id.admPassword);
        admSubmit = findViewById(R.id.admSubmit);
        server = Client.getClient().create(Server.class);
        progressDialog = new ProgressDialog(ChangePassword.this);
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.setCancelable(false);

        builder = new AlertDialog.Builder(ChangePassword.this);
        builder.setCancelable(false);
        builder.setTitle(getString(R.string.action_logout)+" ?");
        builder.setMessage(getString(R.string.do_log));
        builder.setPositiveButton(getString(R.string.action_logout),new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPref.setUserName(getApplicationContext(),"");
                SharedPref.setUserType(getApplicationContext(),9);
                dialog.dismiss();
                startActivity(new Intent(ChangePassword.this,LoginActivity.class));
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

        admSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = admEmail.getEditText().getText().toString();
                String password = admPassword.getEditText().getText().toString();
                if(email.equals("")||password.equals("")){
                    Toast.makeText(getApplicationContext(), getString(R.string.enter_all), Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.show();
                    if(CheckNetwork.isNetworkConnected(getApplicationContext())){
                        Call<String> call = server.changePassword(email,password);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                if(response.body().equals(getString(R.string.success))) {
                                    admEmail.getEditText().setText("");
                                    admPassword.getEditText().setText("");
                                    Toast.makeText(getApplicationContext(), getString(R.string.reg_cmp), Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(getApplicationContext(), getString(R.string.reg_fail), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), getString(R.string.something), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fabAdmin);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.developer, Snackbar.LENGTH_LONG).show();
            }
        });
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
            startActivity(new Intent(ChangePassword.this,ManageStudent.class));
            finish();
            return true;
        } else if (id == R.id.action_manage_2) {
            startActivity(new Intent(ChangePassword.this,ManageCompany.class));
            finish();
            return true;
        } else if (id == R.id.action_password) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
