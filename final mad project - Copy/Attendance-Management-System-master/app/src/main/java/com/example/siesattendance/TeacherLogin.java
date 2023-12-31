package com.example.siesattendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TeacherLogin extends AppCompatActivity {
    EditText userId,password;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_login);
        userId=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        button=(Button)findViewById(R.id.button3);
    }
    public void doOnClick(View view) {
        String user=userId.getText().toString().trim();
        String pass=password.getText().toString().trim();
        if(user.equals("Mentor")&&pass.equals("Mentor")){
            Intent intent=new Intent(TeacherLogin.this,TeacherActivity.class);
            startActivity(intent);
            Toast.makeText(TeacherLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(TeacherLogin.this, "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }

}
