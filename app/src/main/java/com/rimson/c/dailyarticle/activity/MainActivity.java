package com.rimson.c.dailyarticle.activity;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.rimson.c.dailyarticle.R;
import com.rimson.c.dailyarticle.adapter.ContentPagerAdapter;
import com.rimson.c.dailyarticle.broadcast.NetworkChangeReceiver;
import com.rimson.c.dailyarticle.fragment.ArticleFragment;
import com.rimson.c.dailyarticle.fragment.CollectionFragment;
import com.rimson.c.dailyarticle.fragment.VoiceFragment;

import java.util.ArrayList;
import java.util.Objects;

import static com.rimson.c.dailyarticle.activity.VoiceActivity.notificationManager;


public class MainActivity extends AppCompatActivity {

    private String[] titles = new String[]{"文章", "声音", "收藏"};

    private ArrayList<Fragment> fragments = new ArrayList<>();

    private NetworkChangeReceiver networkChangeReceiver;

    private long exitTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        checkNetwork();
    }

    /**
     * 初始化布局
     */
    private void initViews() {
        setTitle("每日一文");

        ViewPager viewPager = findViewById(R.id.viewPager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);

        ArticleFragment articleFragment = new ArticleFragment();
        VoiceFragment voiceFragment = new VoiceFragment();
        CollectionFragment collectionFragment = new CollectionFragment();
        fragments.add(articleFragment);
        fragments.add(voiceFragment);
        fragments.add(collectionFragment);
        ContentPagerAdapter adapter = new ContentPagerAdapter(getSupportFragmentManager(), fragments, titles);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < 3; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(tabLayout.getTabAt(i)).setText(titles[i]);
            }
        }

    }

    private void checkNetwork() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    // 实现再按一次退出应用
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出应用", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            unregisterReceiver(networkChangeReceiver);
            notificationManager.cancelAll();
            finish();
            System.exit(0);
        }
    }

}
