package com.example.user.serverapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 03.04.2018.
 */

public class TestMainActivity extends Activity implements View.OnClickListener {

    EditText urlText;
    ProgressBar pd;
    ImageView imgView;
    SampleResultReceiver resultReceiever;
    Bitmap bmp;
    String defaultUrl = "http://developer.android.com/assets/images/dac_logo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tempactivitymain);
        resultReceiever = new SampleResultReceiver(new Handler());
        urlText = (EditText) findViewById(R.id.urlText);
        pd = (ProgressBar) findViewById(R.id.downloadPD);
        imgView = (ImageView) findViewById(R.id.imgView);
    }

    private class SampleResultReceiver extends ResultReceiver {

        public SampleResultReceiver(Handler handler) {
            super(handler);
            // TODO Auto-generated constructor stub
        }

        // можно переместить в сервис(?)
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            // TODO Auto-generated method stub
            switch (resultCode) {
                case SampleIntentService.DOWNLOAD_ERROR:
                    Toast.makeText(getApplicationContext(), "error in download",
                            Toast.LENGTH_SHORT).show();
                    pd.setVisibility(View.INVISIBLE);
                    break;

                case SampleIntentService.DOWNLOAD_SUCCESS:
                    String filePath = resultData.getString("filePath");

                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = false;
                        options.inPreferredConfig = Bitmap.Config.RGB_565;
                        options.inDither = true;
                        options.inSampleSize = 8;


                    bmp = BitmapFactory.decodeFile(filePath,options);
                    if (imgView != null && bmp != null) {
                        imgView.setImageBitmap(bmp);
                        Toast.makeText(getApplicationContext(),
                                "image download via IntentService is done",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "error in decoding downloaded file",
                                Toast.LENGTH_SHORT).show();
                    }
                    try{
                    pd.setIndeterminate(false);
                    pd.setVisibility(View.INVISIBLE);}

                    catch (OutOfMemoryError e){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            Log.i(TAG, "SIZEOFFILE" + String.valueOf(bmp.getAllocationByteCount()));
                        }
                        e.printStackTrace();
                    }

                    break;
            }
            super.onReceiveResult(resultCode, resultData);
        }

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        Intent startIntent = new Intent(TestMainActivity.this,
                SampleIntentService.class);
        startIntent.putExtra("receiver", resultReceiever);
        startIntent.putExtra("url",
                TextUtils.isEmpty(urlText.getText()) ? defaultUrl : urlText
                        .getText().toString());
        startService(startIntent);

        pd.setVisibility(View.VISIBLE);
        pd.setIndeterminate(true);
    }
}