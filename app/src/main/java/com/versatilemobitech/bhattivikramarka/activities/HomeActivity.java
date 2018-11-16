package com.versatilemobitech.bhattivikramarka.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.versatilemobitech.bhattivikramarka.R;
import com.versatilemobitech.bhattivikramarka.fragments.AudioSongsFragment;
import com.versatilemobitech.bhattivikramarka.fragments.ComplaintBoxFragment;
import com.versatilemobitech.bhattivikramarka.fragments.ContactUsFragment;
import com.versatilemobitech.bhattivikramarka.fragments.DailyActivitiesFragment;
import com.versatilemobitech.bhattivikramarka.fragments.GalleryFragment;
import com.versatilemobitech.bhattivikramarka.fragments.HomeFragment;
import com.versatilemobitech.bhattivikramarka.fragments.ProfileFragment;
import com.versatilemobitech.bhattivikramarka.fragments.VideosFragment;
import com.versatilemobitech.bhattivikramarka.interfaces.IParseListener;
import com.versatilemobitech.bhattivikramarka.utils.PopUtils;
import com.versatilemobitech.bhattivikramarka.utils.UserDetails;
import com.versatilemobitech.bhattivikramarka.webUtils.ServerResponse;
import com.versatilemobitech.bhattivikramarka.webUtils.WebServices;
import com.versatilemobitech.bhattivikramarka.webUtils.WsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;

public class HomeActivity extends BaseActivity implements View.OnClickListener, IParseListener {
    public TextView mTxtTitle, mTxtName, mTxtCaption;
    public ImageView mImgMenu, mImgBack, mImgSettings, mImgCelebrity;
    private FrameLayout mFrameLayout;
    private DrawerLayout mDrawer;
    public NavigationView mNavigationView;
    private Bundle mBundle;
    PopupMenu popup;
    private Intent intent;

