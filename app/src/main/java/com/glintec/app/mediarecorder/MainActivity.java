package com.glintec.app.mediarecorder;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private Button btnRec, btnPlay, btnStop;
    private MediaRecorder recorder;
    private MediaPlayer mp;
    File archivo;
    TextView txtViewStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtViewStatus = findViewById(R.id.txtViewStatus);
        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        btnRec = findViewById(R.id.btnRec);
        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });

        btnStop = findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });

    }

    private void record(){
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        File path = new File(Environment.getExternalStorageDirectory().getPath());
        try{
            archivo = File.createTempFile("temporal", ".3gp",path);
        }catch (IOException e){
            e.printStackTrace();
        }
        recorder.setOutputFile(archivo.getAbsolutePath());
        try{
            recorder.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }
        recorder.start();
        txtViewStatus.setText("GRABANDO");
        btnRec.setEnabled(false);
        btnStop.setEnabled(true);
    }

    private void stop(){
        recorder.stop();
        recorder.release();
        mp = new MediaPlayer();
        mp.setOnCompletionListener(this);
        try{
            mp.setDataSource(archivo.getAbsolutePath());
            mp.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }
        txtViewStatus.setText("Listo para reproducir el audio");
        btnRec.setEnabled(true);
        btnStop.setEnabled(false);
        btnPlay.setEnabled(true);
    }

    private void play(){
        mp.start();
        btnPlay.setEnabled(false);
        btnStop.setEnabled(false);
        btnRec.setEnabled(false);
        txtViewStatus.setText("Reproduciendo audio");
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btnRec.setEnabled(true);
        btnStop.setEnabled(true);
        btnPlay.setEnabled(true);
        txtViewStatus.setText("Reproduccion finalizada");
    }
}
