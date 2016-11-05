package com.zkjinshi.braceletmanager.Pages;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.zkjinshi.braceletmanager.R;
import com.zkjinshi.braceletmanager.common.http.EndpointHelper;
import com.zkjinshi.braceletmanager.common.http.HttpLoadingCallback;
import com.zkjinshi.braceletmanager.common.http.OkHttpHelper;
import com.zkjinshi.braceletmanager.models.PatientVo;
import com.zkjinshi.braceletmanager.response.BaseResponse;
import com.zkjinshi.braceletmanager.models.SOSMessage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import okhttp3.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Fragment mFragment;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "添加病人手环", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(MainActivity.this, PatientRegisterActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentManager manager = getFragmentManager();
        mFragment = manager.findFragmentById(R.id.fragmentContainer);

        if (mFragment == null) {
            mFragment = new PatientsFragment();
            manager.beginTransaction()
                    .add(R.id.fragmentContainer, mFragment)
                    .commit();
        }

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_patients) {
            mFragment = new PatientsFragment();
            fab.show();
        } else if (id == R.id.nav_bracelets) {
            mFragment = new BraceletsFragment();
            fab.hide();
        } else if (id == R.id.nav_setting) {
            mFragment = new SettingFragment();
            fab.hide();
        } else if (id == R.id.nav_about) {
            mFragment = new AboutFragment();
            fab.hide();
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, mFragment)
                .commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Subscribe( threadMode = ThreadMode.MAIN)
    public void onBus(SOSMessage msg){
        //Toast.makeText(this,msg.getAlert(),Toast.LENGTH_LONG).show();
        showMsgAlert(msg);
    }

    @Subscribe( threadMode = ThreadMode.MAIN)
    public void onBus(PatientVo vo){
        Toast.makeText(this,vo.getPatientName(),Toast.LENGTH_LONG).show();
    }

    private void showMsgAlert(final SOSMessage msg) {
        new AlertDialog.Builder(this)
                .setTitle(msg.getMessage())
                .setMessage(msg.getAddress())
                .setPositiveButton(R.string.answer_sos, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        answerSOS(msg);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void answerSOS(SOSMessage msg) {
        HashMap<String, String> params = new HashMap<>();
        params.put("apid", msg.getApid());
        params.put("response","");

        OkHttpHelper.getInstance().put(EndpointHelper.answerSOS(), params, new HttpLoadingCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(Response response, BaseResponse result) {
                if (result.getStatus().equals("ok")) {
                    Toast.makeText(MainActivity.this, "发送应答成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "发送应答失败，请检查网络是否畅通", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Intent intent = new Intent(this, PatientTrackActivity.class);
        intent.putExtra("sos", msg);
        startActivity(intent);
    }
}
