package com.example.abdelkerim.kvik;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EventsActivity extends AppCompatActivity {

    private static final String TAG = "EventsActivity";
    LinearLayoutManager mLayoutManager;
    DatabaseReference eventDatabase;
    RecyclerView EventsList;
    TextView EventTitle, EventDescription,EventDate, PreviousEvents;
    ProgressDialog mProgress;
    FirebaseAuth mfirebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListner;

    private CalendarView  mCalendarView;

    @Override
    protected void onStart() {
        super.onStart();


       /* final FirebaseRecyclerAdapter<Events, EventsActivity.EventViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Events, EventsActivity.EventViewHolder>(
                Events.class,
                R.layout.singleeventlayout,
               EventViewHolder.class,
                eventDatabase
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, Events model, int position) {
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDescription(model.getDesccription());
                viewHolder.setEventTitle(model.getEvent());
            }

        };
        EventsList.setAdapter(firebaseRecyclerAdapter);
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Loading courses..");
        mProgress.show();
        final Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {
                mProgress.dismiss(); // when the task active then close the dialog
                t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
            }
        }, 4000);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*PreviousEvents=(TextView)findViewById(R.id.previousevents);
        EventsList = (RecyclerView) findViewById(R.id.events_list);
        EventTitle = (TextView)findViewById(R.id.trainertitle);
        EventDate = (TextView)findViewById(R.id.eventdate);
        EventDescription = (TextView)findViewById(R.id.trainerdescrption);
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(false);
        mLayoutManager.setStackFromEnd(false);
        EventsList.setHasFixedSize(true);
        EventsList.setLayoutManager(mLayoutManager);
        eventDatabase = FirebaseDatabase.getInstance().getReference().child("Events");

        PreviousEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://kaushalyakendra.org/blog/";

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });*/



        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });

        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        // Set first day of week to Monday, defaults to Monday so calling setFirstDayOfWeek is not necessary
        // Use constants provided by Java Calendar class
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);

        // Add event 1 on Sun, 07 Jun 2015 18:20:51 GMT
        Event ev1 = new Event(Color.GREEN, 1433701251000L, "Some extra data that I want to store.");
        compactCalendarView.addEvent(ev1);

        // Added event 2 GMT: Sun, 07 Jun 2015 19:10:51 GMT
        Event ev2 = new Event(Color.GREEN, 1433704251000L);
        compactCalendarView.addEvent(ev2);

        // Query for events on Sun, 07 Jun 2015 GMT.
        // Time is not relevant when querying for events, since events are returned by day.
        // So you can pass in any arbitary DateTime and you will receive all events for that day.
        List<Event> events = compactCalendarView.getEvents(1433701251000L); // can also take a Date object

        // events has size 2 with the 2 events inserted previously
        Log.d(TAG, "Events: " + events);

        // define a listener to receive callbacks when certain events happen.
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                Log.d(TAG, "Day was clicked: " + dateClicked + " with events " + events);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Log.d(TAG, "Month was scrolled to: " + firstDayOfNewMonth);
            }
        });


    }

    /*public static class EventViewHolder extends RecyclerView.ViewHolder {
        View mView;

        TextView EventDescription;
        TextView EventTitle;
        TextView EventDate;

        public EventViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            EventDate=(TextView)mView.findViewById(R.id.eventdate);
            EventTitle=(TextView)mView.findViewById(R.id.eventtitle);
            EventDescription =(TextView)mView.findViewById(R.id.eventdescrption);
        }


         public  void  setEventTitle(String Text){
             EventDate.setText(Text);
         }
        public void setTitle(String Text)
        {

            EventTitle.setText(Text);
        }

        public void setDescription(String Text)
        {

           EventDescription.setText(Text);
        }
    }*/

}
