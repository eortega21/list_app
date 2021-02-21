package com.zybooks.inventorylist;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class smsPermission extends AppCompatActivity {

    static final int REQUEST_CODE = 123;
    Button btRequest, btCheck, goBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_permission);

        btRequest= findViewById(R.id.buttonGet);
        btCheck = findViewById(R.id.buttonCheck);
        goBack = findViewById(R.id.buttonBack);

        btRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(ContextCompat.checkSelfPermission(smsPermission.this, Manifest.permission.SEND_SMS) != getPackageManager().PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(smsPermission.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE);
                }
            }
        });

        btCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);
            }
        });

        goBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
    }
}