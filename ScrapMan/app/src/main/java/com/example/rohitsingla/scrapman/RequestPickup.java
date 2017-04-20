package com.example.rohitsingla.scrapman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;


public class RequestPickup extends Activity {
    ScrapDatabaseAdapter mScrapDatabaseAdapter;

    Button buttonSubmitRequest;
    Spinner spinnerDay, spinnerTimeSlot;

    private String[] arrText;
    private double[] arrTemp;
    private String[] categoryNames;
    double weights[];

    private static final String TAG = "RequestPickUp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_pickup);

        mScrapDatabaseAdapter = new ScrapDatabaseAdapter(this);

        buttonSubmitRequest = (Button)findViewById(R.id.button_submit_request);
        spinnerDay = (Spinner)findViewById(R.id.spinner_day);
        spinnerTimeSlot = (Spinner)findViewById(R.id.spinner_time_slot);

        ArrayList<PriceListPairs> mPriceListPairs = new ArrayList<PriceListPairs>();
        try {
            mPriceListPairs = mScrapDatabaseAdapter.getPriceList();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int size = mPriceListPairs.size();
        arrText = new String[size];
        categoryNames = new String[size];
        for(int i=0;i<size;i++)
        {
            categoryNames[i] = mPriceListPairs.get(i).key();
            arrText[i] = "" + categoryNames[i] + "( Rs." + mPriceListPairs.get(i).value() + "/Kg)";
        }

        arrTemp = new double[size];

        MyListAdapter myListAdapter = new MyListAdapter();
        ListView listView = (ListView) findViewById(R.id.list_view_request_pickup);
        listView.setAdapter(myListAdapter);

        buttonSubmitRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Log.d(TAG, spinnerDay.getSelectedItem().toString()+spinnerTimeSlot.getSelectedItem().toString());
                    Log.d(TAG, "The number of categories = "+categoryNames.length);
                    double sum = 0.0;
                    for(int i=0;i<categoryNames.length;i++) {
                        Log.d(TAG, "The weight at index " + i + " = " + arrTemp[i]);
                        sum += arrTemp[i];
                    }
                    if(sum > 0.0) {
                        mScrapDatabaseAdapter.requestPickup(spinnerDay.getSelectedItem().toString(), spinnerTimeSlot.getSelectedItem().toString(), HandleSharedPrefs.getSharedPrefValue(RequestPickup.this, "username"), categoryNames, arrTemp, categoryNames.length);
                        Intent intent = new Intent(RequestPickup.this, CheckPastRequests.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(RequestPickup.this, "Sorry, you have to request pickup for at least one category", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }





    //Creating Custom Adapter
    private class MyListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            if(arrText != null && arrText.length != 0){
                return arrText.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return arrText[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //ViewHolder holder = null;
            final ViewHolder holder;
            if (convertView == null) {

                holder = new ViewHolder();
                LayoutInflater inflater = RequestPickup.this.getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_request_pickup, null);
                holder.textView1 = (TextView) convertView.findViewById(R.id.text_view_category_unit_price);
                holder.editText1 = (EditText) convertView.findViewById(R.id.edit_text_quantity);

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            holder.ref = position;

            holder.textView1.setText(arrText[position]);
            holder.editText1.setText(String.valueOf(arrTemp[position]));
            holder.editText1.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                              int arg3) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    // TODO Auto-generated method stub
                    if(arg0.length()>0)
                        arrTemp[holder.ref] = Double.parseDouble(arg0.toString());      //Bug here : in case we erase entire double value 0.0
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView textView1;
            EditText editText1;
            int ref;
        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.request_pickup, menu);
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
