package com.zybooks.inventorylist;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class LoginScreen extends AppCompatActivity {

    DatabaseHelper db;

    private EditText mUserNameFirstTime;
    private EditText mPasswordFirstTime;
    private EditText mConfirmPasswordFirstTime;
    private Button mButtonRegister;

    private EditText userName, userPW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        db = new DatabaseHelper(getApplicationContext());

        mUserNameFirstTime = findViewById(R.id.editUserNameFirstTime);
        mPasswordFirstTime = findViewById(R.id.editTextPasswordFirstTime);
        mConfirmPasswordFirstTime = findViewById(R.id.editTextPasswordReEnterFirstTime);
        mButtonRegister = findViewById(R.id.buttonRegister);

        userName = findViewById(R.id.editText_UserName);
        userPW = findViewById(R.id.editText_UserPassword);
    }




    public void logInButton(View view){
        String user_name = userName.getText().toString();
        String user_key = userPW.getText().toString();
        userPW.setText("");



        boolean user_exist = db.userLogin(user_name,user_key);

        if(user_exist) {
            userName.setText("");
            Toast.makeText(this, "User Exist", Toast.LENGTH_SHORT).show();
            login();
        }
        else{
            Toast.makeText(this, "No exist, please register" , Toast.LENGTH_SHORT).show();
        }



    }
    public void firstTime(View view){
        //username, password, confirm password will become visible.
        mUserNameFirstTime.setVisibility(View.VISIBLE);
        mPasswordFirstTime.setVisibility(View.VISIBLE);
        mConfirmPasswordFirstTime.setVisibility(View.VISIBLE);
        mButtonRegister.setVisibility(View.VISIBLE);
    }
    public void Register(View view){
        if(!userNameExist(mUserNameFirstTime.getText().toString()) && confirmPWmatch(mPasswordFirstTime.getText().toString(),mConfirmPasswordFirstTime.getText().toString())){
            boolean user_registered = db.addUser(mUserNameFirstTime.getText().toString(),mPasswordFirstTime.getText().toString());


            if(user_registered){
                Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "Not Registered" , Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(this, "Error while registering." , Toast.LENGTH_SHORT).show();

        }
        mPasswordFirstTime.setText("");
        mConfirmPasswordFirstTime.setText("");
    }
    public boolean userNameExist(String userName){
        boolean checkUser = db.checkUserExist(userName);
        if(checkUser){
            Toast.makeText(this, "Username Exist" , Toast.LENGTH_SHORT).show();
            return true;
        }
        else{
            return false;
        }
    }

    public boolean confirmPWmatch(String pw, String cpw){
        boolean pw_match = false;
        if(pw.equals(cpw)){
            pw_match = true;
        }
        else{
            pw_match = false;
            Toast.makeText(this, "Password Mismatch" , Toast.LENGTH_SHORT).show();
        }
        return pw_match;
    }

    public void login(){
        Intent openInventory = new Intent(this, MainActivity.class);
        startActivity(openInventory);
    }
}
