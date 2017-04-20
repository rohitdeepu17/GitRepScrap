package com.example.rohitsingla.scrapman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBQueryExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBScanExpression;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.example.rohitsingla.scrapman.amazonaws.mobile.AWSMobileClient;
import com.example.rohitsingla.scrapman.amazonaws.models.nosql.ScrapCategoryDO;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MyActivity extends Activity {
    EditText editTextUsername, editTextPassword;
    Button buttonLogin, buttonSignUp;

    ScrapDatabaseAdapter mScrapDatabaseAdapter;

    String username, passwd;
    String TAG = "MyActivity";

    String[] categoryNames = new String[]{
            "Paper",
            "Cardbox",
            "Iron",
            "Tin",
            "Plastic"
    };

    double[] prices = new double[]{
            9.00,
            7.00,
            18.00,
            50.00,
            12.50,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AmazonAWS
        initializeApplication();
        setContentView(R.layout.activity_my);

        /* TODO : Why onCreate is being called for an activity instance already on the stack?
         * Reason : new intent launched
          * But where does prev instance of this activity goes from activity stack?*/
        /*if(getIntent().getBooleanExtra("EXIT",false)){
            finish();
        }*/

        Log.d(TAG, "Inside onCreate of MyActivity");
        mScrapDatabaseAdapter = new ScrapDatabaseAdapter(this);

        //For the time being, create price list on the user end
        try {
            mScrapDatabaseAdapter.createPriceList(categoryNames, prices, categoryNames.length);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //check the values of shared preferences username and password :
        //If they are not null and matches some user credentials in the database(need to check this also,
        //because user might have changed his credentials through website), directly launch HomePage with sharedPref credentials
        username = HandleSharedPrefs.getSharedPrefValue(MyActivity.this, "username");
        passwd = HandleSharedPrefs.getSharedPrefValue(MyActivity.this, "passwd");

        try {
            if(username != null && username.length()!=0 && passwd != null && passwd.length()!=0 && mScrapDatabaseAdapter.verifyLoginCredentials(username, passwd)){
                Intent intent = new Intent(MyActivity.this,HomePage.class);
                startActivity(intent);
                finish();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        editTextUsername = (EditText)findViewById(R.id.edit_text_username);
        editTextPassword = (EditText)findViewById(R.id.edit_text_password);

        buttonLogin = (Button)findViewById(R.id.button_login);
        buttonSignUp = (Button)findViewById(R.id.button_sign_up);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //login with proper error handling
                //if correct credentials, take user to HomePage
                try {
                    username = editTextUsername.getText().toString();
                    passwd = editTextPassword.getText().toString();
                    Log.d(TAG, "username : "+username+", password : "+passwd);
                    if(username != null && username.length()!=0 && passwd != null && passwd.length()!=0 && mScrapDatabaseAdapter.verifyLoginCredentials(username, passwd))
                    {
                        HandleSharedPrefs.saveUsernameSharedPref(MyActivity.this, "username", username, "passwd", passwd);
                        Intent intent = new Intent(MyActivity.this,HomePage.class);
                        //intent.putExtra("username", editTextUsername.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(MyActivity.this,"Incorrect username/password",Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                //else show toast
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MyActivity.this, SignUpPage.class);
                startActivity(mIntent);
            }
        });

        /*final ScrapCategoryDO myScrapCategoryObj = new ScrapCategoryDO();
        for(int i=0;i<categoryNames.length;i++){
            myScrapCategoryObj.setCategoryName(categoryNames[i]);
            myScrapCategoryObj.setUnitPrice(prices[i]);
            Runnable runnable = new Runnable(){

                @Override
                public void run() {
                    //AWSMobileClient.defaultMobileClient().getDynamoDBMapper().save(myScrapCategoryObj);
                    //ScrapCategoryDO myScrapCategoryObj1 = AWSMobileClient.defaultMobileClient().getDynamoDBMapper().load(ScrapCategoryDO.class,"Plastic");
                    ScrapCategoryDO myScrapCategoryObj1 = AWSMobileClient.defaultMobileClient().getDynamoDBMapper().load(ScrapCategoryDO.class);
                    if(myScrapCategoryObj1!=null)
                        Log.d(TAG, "Scrap Category = "+myScrapCategoryObj1.getUnitPrice());
                    else
                        Log.d(TAG, "Scrap Category = null");
                }
            };
            Thread myThread = new Thread(runnable);
            myThread.start();
        }*/

        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                /*AWSMobileClient.defaultMobileClient().getDynamoDBMapper().save(myScrapCategoryObj);
                ScrapCategoryDO myScrapCategoryObj1 = AWSMobileClient.defaultMobileClient().getDynamoDBMapper().load(ScrapCategoryDO.class,"Plastic");
                Log.d(TAG,"Rohit category name = "+myScrapCategoryObj1.getCategoryName()+", unit price = "+myScrapCategoryObj1.getUnitPrice());*/

                DynamoDBMapper myDynamoDbMapper = AWSMobileClient.defaultMobileClient().getDynamoDBMapper();
                try {
                    FindAllScrapCategories(myDynamoDbMapper);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*ScrapCategoryDO myScrapCategoryObj1 = AWSMobileClient.defaultMobileClient().getDynamoDBMapper().load(ScrapCategoryDO.class);
                final ScrapCategoryDO myHashKeyValues = new ScrapCategoryDO();
                DynamoDBQueryExpression<ScrapCategoryDO> queryExpression = new DynamoDBQueryExpression<ScrapCategoryDO>().setHashKeyValues(myHashKeyValues);
                List<ScrapCategoryDO> allScrapCategories = AWSMobileClient.defaultMobileClient().getDynamoDBMapper().query(ScrapCategoryDO.class, queryExpression);
                Log.d(TAG, "Number of Scrap Categories = "+allScrapCategories.size());*/
            }
        };
        Thread myThread = new Thread(runnable);
        myThread.start();
    }

    //For AmazonAWS
    private void initializeApplication() {

        // Initialize the AWS Mobile Client
        AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());

        // ... Put any application-specific initialization logic here ...
    }

    private void FindAllScrapCategories(DynamoDBMapper mapper)
            throws Exception {
        Log.d(TAG, "Find all scrap categories:-");

        Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
        eav.put(":val1", new AttributeValue().withS("12"));

        /*DynamoDBQueryExpression<ScrapCategoryDO> queryExpression = new DynamoDBQueryExpression<ScrapCategoryDO>()
                .withKeyConditionExpression("categoryName = :val1").withExpressionAttributeValues(eav);*/

        //DynamoDBQueryExpression<ScrapCategoryDO> queryExpression = new DynamoDBQueryExpression<ScrapCategoryDO>();

        ScrapCategoryDO myObject = new ScrapCategoryDO();
        myObject.setCategoryName("Iron");
        DynamoDBQueryExpression<ScrapCategoryDO> queryExpression = new DynamoDBQueryExpression<ScrapCategoryDO>()
                .withHashKeyValues(myObject)
                .withFilterExpression("UnitPrice < :val1")
                .withExpressionAttributeValues(eav);

        List<ScrapCategoryDO> latestReplies = mapper.query(ScrapCategoryDO.class, queryExpression);
        Log.d(TAG, "Number of categories = "+latestReplies.size());
        for (ScrapCategoryDO reply : latestReplies) {
            Log.d(TAG,"category name = "+reply.getCategoryName()+", unit price = "+ reply.getUnitPrice());
        }

        ScanResultPage<ScrapCategoryDO> yourModelClassResultPage = null;
        do {
            //DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(dynamoDBClient);
            DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
            if (yourModelClassResultPage != null) {
                scanExpression.setExclusiveStartKey(yourModelClassResultPage.getLastEvaluatedKey());
            }
            yourModelClassResultPage = mapper.scanPage(ScrapCategoryDO.class, scanExpression);
            Log.d(TAG,"Hey, "+yourModelClassResultPage.getResults().size());
            List<ScrapCategoryDO> myList = yourModelClassResultPage.getResults();
            int n = myList.size();
            for(int i=0;i<n;i++){
                Log.d(TAG,"CategoryName = "+myList.get(i).getCategoryName()+"UnitPrice = "+myList.get(i).getUnitPrice());
            }

        } while (yourModelClassResultPage.getLastEvaluatedKey() != null);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
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
