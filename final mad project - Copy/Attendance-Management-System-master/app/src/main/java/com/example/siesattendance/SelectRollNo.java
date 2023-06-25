package com.example.siesattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SelectRollNo extends AppCompatActivity {

    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_roll_no);
        lv = findViewById(R.id.listView3);
        setAdapter();
    }

    protected void setAdapter() {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("students"); // Change "students" to the appropriate reference path in your Firebase database
            ArrayList<String> studentNameList = new ArrayList<String>();
            ArrayList<String> rollnumberlist = new ArrayList<String>();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            StudentInformation std1 = child.getValue(StudentInformation.class);
                            Log.d("Ankush", "onDataChange: "+std1.getPhone());
                            if (std1 != null) {
                                if(getIntent().getStringExtra("sem").equals(std1.getPhone())){
                                rollnumberlist.add(std1.getRollno());
                                studentNameList.add(std1.getName());
                                }
                            }
                        }
                        function2(studentNameList,rollnumberlist);
                    } catch (Exception e) {
                        Log.e("Exception is", e.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e("Database Error", databaseError.getMessage());
                }
            });

        } catch (Exception e) {
            Log.e("Exception is", e.toString());
        }
    }

    protected void function2(ArrayList<String> studentNameList, ArrayList<String> rollnumberlist) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, studentNameList);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                function3(studentNameList.get(i),rollnumberlist.get(i));
            }
        });
    }
    protected void function3(String sem, String att)
    {
        Intent intent=new Intent(this,StudentInformationActivity.class);
        intent.putExtra("Name","RollNo");
        intent.putExtra("RollNo",att);
        startActivity(intent);
    }
}
