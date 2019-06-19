package in.ac.spsu.placement.placementspsu.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import in.ac.spsu.placement.placementspsu.R;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        hideKeyboard();
    }

    private void hideKeyboard(){

    }
}