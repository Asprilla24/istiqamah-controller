package com.team.istiqomah.istiqomahcontroller;

import android.app.NotificationManager;
import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.team.istiqomah.istiqomahcontroller.adapter.viewPagerAdapter;
import com.team.istiqomah.istiqomahcontroller.helper.alarmHelper;
import com.team.istiqomah.istiqomahcontroller.helper.sholatSunnahRealmHelper;
import com.team.istiqomah.istiqomahcontroller.helper.waktuSholatRealmHelper;
import com.team.istiqomah.istiqomahcontroller.model.waktuSholatModel;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupToolbar();
        init();

        //Menghilangkan notifikasi yang ada
        removeNotification();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.action_updateLocation:break;
            case R.id.action_About:break;
        }

        return true;
    }

    private void init() {
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void removeNotification(){
        NotificationManager nManager = ((NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE));
        nManager.cancelAll();
    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setTabsFromPagerAdapter(viewPagerAdapter);
    }

    private void setupToolbar() {
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            //getSupportActionBar().setElevation(0);
        }
    }
}
