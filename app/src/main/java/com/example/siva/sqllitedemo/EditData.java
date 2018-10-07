package com.example.siva.sqllitedemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditData extends AppCompatActivity {

    private static final String TAG = "EditDataActivity";
    private Button btnsave,btndelete,btnview;
    private EditText edittable_item;
    Dbhelper mDatabaseHelper;
    private String selectedName;
    private int selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_data);

        btnview = (Button) findViewById(R.id.buttonView);
        btnsave = (Button) findViewById(R.id.buttonsave);
        btndelete = (Button) findViewById(R.id.buttondel);
        edittable_item = (EditText) findViewById(R.id.editText);
        mDatabaseHelper = new Dbhelper(this,"example",null,1);
        Intent recintent = getIntent();
        selectedID = recintent.getIntExtra("id",-1);
        selectedName = recintent.getStringExtra("name");
        edittable_item.setText(selectedName);
        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String item = edittable_item.getText().toString();
                if(!item.equals("")){
                    mDatabaseHelper.updateName(item,selectedID,selectedName);
                }else{
                    toastMessage("You must enter a name");
                }
            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseHelper.deleteName(selectedID,selectedName);
                edittable_item.setText("");
                toastMessage("Removed from database");
            }
        });
        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EditData.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }
}
