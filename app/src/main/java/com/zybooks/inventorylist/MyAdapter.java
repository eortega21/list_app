package com.zybooks.inventorylist;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getSystemService;



public class MyAdapter extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<item>arrayList;
    DatabaseHelper db;
    String deleteItem;
    ImageButton deleteButton;

    private NotificationManagerCompat notificationManager;

    public MyAdapter(Context context, ArrayList<item> arrayList){
        this.context = context;
        this.arrayList = arrayList;

    }


    @Override
    public int getCount() {

        return this.arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        notificationManager = NotificationManagerCompat.from(context);


        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.mycustomlistview,null);

        TextView t2_name = (TextView)convertView.findViewById(R.id.name_txt);
        TextView t3_age = (TextView)convertView.findViewById(R.id.qty_txt);

        deleteButton = (ImageButton) convertView.findViewById(R.id.button_delete);
        //deleteButton.setVisibility(convertView.INVISIBLE);

        db = new DatabaseHelper(context.getApplicationContext());

        item Item = arrayList.get(position);

        t2_name.setText(Item.getName());
        t3_age.setText((Item.getQty()));




        deleteItem = t2_name.getText().toString();

        ImageButton buttonDecreaseQty = (ImageButton) convertView.findViewById(R.id.button_decrease);

        ImageButton buttonAddQty = (ImageButton) convertView.findViewById(R.id.button_Increase);

        deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                deleteItem = t2_name.getText().toString();

                //SEND MSG TO ACTIVITY TO UPDATE.
                Intent intent = new Intent("custom-message");
                intent.putExtra("id",String.valueOf(Item.getId()));//sending ID incase I want to delete it from main screen.
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

                //DELETE
                boolean checkdeleteddata = db.deleteData(Item.getId());
                if(checkdeleteddata == true){
                    Toast.makeText(context, "Entry deleted: " + deleteItem, Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "Entry not deleted", Toast.LENGTH_SHORT).show();
                }
            }
        });


        buttonDecreaseQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String QtyString = t3_age.getText().toString();
                int QtyInt = Integer.parseInt(QtyString);

                if(QtyInt > 0){
                    QtyInt = QtyInt - 1;
                    db.QTYupdate(Item.getId(), Integer.toString(QtyInt));
                }
                if(QtyInt< 5){
                    String item_low = "Running low on " + Item.getName() +".\nQuantity amount of " + QtyInt;
                    String item_out = Item.getName() +" OUT OF STOCK.\nOrder more now!";
                    String item_notify = "Error.";
                    if(QtyInt < 5){
                        item_notify = item_low;
                    }
                    if(QtyInt == 0){
                        item_notify = item_out;
                    }
                    if(ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != -1){
                        sendOnChannel1(item_notify,QtyInt);
                    }
                }
                t3_age.setText(String.valueOf(QtyInt));
            }
        });



        buttonAddQty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something

                String QtyString = t3_age.getText().toString();
                int QtyInt = Integer.parseInt(QtyString) + 1;
                t3_age.setText(String.valueOf(QtyInt));

                //REPORT TO DATABASE TO RECORD
                db.QTYupdate(Item.getId(),Integer.toString(QtyInt));



            }
        });
        return convertView;
    }
    //WORK IN PROGRESS
    public void hideDeleteButton(){

        deleteButton.setVisibility(View.INVISIBLE);
    }

    public void sendOnChannel1(String msg, int qty){


        Notification notification = new NotificationCompat.Builder(context, com.zybooks.inventorylist.notification.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle("UPDATE")
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1,notification);
    }


    //user for some other time.
    public void alertBox(String item, int qty){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(true);
        if(qty == 0) {
            builder.setTitle(item + " OUT OF STOCK.");
            builder.setMessage("OUT OF STOCK. remove item, or stock up");
        }
        else{
            builder.setTitle(item + " quantity running low.");
            builder.setMessage("item " + item + " is currently running low.\nCurrent Quantity at " + qty + "." +
                    "\nPlease refill soon");
        }
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DO SOMETHING
                //Toast.makeText(context,"worked",Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //DO SOMETHING
            }
        });
        builder.create().show();
    }


}