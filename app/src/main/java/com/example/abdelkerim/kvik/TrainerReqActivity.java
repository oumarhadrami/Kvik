package com.example.abdelkerim.kvik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class TrainerReqActivity extends AppCompatActivity {

    LinearLayoutManager mLayoutManager;
    DatabaseReference trainerrequirementDatabase , mTrainerDataBase;
    RecyclerView trainerList;
    TextView trainerTitle, trainerDescription;
    ProgressDialog mProgress;
    FirebaseAuth mfirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    int flag=0;





    @Override
    protected void onStart() {
        super.onStart();


        mTrainerDataBase = FirebaseDatabase.getInstance().getReference().child("Trainers");
        mTrainerDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    if (snapshot.child("UID").getValue().toString().equals(mfirebaseAuth.getCurrentUser().getUid()))
                        flag = 1;
                    else
                        flag = 0;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        final FirebaseRecyclerAdapter<Course, CourseViewHolderr> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Course, CourseViewHolderr>(
                Course.class,
                R.layout.singletrainerlayout,
                CourseViewHolderr.class,
                trainerrequirementDatabase) {
            @Override
            protected void populateViewHolder(CourseViewHolderr viewHolder, Course model, final int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDesccription());

                viewHolder.mApplyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAuthListner = (new FirebaseAuth.AuthStateListener() {
                            @Override
                            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                if (firebaseAuth.getCurrentUser() != null) {
                                    if (flag == 1) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(TrainerReqActivity.this, R.style.MyDialogTrainer).create();
                                        alertDialog.setCanceledOnTouchOutside(false);
                                        alertDialog.setTitle("You have Already Applied");
                                        alertDialog.setMessage("Please Email us for any changes");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                    }
                                                });
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Email us",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent i = new Intent(Intent.ACTION_SEND);
                                                        i.setType("message/rfc822");
                                                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"kaushalyakendra@gmail.com"});
                                                        i.putExtra(Intent.EXTRA_SUBJECT, "");
                                                        i.putExtra(Intent.EXTRA_TEXT, "");
                                                        try {
                                                            startActivity(Intent.createChooser(i, "Send mail..."));
                                                        } catch (android.content.ActivityNotFoundException ex) {
                                                            Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                        alertDialog.show();
                                        // Toast.makeText(getApplicationContext(),"YOooooo",Toast.LENGTH_SHORT).show();
                                    } else {
                                        startActivity(new Intent(TrainerReqActivity.this, TrainerDetails.class));
                                /*Intent i = new Intent(TrainerReqActivity.this,EnterYourDetailsActivity.class);
                                CourseNamee = getCourseName(itemKey);
                                i.putExtra("courseName",CourseNamee);
                                Log.i("worknnnnn",""+courseName);
                                startActivity(i);*/
                                    }
                                } else {
                                    startActivity(new Intent(TrainerReqActivity.this, NumberVerifyActivity.class));
                                }

                            }


                        });

                        mfirebaseAuth.addAuthStateListener(mAuthListner);


                    }
                });
            }

        };


        trainerList.setAdapter(firebaseRecyclerAdapter);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading courses..");
        mProgress.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                mProgress.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 4000);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer_req);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mfirebaseAuth =FirebaseAuth.getInstance();

        trainerList = (RecyclerView) findViewById(R.id.trainer_list);
        trainerTitle = (TextView)findViewById(R.id.trainertitle);
        trainerDescription = (TextView)findViewById(R.id.trainerdescrption);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        trainerList.setHasFixedSize(true);
        trainerList.setLayoutManager(mLayoutManager);


        trainerrequirementDatabase = FirebaseDatabase.getInstance().getReference().child("require");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }



    public static class CourseViewHolderr extends RecyclerView.ViewHolder {
        View mView;

        TextView trainerDescription;
        TextView trainerTitle;
        Button mApplyButton;


        public CourseViewHolderr(View itemView) {
            super(itemView);

            mView = itemView;

            trainerDescription =(TextView)mView.findViewById(R.id.trainerdescrption);
            trainerTitle=(TextView)mView.findViewById(R.id.trainertitle);
            mApplyButton=(Button)mView.findViewById(R.id.tapply_Button);
        }



        public void setTitle(String Text)
        {

            trainerTitle.setText(Text);
        }

        public void setDescription(String Text)
        {

            trainerDescription.setText(Text);
        }
    }

}


