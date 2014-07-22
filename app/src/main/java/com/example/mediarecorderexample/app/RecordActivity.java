package com.example.mediarecorderexample.app;

import android.app.Activity;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;


public class RecordActivity extends Activity implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceView cameraPreview;
    private MediaRecorder mediaRecorder;
    private FrameLayout frame;

    private final static int maximumWaitTimeForCamera = 5000;
    private final static String TAG = "RecordActivity";
    private final static String outputFile = "/sdcard/media.mp4";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraPreview = new SurfaceView(this);
        cameraPreview.getHolder().addCallback(this);
        //cameraPreview.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        frame = (FrameLayout) super.findViewById(R.id.camera_preview);
        frame.addView(this.cameraPreview);
    }

    public void stopRecording() {
        mediaRecorder.stop();
        releaseMediaRecorder();
        releaseCamera();
    }

    private void releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void releaseCamera() {
        if (this.camera != null) {
            this.camera.stopPreview();
            this.camera.release();
            this.camera = null;
            this.frame.removeView(this.cameraPreview);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        if (camera == null) {
            camera = getCameraInstanceRetry();
        }

        try {
            camera.stopPreview();
            camera.setPreviewDisplay(null);
        } catch (IOException e){
            Log.d(TAG, "IOException setting preview display: " + e.getMessage());

        }
        camera.unlock();
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setCamera(camera);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setOutputFile(outputFile);
        mediaRecorder.setPreviewDisplay(cameraPreview.getHolder().getSurface());

        try {
            mediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
        }

            mediaRecorder.start();

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopRecording();
                finish();
            }
        }, 5000);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //Do nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //Do nothing
    }

    private Camera getCameraInstanceRetry() {
        Camera c = null;
        boolean acquiredCam = false;
        int timePassed = 0;
        while (!acquiredCam && timePassed < maximumWaitTimeForCamera) {
            try {
                c = Camera.open();
                acquiredCam = true;
                return c;
            } catch (Exception e) {
                Log.e(TAG, "Exception encountered opening camera:" + e.getLocalizedMessage());
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException ee) {
                Log.e(TAG, "Exception encountered sleeping:" + ee.getLocalizedMessage());
            }
            timePassed += 200;
        }
        return c;
    }

}
