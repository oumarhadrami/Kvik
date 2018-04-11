package com.example.abdelkerim.kvik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

public class TrainerDetails extends AppCompatActivity {

    DatabaseReference mTrainerDataBase, mTrainerDetails;
    FirebaseAuth myAuth;
    Button mTSubmitButton,mCvButton;
    EditText tnameEditText,teducationEditText,temailEditText,tageEditText;
    String name_editText,email_editText,age_editText,education_editText;
    String trainer_id;
    TextView fileNameTextView;
    String UID;

    Uri downloadUrl;
    Uri filePath = null;
    final int PICK_IMAGE_REQUEST = 71;
    StorageReference storage;
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        storage = FirebaseStorage.getInstance().getReference();
        tnameEditText = (EditText) findViewById(R.id.tname_editText);
        teducationEditText = (EditText) findViewById(R.id.teducation_editText);
        temailEditText = (EditText) findViewById(R.id.temail_editText);
        tageEditText=(EditText)findViewById(R.id.tage_editText);
        mTSubmitButton = (Button) findViewById(R.id.tsubmit_button);
        mCvButton=(Button)findViewById(R.id.cv_button);
        fileNameTextView = (TextView)findViewById(R.id.file_name);

        myAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
         //UID = myAuth.getCurrentUser().getUid();
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),TrainerReqActivity.class));
            }
        });


        if(myAuth.getCurrentUser()!=null)
            trainer_id = myAuth.getCurrentUser().getUid();

        mTrainerDataBase = FirebaseDatabase.getInstance().getReference().child("Trainers").child(trainer_id);

        mTSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                enterTrainerDetails();

            }
        });
        mCvButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();

            }
        });

    }

    ProgressDialog progressDialog ;

    private void chooseFile() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_IMAGE_REQUEST);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Cursor returnCursor =
                    getContentResolver().query(filePath, null, null, null, null);
            /*
     * Get the column indexes of the data in the Cursor,
     * move to the first row in the Cursor, get the data,
     * and display it.
     */
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            fileNameTextView.setText(returnCursor.getString(nameIndex));



            /*try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            */
        }
    }
    private void enterTrainerDetails() {



            initialize();
            final String nameToDB=tnameEditText.getText().toString();
            final String educationToDB=teducationEditText.getText().toString();
            final String emailToDB=temailEditText.getText().toString();
             final String ageTODB=tageEditText.getText().toString();
             final String UID = myAuth.getCurrentUser().getUid();



            if (!validate()) {
                if (nameToDB.isEmpty() ||ageTODB.isEmpty() || educationToDB.isEmpty() || emailToDB.isEmpty()) {
                    Toast.makeText(this, "fill out all the fields", Toast.LENGTH_SHORT).show();

                }
            }
            else {


                if(filePath != null)
                {

                    storageReference = storage.child("CVS").child(UID);



                    mTrainerDetails =  mTrainerDataBase.push();
                    mTrainerDataBase.child("Name").setValue(nameToDB);
                    mTrainerDataBase.child("Education").setValue(educationToDB);
                    mTrainerDataBase.child("Email").setValue(emailToDB);
                    mTrainerDataBase.child("Age").setValue(ageTODB);
                    mTrainerDataBase.child("UID").setValue(UID);


                    storageReference.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    downloadUrl = taskSnapshot.getDownloadUrl();
                                    Log.i("Herroo",""+downloadUrl);
                                    mTrainerDataBase.child("CV").setValue(downloadUrl.toString());
                                    progressDialog.dismiss();
                                    AlertDialog alertDialog = new AlertDialog.Builder(TrainerDetails.this,R.style.MyDialogTrainer).create();
                                    alertDialog.setCanceledOnTouchOutside(false);
                                    alertDialog.setTitle("Successfully Submitted!");
                                    alertDialog.setMessage("we will get back to you soon\n"+"Thank You.");
                                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    startActivity(new Intent(TrainerDetails.this,TrainerReqActivity.class));
                                                    dialog.dismiss();

                                                }
                                            });
                                    alertDialog.show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(TrainerDetails.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                                    progressDialog.show();
                                }
                            });


                }





            }

        }
    private static final String ALLOWED_CHARACTERS ="0123456789qwertyuiopasdfghjklzxcvbnm";
    private static String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


    public  void initialize(){
        name_editText = tnameEditText.getText().toString().trim();
        education_editText= teducationEditText.getText().toString().trim();
        email_editText= temailEditText.getText().toString().trim();
        age_editText=tageEditText.getText().toString().trim();
    }

    public  boolean validate() {
        boolean valid = true;
        if (name_editText.isEmpty() || name_editText.length() > 30) {
            tnameEditText.setError("Enter valid name");
            valid = false;
        }
        if (age_editText.isEmpty() || age_editText.length() != 2) {
           tageEditText.setError("Enter valid age");
            valid = false;
        }
        if (education_editText.isEmpty() || education_editText.length()<3) {
            teducationEditText.setError("check your education");
            valid=false;
        }

        if (email_editText.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email_editText).matches()) {
            temailEditText.setError("Enter valid Email");
            valid=false;
        }
        return valid;

    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TrainerDetails.this, TrainerReqActivity.class));
        /* your specific things...*/
        super.onBackPressed();
    }

}


