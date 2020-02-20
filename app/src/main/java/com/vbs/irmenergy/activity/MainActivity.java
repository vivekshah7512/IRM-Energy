package com.vbs.irmenergy.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.vbs.irmenergy.R;
import com.vbs.irmenergy.fragments.FragmentChangePassword;
import com.vbs.irmenergy.fragments.FragmentCommissioning;
import com.vbs.irmenergy.fragments.FragmentCustomerRegistration;
import com.vbs.irmenergy.fragments.FragmentDashboard;
import com.vbs.irmenergy.fragments.FragmentExtraMaterialEstimation;
import com.vbs.irmenergy.fragments.FragmentJobsheetSearch;
import com.vbs.irmenergy.fragments.FragmentProfile;
import com.vbs.irmenergy.fragments.FragmentSearch;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.GPSTracker1;
import com.vbs.irmenergy.utilities.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String BACK_STACK_ROOT_TAG = "main_fragment";
    private Context mContext;
    private Fragment fr = null;
    private FragmentManager fm;
    private FragmentTransaction fragmentTransaction;
    private TextView tv_name;
    private GPSTracker1 gpsTracker1;
    private double latitude, longitude;
    private Handler handler;
    private Runnable runnable;
    private GoogleApiClient mGoogleApiClient;
    private LinearLayout ll_profile;
    private String[] screen_name, view;
    private JSONArray jsonArray;
    private JSONObject jsonObjectMessage;
    private MenuItem menuItem;
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mContext = this;
        gpsTracker1 = new GPSTracker1(MainActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.menu);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });


        View hView = navigationView.getHeaderView(0);
        tv_name = (TextView) hView.findViewById(R.id.tv_menu_name);
        tv_name.setText(Utility.getAppPrefString(mContext, Constant.USER_NAME));
        ll_profile = (LinearLayout) hView.findViewById(R.id.ll_profile);
        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                fr = new FragmentProfile();
                if (fr != null) {
                    fm = getSupportFragmentManager();
                    fm.popBackStack(BACK_STACK_ROOT_TAG, 1);
                    fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.container, fr);
                    fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG);
                    fragmentTransaction.commit();
                }
            }
        });

        try {
            String jsonArray1 = Utility.getAppPrefString(mContext, "menuArray");
            jsonArray = new JSONArray(jsonArray1);
            int lenth = jsonArray.length();
            screen_name = new String[lenth];
            view = new String[lenth];
            for (int a = 0; a < lenth; a++) {
                jsonObjectMessage = jsonArray.getJSONObject(a);
                screen_name[a] = jsonObjectMessage.getString("screen_name");
                view[a] = jsonObjectMessage.getString("view");
            }
            if (screen_name.length > 0) {
                for (int a = 0; a < screen_name.length; a++) {
                    String title = screen_name[a];
                    String isShow = view[a];
                    if (title.equalsIgnoreCase("Normal Registration")) {
                        if (isShow.equalsIgnoreCase("true")) {
                            menuItem = menu.findItem(R.id.nav_reg);
                            menuItem.setVisible(true);
                        } else {
                            menuItem = menu.findItem(R.id.nav_reg);
                            menuItem.setVisible(false);
                        }
                    } else if (title.equalsIgnoreCase("Job Sheet Generation")) {
                        if (isShow.equalsIgnoreCase("true")) {
                            menuItem = menu.findItem(R.id.nav_jobsheet);
                            menuItem.setVisible(true);
                        } else {
                            menuItem = menu.findItem(R.id.nav_jobsheet);
                            menuItem.setVisible(false);
                        }
                    } else if (title.equalsIgnoreCase("Commissioning Process")) {
                        if (isShow.equalsIgnoreCase("true")) {
                            menuItem = menu.findItem(R.id.nav_commission);
                            menuItem.setVisible(true);
                        } else {
                            menuItem = menu.findItem(R.id.nav_commission);
                            menuItem.setVisible(false);
                        }
                    } else if (title.equalsIgnoreCase("Customer Survey")) {
                        if (isShow.equalsIgnoreCase("true")) {
                            menuItem = menu.findItem(R.id.nav_survey);
                            menuItem.setVisible(true);
                        } else {
                            menuItem = menu.findItem(R.id.nav_survey);
                            menuItem.setVisible(false);
                        }
                    } else if (title.equalsIgnoreCase("Extra Material Estimation")) {
                        if (isShow.equalsIgnoreCase("true")) {
                            menuItem = menu.findItem(R.id.nav_estimation);
                            menuItem.setVisible(true);
                        } else {
                            menuItem = menu.findItem(R.id.nav_estimation);
                            menuItem.setVisible(false);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        fr = new FragmentDashboard();
        if (fr != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fr);
            fragmentTransaction.commit();
        }

        if (gpsTracker1.canGetLocation()) {
            latitude = gpsTracker1.getLatitude();
            longitude = gpsTracker1.getLongitude();
            if (!String.valueOf(latitude).matches("0.0")) {
                try {
                    Log.v("Lat/Long: ", latitude + "\n" + longitude);
                    Utility.writeSharedPreferences(mContext, "latitude", latitude + "");
                    Utility.writeSharedPreferences(mContext, "longitude", longitude + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    handler = new Handler();
                    runnable = new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    latitude = gpsTracker1.getLatitude();
                                    longitude = gpsTracker1.getLongitude();
                                    if (!String.valueOf(latitude).matches("0.0")) {
                                        handler.removeCallbacks(runnable);
                                        onResume();
                                    } else {
                                        handler.postDelayed(runnable, 1000);
                                        gpsTracker1 = new GPSTracker1(MainActivity.this);
                                    }
                                }
                            });
                        }
                    };
                    handler.postDelayed(runnable, 1000);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            settingAlert();
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_dashboard) {
            fr = new FragmentDashboard();
            if (fr != null) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, fr);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_reg) {
            fr = new FragmentCustomerRegistration();
            Utility.writeSharedPreferences(this, "uid", "");
            Utility.writeSharedPreferences(mContext, "searchFlag", "cust_reg");
            if (fr != null) {
                fm = getSupportFragmentManager();
                fm.popBackStack(BACK_STACK_ROOT_TAG, 1);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, this.fr);
                fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_estimation) {
            fr = new FragmentExtraMaterialEstimation();
            Utility.writeSharedPreferences(mContext, "searchFlag", "estimation");
            if (fr != null) {
                fm = getSupportFragmentManager();
                fm.popBackStack(BACK_STACK_ROOT_TAG, 1);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, this.fr);
                fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_jobsheet) {
            fr = new FragmentJobsheetSearch();
            Utility.writeSharedPreferences(mContext, "searchFlag", "jobsheet");
            if (fr != null) {
                fm = getSupportFragmentManager();
                fm.popBackStack(BACK_STACK_ROOT_TAG, 1);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, this.fr);
                fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_commission) {
            fr = new FragmentCommissioning();
            Utility.writeSharedPreferences(mContext, "searchFlag", "commission");
            if (fr != null) {
                fm = getSupportFragmentManager();
                fm.popBackStack(BACK_STACK_ROOT_TAG, 1);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, this.fr);
                fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_survey) {
            fr = new FragmentSearch();
            Utility.writeSharedPreferences(mContext, "searchFlag", "survey");
            if (fr != null) {
                fm = getSupportFragmentManager();
                fm.popBackStack(BACK_STACK_ROOT_TAG, 1);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, this.fr);
                fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_change_pass) {
            fr = new FragmentChangePassword();
            Utility.writeSharedPreferences(mContext, "searchFlag", "change_pass");
            if (fr != null) {
                fm = getSupportFragmentManager();
                fm.popBackStack(BACK_STACK_ROOT_TAG, 1);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, this.fr);
                fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogTheme);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Utility.writeSharedPreferences(mContext, Constant.isLogin, "false");
                    finish();
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void settingAlert() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
            mGoogleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(1 * 1000);
            locationRequest.setFastestInterval(1 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            /*if (mProgressDialog1 != null) {
                                mProgressDialog1.dismiss();
                            }*/
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(MainActivity.this, 1);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
