package cl.magnet.playmagnet.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

import cl.magnet.playmagnet.R;

public class AudioDetailActivity extends AppCompatActivity {

    private TextView mAudioTitleTextView;
    private TextView mAudioCommentTextView;
    private ImageView mAudioPlayImageView;
    private ImageView mAudioPauseImageView;
    MediaPlayer mediaplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarTitleColor));
        toolbar.setTitle("Detalle de audio");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);
//        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        mAudioTitleTextView = (TextView) findViewById(R.id.tv_audio_title);
        mAudioCommentTextView = (TextView) findViewById(R.id.tv_audio_comment);
        mAudioPlayImageView = (ImageView) findViewById(R.id.iv_play_audio);
        mAudioPauseImageView = (ImageView) findViewById(R.id.iv_pause_audio);
        String title =  getIntent().getExtras().getString("audioTitle");
        final String url =  getIntent().getExtras().getString("audioUrl");
        String comment =  getIntent().getExtras().getString("audioComment");

        if (title != null && title.length() > 0 ) {
            mAudioTitleTextView.setText(title);
        }
        if (comment != null && comment.length() > 0 ) {

            mAudioCommentTextView.setText(comment);
        } else {
            mAudioCommentTextView.setVisibility(View.GONE);
        }

        mediaplayer = new MediaPlayer();
        mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        mAudioPlayImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    mediaplayer.setDataSource(url);
                    mediaplayer.prepare();


                } catch (IllegalArgumentException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (SecurityException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                mediaplayer.start();


            }

        });

        mAudioPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaplayer.pause();
            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaplayer.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mediaplayer.stop();
    }
}
