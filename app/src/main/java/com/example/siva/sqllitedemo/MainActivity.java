package com.example.siva.sqllitedemo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    Dbhelper mDatabaseHelper;
    private Button add,view,exp;
    private EditText editText;
    public boolean mPermissionGranted=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new Dbhelper(MainActivity.this,"example",null,1);
        exp=(Button) findViewById(R.id.buttonexp);
        editText = (EditText) findViewById(R.id.name);
        add = (Button) findViewById(R.id.add);
        view = (Button) findViewById(R.id.view);
        permissionCheck();
        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPermissionGranted){
                    mDatabaseHelper.exportDB();
                }
                else {
                    permissionCheck();
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = editText.getText().toString();
                if(editText.length() != 0){
                    addDataM(newEntry);
                    editText.setText("");
                }
                else{
                    toastMessage("Text field should not be empty");
                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ListData.class);
                startActivity(intent);
            }
        });

    }

    public void addDataM(String newEntry){
        Log.d("MainActivity","newEntry: name is "+newEntry);
        boolean insertData = mDatabaseHelper.addData(newEntry);
        if (insertData){
            toastMessage("Data Successfully Inserted! ");
        }
        else{
            toastMessage("Something went wrong");
        }
    }

    public void permissionCheck(){
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
        if(ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(MainActivity.this.getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                mPermissionGranted = true;
                return;
            }
            else{
                ActivityCompat.requestPermissions(MainActivity.this,permissions,1234);
            }
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1234);
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissionGranted=false;
        switch (requestCode){
            case 1234:{
                if(grantResults.length>0){
                    for(int i=0;i<grantResults.length;i++){
                        if (grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            mPermissionGranted=false;
                            return;
                        }
                    }
                    mPermissionGranted=true;
                }
            }
        }
    }
}
