package com.csci366.july2020.tasfique_enam.labtask3;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    TextView playerPosition, playerDuration, audioName;
    TextView volUp, volDown;
    SeekBar seekBar, volumeBar;
    ImageView btRew, btPlay, btPause, btFf, btStop, audioPicture;

    MediaPlayer mediaPlayer;
    Handler handler = new Handler();
    Runnable runnable;
    Uri uriAudio;
    Uri uri;
    String audioFileName;
    Boolean musicLoaded = false;
    String result;
    final int PICK_AUDIO_REQUEST = 123;
    Intent i;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Assigning Value

        playerPosition = findViewById(R.id.player_position);
        playerDuration = findViewById(R.id.player_duration);
        audioName = findViewById(R.id.audio_name);
        seekBar = findViewById(R.id.seek_bar);
        btRew = findViewById(R.id.bt_rr);
        btPlay = findViewById(R.id.bt_play);
        btPause = findViewById(R.id.bt_pause);
        btFf = findViewById(R.id.bt_ff);
        btStop = findViewById(R.id.bt_stop);
        audioPicture = findViewById(R.id.audio_picture);
        volumeBar = (SeekBar) findViewById(R.id.volumeBar);
        volUp = findViewById(R.id.vol_up);
        volDown = findViewById(R.id.vol_down);
        btPlay.setVisibility(View.INVISIBLE);
        
        btStop.setVisibility(View.INVISIBLE);
        btFf.setVisibility(View.INVISIBLE);
        btRew.setVisibility(View.INVISIBLE);

        seekBar.setVisibility(View.INVISIBLE);
        volumeBar.setVisibility(View.INVISIBLE);

        playerPosition.setVisibility(View.INVISIBLE);

        volUp.setVisibility(View.INVISIBLE);
        volDown.setVisibility(View.INVISIBLE);

        //initialise media player here for no media selected.
        mediaPlayer = null;


        //executing the runnable
        runnable = new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);

            }

        };

        //getting the duration of the music


        audioPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btPlay.setVisibility(View.VISIBLE);

                btStop.setVisibility(View.VISIBLE);
                btFf.setVisibility(View.VISIBLE);
                btRew.setVisibility(View.VISIBLE);

                seekBar.setVisibility(View.VISIBLE);
                volumeBar.setVisibility(View.VISIBLE);

                playerPosition.setVisibility(View.VISIBLE);

                volUp.setVisibility(View.VISIBLE);
                volDown.setVisibility(View.VISIBLE);

                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                    btPlay.setVisibility(View.VISIBLE);
                    btPause.setVisibility(View.GONE);
                    handler.removeCallbacks(runnable);
                    seekBar.setProgress(0);

                    mediaPlayer = null;
                    i = new Intent();

                    // SET THE TYPE FOR ONLY AUDIO FILES.
                    i.setType("audio/*");

                    // SET THE ACTION AND GET THE CONTENT OF THE IMAGE.
                    i.setAction(Intent.ACTION_GET_CONTENT);

                    // START THE ACTIVITY

                    startActivityForResult(Intent.createChooser(i, "Select Music"), PICK_AUDIO_REQUEST);

                } else {
                    handler.removeCallbacks(runnable);
                    seekBar.setProgress(0);

                    mediaPlayer = null;
                    i = new Intent();

                    // SET THE TYPE FOR ONLY AUDIO FILES.
                    i.setType("audio/*");

                    // SET THE ACTION AND GET THE CONTENT OF THE IMAGE.
                    i.setAction(Intent.ACTION_GET_CONTENT);

                    // START THE ACTIVITY

                    startActivityForResult(Intent.createChooser(i, "Select Music"), PICK_AUDIO_REQUEST);

                }


            }
        });

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btPlay.setVisibility(View.GONE);
                btPause.setVisibility(View.VISIBLE);
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.click);
                mp.start();

                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                handler.postDelayed(runnable, 0);
            }
        });

        btPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btPlay.setVisibility(View.VISIBLE);
                btPause.setVisibility(View.GONE);
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.click);
                mp.start();

                mediaPlayer.pause();
                handler.removeCallbacks(runnable);
            }
        });


        btStop.setOnClickListener(new View.OnClickListener() {

            private void stopPlaying() {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }

            @Override
            public void onClick(View v) {

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    //mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.error);

                    mediaPlayer = MediaPlayer.create(MainActivity.this, uriAudio);
                    btPlay.setVisibility(View.VISIBLE);
                    btPause.setVisibility(View.GONE);
                    MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.click);
                    mp.start();
                } else {
                    mediaPlayer.stop();
                    //mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.error);
                    mediaPlayer = MediaPlayer.create(MainActivity.this, uriAudio);
                    btPlay.setVisibility(View.VISIBLE);
                    btPause.setVisibility(View.GONE);
                    MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.click);
                    mp.start();
                    seekBar.setProgress(0);
                    playerDuration.setText("00:00");
                    playerPosition.setText("00:00");
                }

            }
        });

        btFf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.click);
                mp.start();
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && duration != currentPosition) {
                    currentPosition = currentPosition + 5000;
                    playerPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });

        btRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.click);
                mp.start();
                int currentPosition = mediaPlayer.getCurrentPosition();
                //int duration = mediaPlayer.getDuration();
                if (mediaPlayer.isPlaying() && currentPosition > 5000) {
                    currentPosition = currentPosition - 5000;
                    playerPosition.setText(convertFormat(currentPosition));
                    mediaPlayer.seekTo(currentPosition);
                }
            }
        });


        volumeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        float volumeNum = progress / 100f;
                        mediaPlayer.setVolume(volumeNum, volumeNum);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                        MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.seek);
                        mp.start();

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                }
        );

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if (b) {
                    mediaPlayer.seekTo(i);
                }

                playerPosition.setText(convertFormat(mediaPlayer.getCurrentPosition()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.seek);
                mp.start();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @SuppressLint("DefaultLocale")
    private String convertFormat(int duration) {
        return String.format("%02d:%02d"
                , TimeUnit.MILLISECONDS.toMinutes(duration)
                , TimeUnit.MILLISECONDS.toSeconds(duration) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uriAudio = data.getData();
            audioFileName = getFileName(uriAudio);
            mediaPlayer = MediaPlayer.create(this, uriAudio);

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    btPause.setVisibility(View.GONE);
                    btPlay.setVisibility(View.VISIBLE);
                    mediaPlayer.seekTo(0);
                    mediaPlayer.pause();
                    int duration = mediaPlayer.getDuration();
                    String sDuration = convertFormat(duration);
                    playerDuration.setText(sDuration);

//                    int duration2 = mediaPlayer.getDuration()-mediaPlayer.getCurrentPosition();
//                    playerDuration.setText(duration2);
                }
            });

            // SET THE AUDIO FILE NAME AND MAKE IMAGE LOADING STATUS TRUE.
            audioName.setText("\"" + audioFileName + "\"");
            musicLoaded = true;


        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}

