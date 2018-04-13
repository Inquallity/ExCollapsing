package com.inquallity.excollapsing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "http://place4plays.ru/wp-content/uploads/2016/10/%D0%A2%D0%BE%D1%80%D1%82-%D0%BD%D0%B5-%D0%B0%D0%BD%D0%B8%D0%BC%D0%B51.jpg";
    private ImageView mImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        mImage = findViewById(R.id.ivImage);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    protected void onStart() {
        super.onStart();
        final Task task = new Task(URL);
        new Thread(task).start();
    }

    private synchronized void updateImage(final Bitmap bmp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImage.setImageBitmap(bmp);
            }
        });
    }

    private class Task implements Runnable {

        String url;

        Task(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            if (TextUtils.isEmpty(url)) {
                return;
            }
            try {
                final URL url = new URL(this.url);
                final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                final InputStream is = connection.getInputStream();
                final Bitmap bmp = BitmapFactory.decodeStream(is);
                updateImage(bmp);
            } catch (IOException e) {
                Log.e("ERROR", e.getMessage(), e);
            }
        }
    }
}
