package com.example.dadabhagwan.multithreadingprocessservice;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AysncTaskListView extends Activity {

    public String[] items={"One","One","One","One","One","One","One","Two","Three","Four","One","One","One","One","One","One","One","Two","Three","Four","One","One","One","One","One","One","One","Two","Three","Four","One","One","One","One","One","One","One","Two","Three","Four"};
    ListView listViewItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);

        setContentView(R.layout.activity_aysnc_task_list_view);
        listViewItems= (ListView) findViewById(R.id.listViewItems);
        listViewItems.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,new ArrayList<String>()));
        new MyAsyncTask().execute();
    }
    class MyAsyncTask extends AsyncTask<Void,String,Void>{


        int count;
        private ArrayAdapter<String> adapter;
        @Override
        protected void onPreExecute() {
           adapter= (ArrayAdapter<String>) listViewItems.getAdapter();
           setProgressBarIndeterminate(false);
           setProgressBarVisibility(true);
        }
        @Override
        protected Void doInBackground(Void... voids) {

            for(String item: items)
            {
                publishProgress(item);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            return null;
        }


        @Override
        protected void onProgressUpdate(String... values) {

            adapter.add(values[0]);
            count++;
            setProgress((int) ((double)(count/items.length))*10000);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
          setProgressBarVisibility(true);
        }
    }


}
