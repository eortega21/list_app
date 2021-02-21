package com.zybooks.inventorylist;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Menu mMenu;
    EditText ed1, ed2;
    DatabaseHelper db;
    ListView l1;
    ArrayList<item> arrayList;
    MyAdapter myAdapter;
    Button alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed1 = (EditText)findViewById(R.id.editText_item_name);
        ed2 = (EditText)findViewById(R.id.editText_qty);
        l1 = (ListView)findViewById(R.id.listView);
        db = new DatabaseHelper(this);

        arrayList  = new ArrayList<>();
        loadDataInListView();

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String ItemName = intent.getStringExtra("id");
            Toast.makeText(MainActivity.this,"ID: " + ItemName,Toast.LENGTH_SHORT).show();
            loadDataInListView();
        }
    };

    public void loadDataInListView(){

        arrayList = db.getAllData();
        myAdapter = new MyAdapter(this, arrayList);
        l1.setAdapter(myAdapter);

        myAdapter.notifyDataSetChanged();
    }



    public void insert(View v){
        if(TextUtils.isEmpty(ed1.getText()) || TextUtils.isEmpty(ed2.getText()) ){
            Toast.makeText(MainActivity.this, "Name AND quantity must be entered to add to list.", Toast.LENGTH_LONG).show();
        }
        else{
            boolean result = db.insertData(ed1.getText().toString(),ed2.getText().toString());
            ed1.setText("");
            ed2.setText("");
            if(result){
                Toast.makeText(MainActivity.this, "Value Inserted", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(MainActivity.this, "Value NOT Inserted", Toast.LENGTH_SHORT).show();
            }
        }
        loadDataInListView();
    }

    public void loadIT(View v){
        loadDataInListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_one:
                Toast.makeText(MainActivity.this, "edit later",Toast.LENGTH_SHORT).show();

                //myAdapter.hideDeleteButton();

                myAdapter.notifyDataSetChanged();
                break;

            case R.id.action_done:
                loadDataInListView();
                break;

            case R.id.SMSpermission:
                Intent permSMS = new Intent(this, smsPermission.class);
                startActivity(permSMS);
                break;
        }

        return super.onContextItemSelected(item);
    }
}