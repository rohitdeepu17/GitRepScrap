package com.example.rohitsingla.scrapman;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.ArrayList;


public class PriceList extends Activity {
    ListView listViewPriceList ;

    ScrapDatabaseAdapter mScrapDatabaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_list);

        mScrapDatabaseAdapter = new ScrapDatabaseAdapter(this);

        listViewPriceList = (ListView) findViewById(R.id.list_view_price_list);

        ArrayList<PriceListPairs> mPriceListPairs = new ArrayList<PriceListPairs>();
        try {
            mPriceListPairs = mScrapDatabaseAdapter.getPriceList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long size = mPriceListPairs.size();
        ArrayList<String> mPriceList  = new ArrayList<String>();
        for(int i=0;i<size;i++)
        {
            mPriceList.add("" + mPriceListPairs.get(i).key() + "( Rs." + mPriceListPairs.get(i).value() + "/Kg)");
        }

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, mPriceList);


        // Assign adapter to ListView
        listViewPriceList.setAdapter(adapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.price_list, menu);
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
