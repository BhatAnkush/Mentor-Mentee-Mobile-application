package com.example.siesattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AttendanceActivity extends AppCompatActivity {
    ListView lv;
    Button submit;
    Integer position=-1;
    public List<StudentInformation> stdinfo=new ArrayList<StudentInformation>();
    public HashMap<String,Integer> hp=new HashMap<String,Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Intent intent=getIntent();
        final String pos=intent.getStringExtra("position");
        position=Integer.parseInt(pos.trim());
        lv=(ListView)findViewById(R.id.listView2);
        submit=(Button)findViewById(R.id.button6);


        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = database.getReference("students"); // Change "students" to the appropriate reference path in your Firebase database
            ArrayList<String> studentNameList = new ArrayList<String>();
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {

                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            StudentInformation std = child.getValue(StudentInformation.class);

                            //check for subjects
                            if(std.getSubjects().get(Integer.parseInt(pos))!=-1)
                                stdinfo.add(std);
                            //Toast.makeText(AttendanceActivity.this, stdinfo.size() + "", Toast.LENGTH_SHORT).show();
                            Log.e("String is", std.getName());
                        }
                        fn(pos);
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
    void fn(String pos){
        int siz= stdinfo.size();

        MyBaseAdapter mba=new MyBaseAdapter(AttendanceActivity.this);
        lv.setAdapter(mba);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("String is", "Hello Dude");
                Toast.makeText(AttendanceActivity.this, "Hello Dude", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void submitAttendance(View view){
        int siz=stdinfo.size();

        // putting every students attendance details
        for(int i=0;i<siz;i++){
            try{
                hp.put(stdinfo.get(i).getRollno(),stdinfo.get(i).getSubjects().get(position));
            }catch(Exception e){
                Log.e("Exception is",e.toString());
            }
        }
        for(int i=0;i<siz;i++){
            View newView=lv.getChildAt(i);
            String rollno=((TextView)newView.findViewById(R.id.textView13)).getText().toString().trim();
            CheckBox cb=(CheckBox)newView.findViewById(R.id.checkBoxA);
            Integer totalAttendance=hp.get(rollno);
            if(cb.isChecked()){
                hp.put(rollno,totalAttendance+1001);
                Log.e(rollno, " is present, attendance is " + hp.get(rollno));
            }
            else{
                hp.put(rollno, totalAttendance+1000);
                Log.e(rollno," is absent");
            }
        }
        // Entering attendance in database
        try{
            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference databaseReference=database.getReference("students");
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {

                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for (DataSnapshot child : children) {
                            StudentInformation std = child.getValue(StudentInformation.class);
                            String rollno=std.getRollno();
                            if(hp.containsKey(rollno)){
                                int attendance=hp.get(rollno);
                                child.getRef().child("subjects").child(String.valueOf(position)).setValue(attendance);

                            }
                        }
                    } catch (Exception e) {
                        Log.e("Exception is", e.toString());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }catch(Exception e){
            Log.e("Exception is",e.toString());
        }
        finish();
        Toast.makeText(getApplicationContext(), "Attendance Complete", Toast.LENGTH_SHORT).show();

    }
    public class MyBaseAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;

        MyBaseAdapter(Context context){
            this.context=context;
            inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return stdinfo.size();
        }

        @Override
        public StudentInformation getItem(int position) {
            return stdinfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View view=inflater.inflate(R.layout.cust_list_attendance, null);
            final StudentInformation std=stdinfo.get(position);
            TextView tv1=(TextView)view.findViewById(R.id.textView12);
            tv1.setText(std.getName());
            TextView tv2=(TextView)view.findViewById(R.id.textView13);
            tv2.setText(std.getRollno());
            final CheckBox cb=(CheckBox)view.findViewById(R.id.checkBoxA);
            return view;
        }

    }
}
