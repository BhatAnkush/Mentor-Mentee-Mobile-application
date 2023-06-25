package com.example.siesattendance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

    public class sem_select extends AppCompatActivity {

        ListView lv;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_select_roll_no);
            lv = findViewById(R.id.listView3);
            setAdapter();
        }

        protected void setAdapter() {
            ArrayList<String> studentNameList = new ArrayList<String>();
            for (int i=1;i<=8;i++)
                studentNameList.add(String.valueOf(i));
            function2(studentNameList);
        }

        protected void function2(ArrayList<String> studentNameList) {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, studentNameList);
            lv.setAdapter(arrayAdapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    function3(studentNameList.get(i));
                }
            });

        }
        protected void function3(String sem )
        {
            Intent intent=new Intent(this,SelectRollNo.class);
            intent.putExtra("sem",sem);
            startActivity(intent);
        }
    }


