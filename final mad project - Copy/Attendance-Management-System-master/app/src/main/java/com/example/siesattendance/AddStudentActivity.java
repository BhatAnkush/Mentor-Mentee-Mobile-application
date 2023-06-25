package com.example.siesattendance;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;

public class AddStudentActivity extends AppCompatActivity {
    private EditText et_user_id, et_name, et_roll_no, et_email, et_password;
    ProgressDialog pd;
    Button b5;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8;
    FirebaseDatabase firebaseDatabase;
    String[] departmentOptions = {"-SELECT-", "ISE", "CSE", "MECH", "ECE", "DS", "AIML"};
    String[] semesterOptions = {"-SELECT-","1", "2", "3", "4", "5", "6", "7", "8"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        et_user_id = findViewById(R.id.editText5);
        et_name = findViewById(R.id.editText6);
        et_roll_no = findViewById(R.id.editText7);
        et_email = findViewById(R.id.editText10);
        et_password = findViewById(R.id.editText11);
        Spinner spinnerDept = findViewById(R.id.spinner_dept);
        Spinner spinnerSemester = findViewById(R.id.spinner_semester);
        b5 = findViewById(R.id.button5);

        ArrayAdapter<String> adapterDept = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, departmentOptions);
        spinnerDept.setAdapter(adapterDept);

        ArrayAdapter<String> adapterSemester = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, semesterOptions);
        spinnerSemester.setAdapter(adapterSemester);

        firebaseAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(this);
        if (firebaseAuth.getCurrentUser() != null) {
            Toast.makeText(AddStudentActivity.this, "Already signed in", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

        cb1 = findViewById(R.id.checkBox1);
        cb2 = findViewById(R.id.checkBox2);
        cb3 = findViewById(R.id.checkBox3);
        cb4 = findViewById(R.id.checkBox4);
        cb5 = findViewById(R.id.checkBox5);
        cb6 = findViewById(R.id.checkBox6);
        cb7 = findViewById(R.id.checkBox7);
        cb8 = findViewById(R.id.checkBox8);
    }

    String dept;

    public void fnRegister(View view) {
        String userid = et_user_id.getText().toString().trim();
        String name = et_name.getText().toString().trim();
        String rollno = et_roll_no.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        Spinner spinnerDept = findViewById(R.id.spinner_dept);
        Spinner spinnerSemester = findViewById(R.id.spinner_semester);
        // Retrieve the selected department and semester from the Spinners
        dept = spinnerDept.getSelectedItem().toString();
        String semester = spinnerSemester.getSelectedItem().toString();

        // Checking checkboxes
        ArrayList<Integer> subject = new ArrayList<>(8);
        for (int i = 0; i < 8; i++)
            subject.add(0);
        if (!cb1.isChecked())
            subject.set(0, -1);
        if (!cb2.isChecked())
            subject.set(1, -1);
        if (!cb3.isChecked())
            subject.set(2, -1);
        if (!cb4.isChecked())
            subject.set(3, -1);
        if (!cb5.isChecked())
            subject.set(4, -1);
        if (!cb6.isChecked())
            subject.set(5, -1);
        if (!cb7.isChecked())
            subject.set(6, -1);
        if (!cb8.isChecked())
            subject.set(7, -1);
        int sum = 0;
        for (int i = 0; i < 8; i++)
            sum = sum + subject.get(i);
        if (sum == -8) {
            Toast.makeText(AddStudentActivity.this, "Please select subjects", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(userid) || TextUtils.isEmpty(name) || TextUtils.isEmpty(rollno) ||
                TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(AddStudentActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(AddStudentActivity.this, "Password should contain at least 6 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(AddStudentActivity.this, "Validation Successful", Toast.LENGTH_SHORT).show();

        pd.setMessage("Registering User...");
        pd.setCancelable(false);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.show();

        try {
            final StudentInformation stdinfo = new StudentInformation(name, rollno, dept, semester, email, password, subject);
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(AddStudentActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                        saveInformation(stdinfo);
                        pd.dismiss();
                    } else {
                        Toast.makeText(AddStudentActivity.this, "Unable to register", Toast.LENGTH_SHORT).show();
                        Log.e("Exception is", task.getException().toString());
                        pd.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            Log.e("Exception is ", e.toString());
        }
    }

    public void saveInformation(StudentInformation stdinfo) {
        try {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                databaseReference = FirebaseDatabase.getInstance().getReference().child("students");
                databaseReference.child(user.getUid()).setValue(stdinfo);
            }
        } catch (Exception e) {
            Log.e("Exception is", e.toString());
        }

        firebaseAuth.signOut();
    }
}