    BroadcastReceiver mPushInstanceIdReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("REGISTRATION_COMPLETE")) {
                Log.e("the token is", intent.getStringExtra("token"));
                UserDetails.getInstance(HomeActivity.this).setDeviceToken(intent.getStringExtra("token"));

                if (PopUtils.checkInternetConnection(HomeActivity.this)) {
                    requestForDeviceToken();
                } else {
                    PopUtils.alertDialog(HomeActivity.this, getString(R.string.pls_check_internet), null);
                }
            }
        }
    };

    private void requestForDeviceToken() {
        showLoadingDialog("Loading", false);
        HashMap<String, String> params = new HashMap<>();
        params.put("token_id", UserDetails.getInstance(this).getDeviceToken());
        ServerResponse serverResponse = new ServerResponse();
        serverResponse.serviceRequest(this, WebServices.URL_DEVICE_TOKEN, params, this, WsUtils.WS_DEVICE_TOKEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mPushInstanceIdReceiver, new IntentFilter("REGISTRATION_COMPLETE"));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPushInstanceIdReceiver);
        super.onPause();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initComponents();
    }

    private void initComponents() {
        setReferences();
        setClickListeners();
        getBundleData();
        setSideBarNavigation();

        popup = new PopupMenu(this, mImgSettings);
        MenuInflater inflate = popup.getMenuInflater();
        inflate.inflate(R.menu.menu_language, popup.getMenu());

        popup.getMenu().getItem(0).setChecked(true);
        if (UserDetails.getInstance(this).getLanguage().equalsIgnoreCase("en")) {
            popup.getMenu().getItem(0).setChecked(true);
        } else if (UserDetails.getInstance(this).getLanguage().equalsIgnoreCase("te")) {
            popup.getMenu().getItem(1).setChecked(true);
        } else if (UserDetails.getInstance(this).getLanguage().equalsIgnoreCase("hi")) {
            popup.getMenu().getItem(2).setChecked(true);
        }

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.itemTelugu:
                        UserDetails.getInstance(HomeActivity.this).setLanguage("te");
                        getLocale("te");
                        return true;
                    case R.id.itemEnglish:
                        UserDetails.getInstance(HomeActivity.this).setLanguage("en");
                        getLocale("en");
                        return true;
                    case R.id.itemHindi:
                        UserDetails.getInstance(HomeActivity.this).setLanguage("hi");
                        getLocale("hi");
                        return true;
                    default:
                        UserDetails.getInstance(HomeActivity.this).setLanguage("en");
                        getLocale("en");
                        break;
                }
                return false;
            }
        });
    }

    private void getBundleData() {
        intent = getIntent();
        if (UserDetails.getInstance(this).getPushPageNo().equalsIgnoreCase("1")) {
            UserDetails.getInstance(this).setPushPageNo("");
            mNavigationView.getMenu().getItem(3).setChecked(true);
            mBundle = new Bundle();
            UserDetails.getInstance(this).setPushPageNo("");
            navigateFragment(GalleryFragment.newInstance(), "GALLERYFRAGMENT", mBundle, HomeActivity.this);
        } else if (UserDetails.getInstance(this).getPushPageNo().equalsIgnoreCase("2")) {
            UserDetails.getInstance(this).setPushPageNo("");
            mNavigationView.getMenu().getItem(4).setChecked(true);
            mBundle = new Bundle();
            navigateFragment(VideosFragment.newInstance(), "VIDEOSFRAGMENT", mBundle, HomeActivity.this);
        } else if (UserDetails.getInstance(this).getPushPageNo().equalsIgnoreCase("3")) {
            UserDetails.getInstance(this).setPushPageNo("");
            mNavigationView.getMenu().getItem(5).setChecked(true);
            mBundle = new Bundle();
            mBundle.putString("from", "1");
            navigateFragment(DailyActivitiesFragment.newInstance(), "SOCIALSERVICEFRAGMENT", mBundle, HomeActivity.this);
        } else {
            setHomeFragment();
        }
    }

    public void getLocale(String languageCode) {
        Configuration config = getBaseContext().getResources().getConfiguration();
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        recreate();
    }

    private void setReferences() {
        mTxtTitle = (TextView) findViewById(R.id.txtTitle);
        mImgMenu = (ImageView) findViewById(R.id.imgMenu);
        mImgBack = (ImageView) findViewById(R.id.imgBack);
        mImgSettings = (ImageView) findViewById(R.id.imgSettings);
        mFrameLayout = (FrameLayout) findViewById(R.id.container);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) findViewById(R.id.navigationView);

       /* View mHeaderView = mNavigationView.getHeaderView(0);
        mTxtName = (TextView) mHeaderView.findViewById(R.id.txtName);
        mTxtCaption = (TextView) mHeaderView.findViewById(R.id.txtCaption);
        mImgCelebrity = (ImageView) mHeaderView.findViewById(R.id.imgCelebrity);*/
    }

    private void setClickListeners() {
        mImgMenu.setOnClickListener(this);
        mImgBack.setOnClickListener(this);
        mImgSettings.setOnClickListener(this);
    }

    private void setSideBarNavigation() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                mDrawer.closeDrawer(GravityCompat.START);
                if (id == R.id.itemhome) {
                    mNavigationView.getMenu().getItem(0).setChecked(true);
                    mBundle = new Bundle();
                    navigateFragment(HomeFragment.newInstance(), "HOMEFRAGMENT", mBundle, HomeActivity.this);
                } else if (id == R.id.itemprofile) {
                    mNavigationView.getMenu().getItem(1).setChecked(true);
                    mBundle = new Bundle();
                    navigateFragment(ProfileFragment.newInstance(), "PROFILEFRAGMENT", mBundle, HomeActivity.this);
                } else if (id == R.id.itemgallery) {
                    mNavigationView.getMenu().getItem(2).setChecked(true);
                    mBundle = new Bundle();
                    navigateFragment(GalleryFragment.newInstance(), "GALLERYFRAGMENT", mBundle, HomeActivity.this);
                } else if (id == R.id.itemvideo) {
                    mNavigationView.getMenu().getItem(3).setChecked(true);
                    mBundle = new Bundle();
                    navigateFragment(VideosFragment.newInstance(), "VIDEOSFRAGMENT", mBundle, HomeActivity.this);
                } else if (id == R.id.itemdailyactivities) {
                    mNavigationView.getMenu().getItem(4).setChecked(true);
                    mBundle = new Bundle();
                    mBundle.putString("from", "0");
                    navigateFragment(DailyActivitiesFragment.newInstance(), "SOCIALSERVICEFRAGMENT", mBundle, HomeActivity.this);
                } else if (id == R.id.itemComplaintbox) {
                    mNavigationView.getMenu().getItem(5).setChecked(true);
                    mBundle = new Bundle();
                    navigateFragment(ComplaintBoxFragment.newInstance(), "NEEDHELPFRAGMENT", mBundle, HomeActivity.this);
                } /*else if (id == R.id.itemaudiosongs) {
                    mNavigationView.getMenu().getItem(6).setChecked(true);
                    mBundle = new Bundle();
                    navigateFragment(AudioSongsFragment.newInstance(), "AUDIOSONGSFRAGMENT", mBundle, HomeActivity.this);
                }*/ else if (id == R.id.itemcontactus) {
                    mNavigationView.getMenu().getItem(6).setChecked(true);
                    mBundle = new Bundle();
                    navigateFragment(ContactUsFragment.newInstance(), "CONTACTUSFRAGMENT", mBundle, HomeActivity.this);
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgMenu:
                if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                    mDrawer.closeDrawer(GravityCompat.START);
                } else {
                    mDrawer.openDrawer(GravityCompat.START);
                }
                break;
            case R.id.imgBack:
                onBackPressed();
                break;
            case R.id.imgSettings:
                popup.show();
                break;
            default:
                break;
        }
    }

    private void setHomeFragment() {
        mNavigationView.getMenu().getItem(0).setChecked(true);
        mBundle = new Bundle();
        navigateFragment(HomeFragment.newInstance(), "HOMEFRAGMENT", mBundle, this);
    }

    View.OnClickListener yesClick = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View view) {
            HomeActivity.this.finishAffinity();
        }
    };

    public static void navigateFragment(Fragment fragment, String tag, Bundle bundle, FragmentActivity fragmentActivity) {
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag);
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        if (tag != null) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
                String tagName = backEntry.getName();
                if (tagName.equals("HOMEFRAGMENT")) {
                    PopUtils.twoButtonDialog(this, getString(R.string.are_u_sure_u_want_to_exit), "YES", "NO", yesClick, null);
                } else if (tagName.equals("PROFILEFRAGMENT")) {
                    setHomeFragment();
                } else if (tagName.equals("FOLLOWUSFRAGMENT")) {
                    setHomeFragment();
                } else if (tagName.equals("GALLERYFRAGMENT")) {
                    setHomeFragment();
                } else if (tagName.equals("VIDEOSFRAGMENT")) {
                    setHomeFragment();
                } else if (tagName.equals("SOCIALSERVICEFRAGMENT")) {
                    setHomeFragment();
                } else if (tagName.equals("CONTACTUSFRAGMENT")) {
                    setHomeFragment();
                } else if (tagName.equals("NEEDHELPFRAGMENT")) {
                    setHomeFragment();
                } else if (tagName.equals("AUDIOSONGSFRAGMENT")) {
                    setHomeFragment();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public void ErrorResponse(VolleyError error, int requestCode) {
        hideLoadingDialog();
        PopUtils.alertDialog(this, getString(R.string.something_wrong), null);
    }

    @Override
    public void SuccessResponse(String response, int requestCode) {
        hideLoadingDialog();
        switch (requestCode) {
            case WsUtils.WS_DEVICE_TOKEN:
                responseForDeviceToken(response);
                break;
            default:
                break;
        }
    }

    private void responseForDeviceToken(String response) {
        if (response != null) {
            try {
                JSONObject mJsonObject = new JSONObject(response);
                String message = mJsonObject.optString("message");
                if (mJsonObject.getString("status").equalsIgnoreCase("200")) {
                } else {
                    Toast.makeText(this, "Device Token is not updated", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
