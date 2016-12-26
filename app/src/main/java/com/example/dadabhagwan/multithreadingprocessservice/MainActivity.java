package com.example.dadabhagwan.multithreadingprocessservice;

import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public EditText ImageUrl;
    public Button downloadingButton;
    public ListView listImages;
    public LinearLayout LoadingSection;
    public String[] imagesStrings;
    public ProgressBar progressBar;
    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageUrl = (EditText) findViewById(R.id.EnterUrl);
        downloadingButton = (Button) findViewById(R.id.ClickToDownloadImage);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listImages = (ListView) findViewById(R.id.ListView);
        listImages.setOnItemClickListener(this);
        imagesStrings = getResources().getStringArray(R.array.UrlImages);
        LoadingSection= (LinearLayout) findViewById(R.id.loadingSection);
        handler=new Handler();
    }

    public boolean downloadImagesUsingThreads(String url) {
        boolean Successful = false;
        HttpURLConnection connection = null;
        URL downloadURL = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file;
        try {
            downloadURL = new URL(url);
            connection = (HttpURLConnection) downloadURL.openConnection();
            inputStream = connection.getInputStream();
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + Uri.parse(url).getLastPathSegment());
            fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int read = -1;
            while ((read = inputStream.read(buffer)) != -1) {

            fileOutputStream.write(buffer,0,read);
            }
            Successful=true;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    LoadingSection.setVisibility(View.GONE);
                }
            });
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream!=null)
            {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


        return Successful;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        ImageUrl.setText(imagesStrings[i]);

    }

    public void downloadImage(View view) {
        String url=ImageUrl.getText().toString();
        Thread thread = new Thread(new DownlaodThread(url));
        thread.start();

    }

    private class DownlaodThread implements Runnable {


        String url;
        public DownlaodThread(String url)
        {
            this.url=url;
        }

        @Override
        public void run() {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
              handler.post(new Runnable() {
                  @Override
                  public void run() {
                      LoadingSection.setVisibility(View.VISIBLE);
                  }
              });


                }
            });
            downloadImagesUsingThreads(url);
        }
    }
}
