package com.zybooks.inventorylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.Nullable;

import java.util.ArrayList;

class DatabaseHelper extends SQLiteOpenHelper {
    Context context;
    public DatabaseHelper(@Nullable Context context) {
        super(context, "List.db", null, 1);  //student = database name.
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE ListInfo (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, QTY TEXT)");
        db.execSQL("CREATE TABLE loginInfo (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, PW TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS ListInfo");  //name table as student info. change later.
        db.execSQL("DROP TABLE IF EXISTS loginInfo");
        onCreate(db);
    }

    public boolean userLogin(String user_name, String user_pw) {

        boolean choice = false;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM loginInfo",null);

        while(cursor.moveToNext()){

            //1 = id, 2 = name, 3 = quantity
            if(user_name.equals(cursor.getString(1)) && user_pw.equals(cursor.getString(2))){
                choice = true;
                Log.d("match","true");
            }

            Log.d("user","\tID: " + cursor.getString(0) + "\tname: " + cursor.getString(1) + "\tpw: " + cursor.getString(2));
        }
        return choice;
    }

    public boolean checkUserExist(String user_name){
        boolean choice = false;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM loginInfo",null);

        while(cursor.moveToNext()){
            if(user_name.equals(cursor.getString(1))){
                choice = true;
                Log.d("match","true");
            }
        }
        return choice;
    }

    public boolean addUser(String nameEntered, String passwordEntered){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", nameEntered);
        contentValues.put("PW",passwordEntered);

        long result = db.insert("loginInfo", null, contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean insertData(String name, String age){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", name);
        contentValues.put("QTY",age);

        long result = db.insert("ListInfo", null, contentValues);

        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public boolean deleteData(int user_id) {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery("Select * from ListInfo where ID = ?", new String[]{Integer.toString(user_id)});

        long result = DB.delete("ListInfo","ID=?",new String[]{Integer.toString(user_id)});
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public void QTYupdate(int user_id, String QTY) {
        SQLiteDatabase DB = this.getWritableDatabase();

        ContentValues ContentValues = new ContentValues();
        ContentValues.put("QTY", QTY);

        DB.update("ListInfo",ContentValues,"ID=?",new String[]{Integer.toString(user_id)});

        Log.d("ID found: ", "user ID: " + user_id + "\n QTY: " + QTY);
    }

    public ArrayList<item> getAllData(){

        ArrayList<item> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ListInfo",null);

        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String qty = cursor.getString(2);

            item Item = new item (id, name, qty);

            arrayList.add(Item);
        }
        return arrayList;
    }

}