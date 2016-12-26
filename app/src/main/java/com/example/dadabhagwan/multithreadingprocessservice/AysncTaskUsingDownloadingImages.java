package com.example.dadabhagwan.multithreadingprocessservice;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class AysncTaskUsingDownloadingImages extends Activity implements AdapterView.OnItemClickListener{

    public EditText ImageUrl1;
    public Button downloadingButton1;
    public ListView listImages1;

    public String[] imagesStrings1;
    public ProgressBar progressBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aysnc_task_using_downloading_images);
        ImageUrl1 = (EditText) findViewById(R.id.EnterUrl1);
        downloadingButton1 = (Button) findViewById(R.id.ClickToDownloadImage1);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar2);
        listImages1 = (ListView) findViewById(R.id.ListView1);
        listImages1.setOnItemClickListener(this);
        imagesStrings1 = getResources().getStringArray(R.array.UrlImages);


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        ImageUrl1.setText(imagesStrings1[i]);
    }


    public void downloadImage(View view) {
       if((ImageUrl1.getText().toString() != null) && (ImageUrl1.getText().toString().length() > 0))
       {
           MyTask myTask=new MyTask();
           myTask.execute(ImageUrl1.getText().toString());
       }

    }
  class MyTask extends AsyncTask<String,Integer,Boolean>{

      int connectionLength=-1;
      int count=0;
      Activity activity;
      @Override
      protected void onPreExecute() {
          progressBar1.setVisibility(View.VISIBLE);
          if (AysncTaskUsingDownloadingImages.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
              AysncTaskUsingDownloadingImages.this.setRequestedOrientation(Configuration.ORIENTATION_PORTRAIT);
          } else {
              {
                  AysncTaskUsingDownloadingImages.this.setRequestedOrientation(Configuration.ORIENTATION_LANDSCAPE);
              }
          }


      }

      @Override
      protected Boolean doInBackground(String... params) {
          boolean Successful = false;
          HttpURLConnection connection = null;
          URL downloadURL = null;
          InputStream inputStream = null;
          FileOutputStream fileOutputStream = null;
          File file;
          try {
              downloadURL = new URL(params[0]);
              connection = (HttpURLConnection) downloadURL.openConnection();
              connectionLength=connection.getContentLength();
              inputStream = connection.getInputStream();
              file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + Uri.parse(params[0]).getLastPathSegment());
              fileOutputStream = new FileOutputStream(file);
              byte[] buffer = new byte[1024];
              int read = -1;
              while ((read = inputStream.read(buffer)) != -1) {

                  fileOutputStream.write(buffer,0,read);
                  count=count+1;
                  publishProgress(count);
              }
              Successful=true;

          } catch (MalformedURLException e) {
              e.printStackTrace();
          } catch (IOException e) {
              e.printStackTrace();
          } finally {

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
      protected void onProgressUpdate(Integer... values) {
         progressBar1.setProgress((int) ((double)(values[0]/connectionLength))*100000);
      }

      @Override
      protected void onPostExecute(Boolean aBoolean) {
          progressBar1.setVisibility(View.GONE);
          AysncTaskUsingDownloadingImages.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
      }
  }


}
