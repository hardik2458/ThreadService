

package com.example.dadabhagwan.multithreadingprocessservice;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LooperExample extends AppCompatActivity {

    MyThread thread;
    Button cliked;
    //ProgressBar progressBar;
     public     Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_looper_example);
        cliked= (Button) findViewById(R.id.btnForLooper);
        thread=new MyThread();
        thread.start();



//        handler=new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg) {
//            progressBar.setProgress(msg.arg1);
//
//            }
//        };
    }

    public void sendMessage(View view) {
        thread.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LooperExample.this, thread.currentThread().getName()+ "", Toast.LENGTH_SHORT).show();
            }
        });

    }

    class MyThread extends Thread
    {
        Handler handler;
        public MyThread() {
        }

        @Override
        public void run() {
            Looper.prepare();
            handler=new Handler();
            Looper.loop();

        }
    }
//    class MyThread implements Runnable{
//
//
//
//        @Override
//        public void run() {
//
//            for (int i = 0; i <100 ; i++) {
//                Message message=Message.obtain();
//                message.arg1=i;
//                handler.sendMessage(message);
//                try {
//                    Thread.sleep(100);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//
//        }
//    }

}
