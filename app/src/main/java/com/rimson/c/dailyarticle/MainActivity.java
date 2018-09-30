package com.rimson.c.dailyarticle;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.rimson.c.dailyarticle.fragment.ArticleFragment;
import com.rimson.c.dailyarticle.fragment.CollectionFragment;
import com.rimson.c.dailyarticle.fragment.VoiceFragment;
import com.rimson.c.dailyarticle.adapter.MyFragmentPagerAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private String[] titles= new String[]{"文章", "声音", "收藏"};

    private ArrayList<Fragment>fragments=new ArrayList<Fragment>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    /**
     * 初始化布局
     */
    private void initViews(){
        setTitle("每日一文");
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        TabLayout tabLayout=(TabLayout)findViewById(R.id.tab_layout);

        ArticleFragment articleFragment=new ArticleFragment();
        VoiceFragment voiceFragment=new VoiceFragment();
        CollectionFragment collectionFragment=new CollectionFragment();
        fragments.add(articleFragment);
        fragments.add(voiceFragment);
        fragments.add(collectionFragment);
        MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,titles);

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i=0;i<3;i++){
            tabLayout.getTabAt(i).setText(titles[i]);
        }

    }

}
