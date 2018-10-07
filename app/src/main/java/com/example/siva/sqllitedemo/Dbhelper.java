package com.example.siva.sqllitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class Dbhelper extends SQLiteOpenHelper{

    private static final String table_name="example";
    private static final String col1 = "ID";
    private static final String col2="Name";
    private Context context;

    public Dbhelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE "+ table_name +" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+ col2 +" TEXT)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+table_name);
        onCreate(db);

    }

    public boolean addData(String item){
        Log.d("dbhelper","newItem: name is"+item);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col2, item);
        Log.d("dbhelper","addData: Adding "+item+" to "+table_name);
        long result = db.insert(table_name,null,cv);
        if(result==-1){
            Log.d("dbhelper","returning false");
            return false;
        }
        else {
            Log.d("dbhelper","returning true");
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+table_name;
        Cursor data=db.rawQuery(query,null);
        return data;
    }

    public Cursor getItemID(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+col1+" FROM "+table_name+" WHERE "+col2+" = '"+name+"'";
        Cursor data = db.rawQuery(query,null);
        return data;

    }

    public void updateName(String newName,int id, String oldName){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+table_name+" SET "+col2+" = '"+newName+"' WHERE "+col1+" = '"+id+"'"+" AND "+col2+" = '"+oldName+"'";
        Log.d("dbhelper","updateName: query: "+query);
        Log.d("dbhelper","updateName: Setting name to "+newName);
        db.execSQL(query);
    }

    public void deleteName(int id,String name){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM "+table_name+" WHERE "+col1+" = '"+id+"'"+" AND "+col2+" = '"+name+"'";
        Log.d("dbhelper","deleteName: query: "+query);
        Log.d("dbhelper","deleteName: Deleting "+name+" from the database");
        db.execSQL(query);
    }

    public void exportDB(){
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        FileChannel sourcef=null;
        FileChannel destinationf=null;
        Log.d("dbhelper","DB Name: "+getDatabaseName());
        String currentDBpath="/data/"+"com.example.siva.sqllitedemo"+"/databases/"+getDatabaseName();
        String backupDBpath=getDatabaseName()+".sql";
        String path=getDatabaseName();
        File currentDB = new File(data,currentDBpath);
        File backupDB=new File(sd,backupDBpath);
        File dir=new File(sd,path);
        if(!dir.exists()){
            dir.mkdir();
            Log.d("dbhelper","folder created");
        }
        else{
            Log.d("dbhelper","folder already exists");
        }
        try{
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source,0,source.size());
            String sourcepath=getDatabaseName()+".sql";
            File sfile=new File(sd,sourcepath);
            String ab=getDatabaseName()+".sql";
            File abc=new File(dir,ab);
            Log.d("dbhelper","Export DB: DB exported");
            try{
                sourcef = new FileInputStream(sfile).getChannel();
                destinationf = new FileOutputStream(abc).getChannel();
                destinationf.transferFrom(sourcef,0,sourcef.size());
                sourcef.close();
                destinationf.close();
                Log.d("dbhelper","Export DB to foler: Done");
            }
            catch (IOException e){
                e.printStackTrace();
            }
            source.close();
            destination.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
