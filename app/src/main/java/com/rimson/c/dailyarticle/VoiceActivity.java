package com.rimson.c.dailyarticle;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rimson.c.dailyarticle.entity.Voice;

public class VoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice);

        initViews();
    }

    private void initViews(){
        setTitle("播放");
        final Voice voice=getIntent().getParcelableExtra("VOICE");

        ImageView imageView=(ImageView)findViewById(R.id.voiceImg);
        TextView title=(TextView)findViewById(R.id.voiceTitle);
        TextView author=(TextView)findViewById(R.id.voiceAuthor);

        Glide
                .with(getApplicationContext())
                .load(voice.getImgURL())
                .apply(new RequestOptions()
                .fitCenter())
                .into(imageView);

        title.setText(voice.getTitle());
        author.setText(voice.getAuthor());
    }
}
