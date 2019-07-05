package com.bantu.lift.user;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bantu.lift.user.activity.LoginActivity;
import com.bantu.lift.user.constant.CommonMeathod;
import com.bantu.lift.user.constant.FunctionHelper;
import com.bantu.lift.user.fragment.DashboardFragment;
import com.bantu.lift.user.fragment.NotificationFragment;
import com.bantu.lift.user.interFace.FragmentInterface;
import com.bantu.lift.user.modelclass.FcmModel.FcmModelclass;
import com.bantu.lift.user.modelclass.LogoutModelclass.LogoutModelclass;
import com.bantu.lift.user.retrofit.ApiUtils;
import com.bantu.lift.user.retrofit.IRestInterfaces;
import com.bantu.lift.user.utils.SharedPreferenceConstants;
import com.bantu.lift.user.utils.SmoothActionBarDrawerToggle;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FragmentInterface {
    Dialog dialog;
    DrawerLayout drawer;
    private CoordinatorLayout coordinateLayout;
    private boolean doubleBackToExitPressedOnce = false;
    TextView name, mobileNo;
    public static TextView text_toolbarTitle;
    SharedPreferences sharedPreferences;
    ImageView notification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        coordinateLayout = findViewById(R.id.coordinateLayout);
        text_toolbarTitle = findViewById(R.id.text_toolbarTitle);
        notification = findViewById(R.id.notification);
        sharedPreferences = getApplication().getSharedPreferences(SharedPreferenceConstants.PREF, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(SharedPreferenceConstants.checkData, "0").apply();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle = new SmoothActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawer.getWindowToken(), 0);
                coordinateLayout.setTranslationX(slideOffset * drawerView.getWidth());
                drawer.bringChildToFront(drawerView);
                drawer.requestLayout();
            }
        };
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        addFragment(DashboardFragment.newInstance("", ""), "Dashboard");
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        navigationView.setNavigationItemSelectedListener(this);
        name = hView.findViewById(R.id.name);
        mobileNo = hView.findViewById(R.id.mobileNo);
        name.setText(sharedPreferences.getString(SharedPreferenceConstants.name, ""));
        mobileNo.setText(sharedPreferences.getString(SharedPreferenceConstants.mobile, ""));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getSupportFragmentManager().getBackStackEntryCount() >= 1) {
                    onBackPressed();
                } else {

                    if (drawer.isDrawerVisible(GravityCompat.START)) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        drawer.closeDrawer(GravityCompat.START);

                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        drawer.openDrawer(GravityCompat.START);
                    }
                }
            }
        });
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFragment(NotificationFragment.newInstance("", ""), "Notification");

            }
        });
        updateToken();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            addFragment(DashboardFragment.newInstance("", ""), "Dashboard");

        } else if (id == R.id.nav_search) {
            addFragment(DashboardFragment.newInstance("", ""), "Dashboard");

        } else if (id == R.id.nav_logout) {
            openDialog();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openDialog() {
        dialog = new Dialog(MainActivity.this);
        dialog.setCancelable(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_logout);
        TextView text = dialog.findViewById(R.id.text);
        Button btn_yes = dialog.findViewById(R.id.btn_yes);
        Button btn_no = dialog.findViewById(R.id.btn_no);
        dialog.show();


        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                logoutUser();

            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                // getActivity().onBackPressed();
            }
        });
    }

    private void addFragment(Fragment fragment, final String title1) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, title1);
        fragmentTransaction.commit();
        // drawer.closeDrawer(GravityCompat.START);
        //imgTitle.setImageResource(R.drawable.toolbarimage);
    }

    private void addFragmentBackstack(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment, title);
        fragmentTransaction.addToBackStack(title);
        fragmentTransaction.commit();
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public void fragmentResult(Fragment fragment, String title) {
        addFragmentBackstack(fragment, title);
    }

    @Override
    public void onBackPressed() {
        View view = findViewById(android.R.id.content);
        if (sharedPreferences.getString(SharedPreferenceConstants.checkData,"").equalsIgnoreCase("1"))
        {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            super.onBackPressed();
        }else {if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                super.onBackPressed();
            } else if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Snackbar.make(view, "Press again to close", Snackbar.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {

                getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                super.onBackPressed();
            }
            // text_toolbarTitle.setText(getSupportFragmentManager().findFragmentById(R.id.frame_container).getTag());
        }}
        CommonMeathod.hideKeyboard(MainActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Integer buildVersion=sharedPreferences.getInt(SharedPreferenceConstants.buildVersion,0);
        Integer currentVersion=BuildConfig.VERSION_CODE;

        if(buildVersion==0||buildVersion<currentVersion)
        {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    public void logoutUser() {
        FunctionHelper.showDialog(MainActivity.this, "Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Call<LogoutModelclass> signInModelclassCall = iRestInterfaces.logoutUser(sharedPreferences.getString(SharedPreferenceConstants.serviceKey, ""), sharedPreferences.getString(SharedPreferenceConstants.userId, ""));
        signInModelclassCall.enqueue(new Callback<LogoutModelclass>() {
            @Override
            public void onResponse(Call<LogoutModelclass> call, Response<LogoutModelclass> response) {
                if (response.isSuccessful()) {
                    FunctionHelper.dismissDialog();

                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                        String refreshedToken = sharedPreferences.getString(SharedPreferenceConstants.fcmId, "");

                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SharedPreferenceConstants.email, "");
                        editor.putString(SharedPreferenceConstants.name, "");
                        editor.putString(SharedPreferenceConstants.serviceKey, "");
                        editor.putString(SharedPreferenceConstants.userId, "");
                        editor.putString(SharedPreferenceConstants.homeCity, "");
                        editor.putString(SharedPreferenceConstants.workCity, "");
                        editor.putString(SharedPreferenceConstants.mobile, "");
                        editor.putString(SharedPreferenceConstants.checkData, "");
                        editor.clear();
                        editor.commit();
                        sharedPreferences.edit().putString(SharedPreferenceConstants.fcmId, refreshedToken).apply();

                        Intent i1 = new Intent();
                        i1.setClassName("com.bantu.lift.user", "com.bantu.lift.user.activity.LoginActivity");
                        i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i1);


                    } else if (status_val == 2) {
                        FunctionHelper.dismissDialog();
                        String refreshedToken = sharedPreferences.getString(SharedPreferenceConstants.fcmId, "");

                        final SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(SharedPreferenceConstants.email, "");
                        editor.putString(SharedPreferenceConstants.name, "");
                        editor.putString(SharedPreferenceConstants.serviceKey, "");
                        editor.putString(SharedPreferenceConstants.userId, "");
                        editor.putString(SharedPreferenceConstants.homeCity, "");
                        editor.putString(SharedPreferenceConstants.workCity, "");
                        editor.putString(SharedPreferenceConstants.mobile, "");
                        editor.putString(SharedPreferenceConstants.checkData, "");
                        editor.clear();
                        editor.commit();
                        sharedPreferences.edit().putString(SharedPreferenceConstants.fcmId, refreshedToken).apply();
                        Intent i1 = new Intent();
                        i1.setClassName("com.bantu.lift.user", "com.bantu.lift.user.activity.LoginActivity");
                        i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i1);
                        // Toast.makeText(getApplicationContext(), response.body().getErrorMsg(), Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<LogoutModelclass> call, Throwable t) {
                FunctionHelper.dismissDialog();

            }
        });
    }

    public void add() {
        addFragment(DashboardFragment.newInstance("", ""), "Dashboard");

    }
    public void updateToken() {
        // FunctionHelper.showDialog(MainActivity.this, "Loading...");
        IRestInterfaces iRestInterfaces = ApiUtils.getAPIService();
        Call<FcmModelclass> signInModelclassCall = iRestInterfaces.updateToken(sharedPreferences.getString(SharedPreferenceConstants.fcmId, ""), sharedPreferences.getString(SharedPreferenceConstants.userId, ""));
        signInModelclassCall.enqueue(new Callback<FcmModelclass>() {
            @Override
            public void onResponse(Call<FcmModelclass> call, Response<FcmModelclass> response) {
                if (response.isSuccessful()) {
                    //  FunctionHelper.dismissDialog();
                    int status_val = Integer.parseInt(response.body().getErrorCode());
                    if (status_val == 0) {
                        //String refreshedToken = sharedPreferences.getString(SharedPreferenceConstants.fcmId, "");
                      //  Toast.makeText(MainActivity.this, "hehhe", Toast.LENGTH_SHORT).show();
                    } else if (status_val == 1) {

                    }
                }
            }

            @Override
            public void onFailure(Call<FcmModelclass> call, Throwable t) {
                // FunctionHelper.dismissDialog();

            }
        });
    }

}
