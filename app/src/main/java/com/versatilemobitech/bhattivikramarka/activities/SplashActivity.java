package com.versatilemobitech.bhattivikramarka.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.utils.PopUtils;
import com.versatilemobitech.bhattivikramarka.utils.UserDetails;

public class SplashActivity extends BaseActivity {
    private String pageNo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initComponents();
    }

    private void initComponents() {
        setSplashScreen();
        getBundleData();
    }

    private void getBundleData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("page_no")) {
            UserDetails.getInstance(this).setPushPageNo(intent.getStringExtra("page_no"));
            pageNo = intent.getStringExtra("page_no");
        }
    }

    private void setSplashScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PopUtils.checkInternetConnection(SplashActivity.this)) {
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    intent.putExtra("page_no", pageNo);
                    navigateActivity(intent, true);
                } else {
                    PopUtils.alertDialog(SplashActivity.this, getString(R.string.pls_check_internet), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            }
        }, 2000);
    }
}
