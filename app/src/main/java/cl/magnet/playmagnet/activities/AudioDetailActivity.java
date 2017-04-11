package cl.magnet.playmagnet.activities;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

import cl.magnet.playmagnet.R;

public class AudioDetailActivity extends AppCompatActivity {

    private TextView mAudioTitleTextView;
    private TextView mAudioCommentTextView;
    private ImageButton mAudioPlayImageView;
    private ImageButton mAudioPauseImageView;
    MediaPlayer mediaplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(ContextCompat.getColor(getApplicationContext(), R.color.toolbarTitleColor));
        toolbar.setTitle("Detalle de audio");

        mAudioTitleTextView = (TextView) findViewById(R.id.tv_audio_title);
        mAudioCommentTextView = (TextView) findViewById(R.id.tv_audio_comment);
        mAudioPlayImageView = (ImageButton) findViewById(R.id.iv_play_audio);
        mAudioPauseImageView = (ImageButton) findViewById(R.id.iv_pause_audio);
        mAudioPauseImageView.setEnabled(false);

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

                mAudioPlayImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_audio_dark));
                mAudioPauseImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pause_audio_light));
                mAudioPlayImageView.setEnabled(false);
                mAudioPauseImageView.setEnabled(true);
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

                Snackbar.make(view, "Reproduciendo Audio", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();


            }

        });

        mAudioPauseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaplayer.pause();
                mAudioPlayImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_play_audio_light));
                mAudioPauseImageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_pause_audio_dark));
                mAudioPlayImageView.setEnabled(true);
                mAudioPauseImageView.setEnabled(false);
                Snackbar.make(view, "Audio en Pausa", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

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
