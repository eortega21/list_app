package com.zybooks.inventorylist;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.getSystemService;

public class MyAdapter extends BaseAdapter implements ListAdapter {

    Context context;
    ArrayList<item>arrayList;
    DatabaseHelper db;
    String deleteItem;
    Button deleteButton;

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




        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.mycustomlistview,null);

        TextView t1_id = (TextView)convertView.findViewById(R.id.id_txt);
        TextView t2_name = (TextView)convertView.findViewById(R.id.name_txt);
        TextView t3_age = (TextView)convertView.findViewById(R.id.qty_txt);

        deleteButton = (Button)convertView.findViewById(R.id.button_delete);
        //deleteButton.setVisibility(convertView.INVISIBLE);

        db = new DatabaseHelper(context.getApplicationContext());


        item Item = arrayList.get(position);

        t1_id.setText(String.valueOf(Item.getId()));
        t2_name.setText(Item.getName());
        t3_age.setText((Item.getQty()));




        deleteItem = t2_name.getText().toString();

        Button buttonDecreaseQty = (Button) convertView.findViewById(R.id.button_decrease);

        Button buttonAddQty = (Button)convertView.findViewById(R.id.button_Increase);

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
                    alertBox(Item.getName(),QtyInt);
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
    public void hideDeleteButton(){

        deleteButton.setVisibility(View.INVISIBLE);
    }


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