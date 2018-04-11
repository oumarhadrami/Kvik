package com.example.abdelkerim.kvik;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.jar.Attributes;

public class EnterYourDetailsActivity extends AppCompatActivity {
TextView logoutTextView;
    Button mSubmitButton;
    EditText nameEditText;
    EditText ageEditText;
    EditText educationEditText;
    EditText emailEditText;
    DatabaseReference mUserDataBase;
    DatabaseReference mUserDetails;
    String name_editText,age_editText,email_editText,education_editText;
    FirebaseAuth myAuth;
    String CourseNamee;

    String user_id;

    @Override
    protected void onStart() {
        super.onStart();

        Intent ii = getIntent();
        Bundle extras = ii.getExtras();
        //
        //
        //
        // if (extras != null)
            CourseNamee = extras.getString("courseName");
            Log.i("Yohoho",""+ CourseNamee);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_your_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        mSubmitButton = (Button) findViewById(R.id.submit_button);
        logoutTextView = (TextView) findViewById(R.id.differentuser_textView);
        nameEditText = (EditText) findViewById(R.id.name_editText);
        ageEditText = (EditText) findViewById(R.id.age_editText);
        educationEditText = (EditText) findViewById(R.id.education_editText);
        emailEditText = (EditText) findViewById(R.id.email_editText);
        myAuth = FirebaseAuth.getInstance();

        if(myAuth.getCurrentUser()!=null)
        user_id = myAuth.getCurrentUser().getUid();
        mUserDataBase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TrainingScheActivity.class));
            }
        });


           /* itemkey = getIntent().getExtras().getString("itemKey");
       mUserDataBase.child(itemkey).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               String nameToDB = (String) dataSnapshot.child("Name").getValue();
               String educationToDB = (String) dataSnapshot.child("Education").getValue();
               String ageToDB = (String) dataSnapshot.child("Age").getValue();
               String emailToDB = (String) dataSnapshot.child("Email").getValue();



               nameEditText.setText(nameToDB);
               educationEditText.setText(educationToDB);
               ageEditText.setText(ageToDB);
               emailEditText.setText(emailToDB);


           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });
       */

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterUserDetails();

            }
        });

        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(EnterYourDetailsActivity.this, NumberVerifyActivity.class));
            }
        });


    }

    private void enterUserDetails(){
        initialize();
        final String nameToDB=nameEditText.getText().toString();
        final String educationToDB=educationEditText.getText().toString();
        final String ageToDB=ageEditText.getText().toString();
        final String emailToDB=emailEditText.getText().toString();

        final String UID = myAuth.getCurrentUser().getUid();

        if (!validate()) {

            if (nameToDB.isEmpty() || ageToDB.isEmpty() || educationToDB.isEmpty() || emailToDB.isEmpty()) {
                Toast.makeText(this, "fill out all the fields", Toast.LENGTH_SHORT).show();

            }
            }
            else {
                mUserDetails = mUserDataBase.push();
                mUserDataBase.child("Name").setValue(nameToDB);
                mUserDataBase.child("Education").setValue(educationToDB);
                mUserDataBase.child("Age").setValue(ageToDB);
                mUserDataBase.child("Email").setValue(emailToDB);
                mUserDataBase.child("UID").setValue(UID);
                mUserDataBase.child("courseName").setValue(CourseNamee);
            AlertDialog alertDialog = new AlertDialog.Builder(EnterYourDetailsActivity.this,R.style.MyDialogTrainer).create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setTitle("Successfully Submitted!");
            alertDialog.setMessage("we will get back to you soon\n"+"Thank You.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(EnterYourDetailsActivity.this,MainActivity.class));
                            dialog.dismiss();

                        }
                    });
            alertDialog.show();

            //add receive email feature here



            }

        }
    public  void initialize(){
        name_editText = nameEditText.getText().toString().trim();
        education_editText= educationEditText.getText().toString().trim();
        age_editText= ageEditText.getText().toString().trim();
        email_editText= emailEditText.getText().toString().trim();
    }

    public  boolean validate() {
        boolean valid = true;
        if (name_editText.isEmpty() || name_editText.length() > 30) {
            nameEditText.setError("Enter valid name");
            valid = false;
        }
        if (education_editText.isEmpty() || education_editText.length()<3 || age_editText.length()>20) {
            educationEditText.setError("check your education");
            valid=false;
        }
        if (age_editText.isEmpty() || age_editText.length()!=2) {
            ageEditText.setError("Enter valid Age");
            valid=false;
        }
        if (email_editText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email_editText).matches()) {
            emailEditText.setError("Enter valid Email");
            valid=false;
        }
        return valid;

    }

    @Override
    public void onBackPressed() {
        FirebaseAuth.getInstance().signOut();
        /* your specific things...*/
       startActivity(new Intent(EnterYourDetailsActivity.this,TrainingScheActivity.class));
       super.onBackPressed();
    }

}
