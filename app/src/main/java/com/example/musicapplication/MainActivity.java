package com.example.musicapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements SongChangeListener {
private  final List<musiclist> musiclistLists=new ArrayList<>();
private RecyclerView musicview;
private MediaPlayer mediaPlayer;
private TextView endTime,startTime;
private boolean isPlaying =false;
private SeekBar playerSeekBar;
private ImageView playpauseimg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

View decodeView=getWindow().getDecorView();
int options= View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
decodeView.setSystemUiVisibility(options);

        setContentView(R.layout.activity_main);

        final LinearLayout search=findViewById(R.id.search);
        final LinearLayout menu=findViewById(R.id.menu);
        final RecyclerView musicview=findViewById(R.id.musicview);
        final CardView playpause=findViewById(R.id.playpause);
         ImageView playpauseimg=findViewById(R.id.playpauseimg);
        final ImageView next=findViewById(R.id.next);
        final ImageView previous=findViewById(R.id.previous);
        playerSeekBar=findViewById(R.id.playseekbar);

        startTime=findViewById(R.id.timestart);
        endTime=findViewById(R.id.timeend);
        musicview.setHasFixedSize(true);
        musicview.setLayoutManager(new LinearLayoutManager(this));
        mediaPlayer=new MediaPlayer();


//        if(ContextCompat.checkSelfPermission(this,Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION)== PackageManager.PERMISSION_GRANTED){
//            getMusicFiles();
//
//        }
//        else
//        {
//            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
//                requestPermissions(new String[]{Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION},11);
//            }
//            else
//            {
//                getMusicFiles();
//            }
//        }


    }

    @SuppressLint("Range")
    private void getMusicFiles() {
        ContentResolver contentResolver=getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor =contentResolver.query(uri,null ,MediaStore.Audio.Media.DATA+"LIKE?",new String[]{"%.mp3"},null);
        if(cursor==null){
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
        else if(!cursor.moveToNext()){
            Toast.makeText(this, "No Music Found", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                final String getArtistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                @SuppressLint("Range") final String getMusicFileName=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
         @SuppressLint("Range") long cursorId;
             cursorId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                Uri musicFileUri= ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursorId);
                String getDuration ="00:00";
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    getDuration =cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
                }
                final musiclist musiclist=new musiclist(getMusicFileName,"getArtistName",getDuration,false,musicFileUri);
                musiclistLists.add(musiclist);
            }
        }

        musicview.setAdapter(new musicAdapter(musiclistLists,MainActivity.this));
        cursor.close();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getMusicFiles();
        } else {
            Toast.makeText(this, "Permission Declined", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if(hasFocus){
            View decodeView=getWindow().getDecorView();
            int options= View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decodeView.setSystemUiVisibility(options);

        }
    }

    @Override
    public void onChanged(int position) {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.reset();
        }
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.setDataSource(MainActivity.this, musiclistLists.get(position).getMusicFile());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Unable to play trackk", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                final int getTotalDuration=mp.getDuration();
                String generateDuration=String.format(Locale.getDefault(),"%02d:%02d",
                        TimeUnit.MICROSECONDS.toMinutes(getTotalDuration),
                        TimeUnit.MILLISECONDS.toSeconds(getTotalDuration) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getTotalDuration)));
                endTime.setText(generateDuration);
                isPlaying=true;
                mp.start();
                playerSeekBar.setMax(getTotalDuration);
                playpauseimg.setImageResource(R.drawable.pause);
            }
        });


    }

    private class WRITE_EXTERNAL_STORAGE {
    }
}