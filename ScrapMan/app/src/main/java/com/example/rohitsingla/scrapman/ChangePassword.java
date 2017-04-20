package com.example.rohitsingla.scrapman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;


public class ChangePassword extends Activity {
    ScrapDatabaseAdapter mScrapDatabaseAdapter;

    EditText editTextCurrentPassword, editTextNewPassword, editTextConfirmNewPassword;
    Button buttonUpdatePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mScrapDatabaseAdapter = new ScrapDatabaseAdapter(this);

        editTextCurrentPassword = (EditText) findViewById(R.id.edit_text_current_password);
        editTextNewPassword = (EditText) findViewById(R.id.edit_text_new_password);
        editTextConfirmNewPassword = (EditText) findViewById(R.id.edit_text_confirm_new_password);

        buttonUpdatePassword = (Button)findViewById(R.id.button_update_password);
        buttonUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //update the password in database with proper error handling
                String currentPassword = editTextCurrentPassword.getText().toString();
                String newPassword = editTextNewPassword.getText().toString();
                String confirmNewPassword = editTextConfirmNewPassword.getText().toString();

                boolean flag = true;

                //check if any field is empty or filled with only spaces
                if(currentPassword.length() == 0 || newPassword.length() == 0 || confirmNewPassword.length() == 0 ||
                        currentPassword.trim().length() == 0|| newPassword.trim().length() == 0 || confirmNewPassword.trim().length() == 0){
                    Toast.makeText(ChangePassword.this, "All the above fields are mandatory", Toast.LENGTH_SHORT).show();
                    flag = false;
                }

                //verify current password, using verifyLoginCredentials function
                try {
                    if(flag && !mScrapDatabaseAdapter.verifyLoginCredentials(HandleSharedPrefs.getSharedPrefValue(ChangePassword.this, "username"), currentPassword)){
                        Toast.makeText(ChangePassword.this, "Sorry, You have entered wrong current password", Toast.LENGTH_SHORT).show();
                        flag = false;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //verify if both password and confirmNewPassword match
                if(flag && !newPassword.equals(confirmNewPassword)){
                    Toast.makeText(ChangePassword.this, "Sorry, Password and Confirm Password do not match", Toast.LENGTH_SHORT).show();
                    flag = false;
                }

                //if no error so far, update password for this user
                if(flag){
                    try {
                        mScrapDatabaseAdapter.updatePassword(HandleSharedPrefs.getSharedPrefValue(ChangePassword.this, "username"), newPassword);
                        Toast.makeText(ChangePassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT);
                        Intent intent = new Intent(ChangePassword.this, HomePage.class);
                        startActivity(intent);
                        finish();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.change_password, menu);
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
