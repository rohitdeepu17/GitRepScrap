package com.example.rohitsingla.scrapman;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;


public class PastRequestDetails extends Activity {

    TextView textViewRequestId, textViewDay, textViewTimeSlot, textViewStatus;
    ListView listViewWeights ;

    ScrapDatabaseAdapter mScrapDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_past_request_details);

        mScrapDatabaseAdapter = new ScrapDatabaseAdapter(this);

        textViewRequestId = (TextView)findViewById(R.id.text_view_request_id);
        textViewDay = (TextView)findViewById(R.id.text_view_day);
        textViewTimeSlot = (TextView)findViewById(R.id.text_view_time_slot);
        textViewStatus = (TextView)findViewById(R.id.text_view_status);

        listViewWeights = (ListView)findViewById(R.id.list_view_weight_details);

        //getting extras from intent
        String day, timeSlot, status;
        int requestId;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                day = "Default";
                timeSlot = "Default";
                status = "Default";
                requestId = -1;
            } else {
                day = extras.getString("day");
                timeSlot = extras.getString("timeSlot");
                status = extras.getString("status");
                requestId = extras.getInt("requestId");
            }
        } else {
            day = (String) savedInstanceState.getSerializable("day");
            timeSlot = (String) savedInstanceState.getSerializable("timeSlot");
            status = (String) savedInstanceState.getSerializable("status");
            requestId = (Integer) savedInstanceState.getSerializable("requestId");
        }

        textViewRequestId.setText(""+requestId);
        textViewDay.setText(day);
        textViewTimeSlot.setText(timeSlot);
        textViewStatus.setText(status);

        ArrayList<RequestWeightsData> mRequestWeightsData = new ArrayList<RequestWeightsData>();
        try {
            mRequestWeightsData = mScrapDatabaseAdapter.getWeightsDetails(requestId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long size = mRequestWeightsData.size();
        ArrayList<String> mWeightsList  = new ArrayList<String>();
        for(int i=0;i<size;i++)
        {
            mWeightsList.add("" + mRequestWeightsData.get(i).getCategoryName()  + " " + mRequestWeightsData.get(i).getWeight());
        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mWeightsList);


        // Assign adapter to ListView
        listViewWeights.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.past_request_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
