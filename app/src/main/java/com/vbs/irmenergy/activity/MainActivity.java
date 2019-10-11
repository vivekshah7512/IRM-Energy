package com.vbs.irmenergy.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.vbs.irmenergy.R;
import com.vbs.irmenergy.fragments.FragmentChangePassword;
import com.vbs.irmenergy.fragments.FragmentCustomerRegistration;
import com.vbs.irmenergy.fragments.FragmentDashboard;
import com.vbs.irmenergy.fragments.FragmentSearch;
import com.vbs.irmenergy.utilities.Constant;
import com.vbs.irmenergy.utilities.Utility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String BACK_STACK_ROOT_TAG = "main_fragment";
    private Context mContext;
    private Fragment fr = null;
    private FragmentManager fm;
    private FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
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

        fr = new FragmentDashboard();
        if (fr != null) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, fr);
            fragmentTransaction.commit();
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
            Utility.writeSharedPreferences(mContext, "searchFlag", "estimation");
            if (fr != null) {
                fm = getSupportFragmentManager();
                fm.popBackStack(BACK_STACK_ROOT_TAG, 1);
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container, this.fr);
                fragmentTransaction.addToBackStack(BACK_STACK_ROOT_TAG);
                fragmentTransaction.commit();
            }
        } else if (id == R.id.nav_estimation) {
            fr = new FragmentSearch();
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
            fr = new FragmentSearch();
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
            fr = new FragmentSearch();
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
            Utility.writeSharedPreferences(mContext, "searchFlag", "survey");
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
}
