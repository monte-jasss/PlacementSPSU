package in.ac.spsu.placement.placementspsu.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import in.ac.spsu.placement.placementspsu.Helper.CheckNetwork;
import in.ac.spsu.placement.placementspsu.R;
import in.ac.spsu.placement.placementspsu.Server.Client;
import in.ac.spsu.placement.placementspsu.Server.Server;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activation extends BaseActivity {
    private Toolbar toolbar;
    private Button activate;
    TextView resend,timer;
    private TextInputLayout activateOtp;
    private Server server;
    private ProgressDialog progressDialog;
    private long count = 30000;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);

        toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            setSupportActionBar(toolbar);

            setSupportActionBar(toolbar);
            toolbar.setTitle("Activate Account");

            final Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        }

        activate = findViewById(R.id.activate);
        resend = findViewById(R.id.resend);
        timer = findViewById(R.id.timer);
        activateOtp = findViewById(R.id.activationOtp);
        server = Client.getClient().create(Server.class);
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage(getString(R.string.verifying));
        progressDialog.setCancelable(false);

        final String email = getIntent().getExtras().getString("email");

        resendLinkEnable();

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp = activateOtp.getEditText().getText().toString().trim();
                if(tmp.equals("")){
                    Snackbar.make(view, R.string.enter_otp, Snackbar.LENGTH_LONG).show();
                } else {
                    int otp = Integer.parseInt(tmp);
                    if(CheckNetwork.isNetworkConnected(getApplicationContext())){
                        //progressDialog.show();
                        Call<String> call = server.getOTPVerification(email,otp);
                        call.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                String data =response.body();
                                if(data.equals(getString(R.string.success))){
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(Activation.this, getString(R.string.in_otp), Toast.LENGTH_SHORT).show();
                                }
                                //progressDialog.dismiss();
                            }

                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                                Toast.makeText(Activation.this, R.string.something, Toast.LENGTH_SHORT).show();
                                //progressDialog.dismiss();
                            }
                        });
                    } else {
                        Toast.makeText(Activation.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetwork.isNetworkConnected(getApplicationContext())){
                    resend.setVisibility(View.GONE);
                    Call<String> call = server.resendOTP(email);
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            String data =response.body();
                            if(data.equals(getString(R.string.success))){
                                Toast.makeText(Activation.this, getString(R.string.activate_msg), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(Activation.this, getString(R.string.fld_otp), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(Activation.this, R.string.something, Toast.LENGTH_SHORT).show();
                        }
                    });
                    resendLinkEnable();
                }  else {
                    Toast.makeText(Activation.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resendLinkEnable() {
        timer.setVisibility(View.VISIBLE);
        countDownTimer = new CountDownTimer(count,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int t = (int) (millisUntilFinished/1000);
                if(t<10)
                    timer.setText("00:0"+t);
                else
                    timer.setText("00:"+t);
            }

            @Override
            public void onFinish() {
                resend.setText(getString(R.string._resend));
                timer.setVisibility(View.GONE);
                resend.setVisibility(View.VISIBLE);
            }
        }.start();
    }
}
