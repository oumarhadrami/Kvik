package com.example.abdelkerim.kvik;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Credentials;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

public class TrainingScheActivity extends AppCompatActivity {
    LinearLayoutManager mLayoutManager;
    DatabaseReference coursesDatabase, mUserDataBase;
    RecyclerView CourseList;
    ImageView courseImage;
    TextView courseTitle,courseDescription,courseFees;
    ProgressDialog mProgress;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseAuth mfirebaseAuth;
    public String courseName;
    private Thread mThread;
     public   String CourseNamee;
    int flag = 0;

    @Override
    protected void onStart() {
        super.onStart();



        mUserDataBase = FirebaseDatabase.getInstance().getReference().child("Users");
        mUserDataBase.addListenerForSingleValueEvent(new ValueEventListener() {
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

            final FirebaseRecyclerAdapter<Course,CourseViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Course, CourseViewHolder>(
                    Course.class,
                    R.layout.singlecourselayout,
                    CourseViewHolder.class,
                    coursesDatabase
            ){
                @Override
                protected void populateViewHolder(CourseViewHolder viewHolder, Course model, final int position) {
                    viewHolder.setTitle(model.getTitle());
                    viewHolder.setDescription(model.getDesccription());
                    final String itemKey = getRef(position).getKey();




                    viewHolder.viewMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showCourseDetails(itemKey);
                        }
                    });

                    // put onclocklistener here for apply button
                    viewHolder.mapplyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                       //CourseNamee= getCourseName(itemKey);

                            mAuthListner=(new FirebaseAuth.AuthStateListener() {
                                @Override
                                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                    if(firebaseAuth.getCurrentUser()!=null){
                                        if (flag==1) {
                                            AlertDialog alertDialog = new AlertDialog.Builder(TrainingScheActivity.this,R.style.MyDialogTrainer).create();
                                            alertDialog.setCanceledOnTouchOutside(false);
                                            alertDialog.setTitle("You have Already Applied");
                                            alertDialog.setMessage("Please Email us for any quries");
                                            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "OK",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();

                                                        }
                                                    });
                                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL,"Email us",
                                                    new DialogInterface.OnClickListener(){
                                                        public void onClick(DialogInterface dialog, int which){
                                                            Intent i = new Intent(Intent.ACTION_SEND);
                                                            i.setType("message/rfc822");
                                                            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kaushalyakendra@gmail.com"});
                                                            i.putExtra(Intent.EXTRA_SUBJECT, "");
                                                            i.putExtra(Intent.EXTRA_TEXT   , "");
                                                            try {
                                                                startActivity(Intent.createChooser(i, "Send mail..."));
                                                            } catch (android.content.ActivityNotFoundException ex) {
                                                                Toast.makeText(getApplicationContext(),"There are no email clients installed.",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                            alertDialog.show();
                                            // Toast.makeText(getApplicationContext(),"YOooooo",Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {

                                            Intent i = new Intent(TrainingScheActivity.this,EnterYourDetailsActivity.class);
                                           CourseNamee = getCourseName(itemKey);
                                            i.putExtra("courseName",CourseNamee);
                                            Log.i("worknnnnn",""+courseName);
                                            startActivity(i);
                                    }
                                    }
                                    else{
                                        startActivity(new Intent(TrainingScheActivity.this, NumberVerifyActivity.class));
                                    }

                                }

                            });
                            mfirebaseAuth.addAuthStateListener(mAuthListner);

                        }
                    });
                }




            };



            CourseList.setAdapter(firebaseRecyclerAdapter);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading courses..");
        mProgress.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                mProgress.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 3000); // after 4 second (or 4000 miliseconds), the task will be active.






    }
        public String getCourseName(String itemKey) {

        DatabaseReference particularCourse = coursesDatabase.child(itemKey).child("title");

        particularCourse.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                courseName = dataSnapshot.getValue(String.class);
                Log.i("blablabom",""+courseName);
                Toast.makeText(getApplicationContext(),"You are Applying"+courseName,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return courseName;
    }


    // show Course Details
    private void showCourseDetails(String itemKey) {

        AlertDialog.Builder courseDetailsDiaolg = new AlertDialog.Builder(TrainingScheActivity.this);
        final View DetailsCard = LayoutInflater.from(TrainingScheActivity.this).inflate(R.layout.course_details, null);
        courseDetailsDiaolg.setView(DetailsCard);
        AlertDialog alertDialog = courseDetailsDiaolg.create();

        courseImage = (ImageView) DetailsCard.findViewById(R.id.courseimagedetails);
        courseTitle = (TextView) DetailsCard.findViewById(R.id.titledetails);
        courseDescription = (TextView) DetailsCard.findViewById(R.id.descriptiondetails);
        courseFees = (TextView) DetailsCard.findViewById(R.id.feesdetails);
        alertDialog.getWindow().setLayout(500, 400); //Controlling width and height.
        alertDialog.show();

        DatabaseReference courseDatabase = FirebaseDatabase.getInstance().getReference().child("courses").child(itemKey);

        courseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String courseTitleFromDB = (String) dataSnapshot.child("title").getValue();
                String courseDescriptionFromDB = (String) dataSnapshot.child("description").getValue();
                String courseFeesFromDB = (String) dataSnapshot.child("fees").getValue();
                String courseImageFromDB = (String) dataSnapshot.child("courseimage").getValue();

                courseTitle.setText(courseTitleFromDB);
                courseDescription.setText(courseDescriptionFromDB);
                courseFees.setText(courseFeesFromDB);
                Picasso.with(TrainingScheActivity.this).load(courseImageFromDB).into(courseImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_scheactivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CourseList = (RecyclerView) findViewById(R.id.course_list);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        mfirebaseAuth =FirebaseAuth.getInstance();
        CourseList.setHasFixedSize(true);
        CourseList.setLayoutManager(mLayoutManager);





            coursesDatabase = FirebaseDatabase.getInstance().getReference().child("courses");

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

    }
    // OnBackPressed
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TrainingScheActivity.this, MainActivity.class));
        /* your specific things...*/
        super.onBackPressed();
    }
    //Course View Holder

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        View mView;

        TextView courseDescription;
        TextView courseTitle;
        TextView viewMore;
        Button mapplyButton;

        public CourseViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            courseDescription =(TextView)mView.findViewById(R.id.coursedescrption);
            courseTitle=(TextView)mView.findViewById(R.id.coursetitle);
            viewMore = (TextView)mView.findViewById(R.id.viewmore);
            mapplyButton=(Button)mView.findViewById(R.id.apply_Button);

        }



        public void setTitle(String Text)
        {

            courseTitle.setText(Text);
        }

        public void setDescription(String Text)
        {

            courseDescription.setText(Text);
        }
    }



}
