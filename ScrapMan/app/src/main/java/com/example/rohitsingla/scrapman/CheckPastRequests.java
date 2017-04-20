package com.example.rohitsingla.scrapman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;


public class CheckPastRequests extends Activity {
    ScrapDatabaseAdapter mScrapDatabaseAdapter;

    ListView listViewPastRequests ;

    ArrayList<PickupRequestData> mPickupRequestData = new ArrayList<PickupRequestData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_past_requests);

        mScrapDatabaseAdapter = new ScrapDatabaseAdapter(this);

        listViewPastRequests = (ListView) findViewById(R.id.list_view_past_requests);

        //ArrayList<PickupRequestData> mPickupRequestData = new ArrayList<PickupRequestData>();
        try {
            mPickupRequestData = mScrapDatabaseAdapter.getPickupRequests(HandleSharedPrefs.getSharedPrefValue(CheckPastRequests.this,"username"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long size = mPickupRequestData.size();
        ArrayList<String> mPickupDataStrings  = new ArrayList<String>();
        for(int i=0;i<size;i++)
        {
            mPickupDataStrings.add(""+ mPickupRequestData.get(i).getmRequestId()+ " " + mPickupRequestData.get(i).getmDay() + " " + mPickupRequestData.get(i).getmTimeSlot() + " " + getStatusString(mPickupRequestData.get(i).getmStatus()));
        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mPickupDataStrings);


        // Assign adapter to ListView
        listViewPastRequests.setAdapter(adapter);

        listViewPastRequests.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CheckPastRequests.this, PastRequestDetails.class);
                intent.putExtra("requestId",mPickupRequestData.get(position).getmRequestId());
                intent.putExtra("day",mPickupRequestData.get(position).getmDay());
                intent.putExtra("timeSlot",mPickupRequestData.get(position).getmTimeSlot());
                intent.putExtra("status",getStatusString(mPickupRequestData.get(position).getmStatus()));
                startActivity(intent);
            }
        });
    }

    public String getStatusString(int status)
    {
        if(status == 0)
            return "Requested";
        else if(status == 1)
            return "Accepted";
        else if(status == 2)
            return "Picked";
        else
            return "Error";
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.check_past_requests, menu);
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
