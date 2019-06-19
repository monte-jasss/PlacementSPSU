package in.ac.spsu.placement.placementspsu.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import in.ac.spsu.placement.placementspsu.Helper.SharedPref;
import in.ac.spsu.placement.placementspsu.R;

public class Splash extends AppCompatActivity {
    private Animation splash, splash_a;
    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        splash = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.btm_scale);
        splash_a = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_rot);
        tv = findViewById(R.id.textView);
        iv = findViewById(R.id.imageView);

        tv.startAnimation(splash);
        iv.startAnimation(splash_a);

        splash.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(SharedPref.getUserName(getApplicationContext()).equals("")) {
                            startActivity(new Intent(Splash.this, LoginActivity.class));
                            finish();
                        } else {
                            int type = SharedPref.getUserType(getApplicationContext());
                            if(type==0){
                                startActivity(new Intent(Splash.this, Home.class));
                                finish();
                            }
                            else if(type==1){
                                startActivity(new Intent(Splash.this, Company.class));
                                finish();
                            }
                            else if(type==2){
                                startActivity(new Intent(Splash.this, Admin.class));
                                finish();
                            }
                            else{
                                SharedPref.setUserName(getApplicationContext(),"");
                                startActivity(new Intent(Splash.this, LoginActivity.class));
                                finish();
                            }
                        }
                    }
                },2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        splash_a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}