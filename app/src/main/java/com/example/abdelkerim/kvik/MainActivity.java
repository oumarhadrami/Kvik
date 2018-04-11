package com.example.abdelkerim.kvik;

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
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    GridLayout mainGrid;
   LinearLayout logoContainer;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseAuth mfirebaseAuth;
    int flag=0;

    @Override
    protected void onStart() {
        super.onStart();
      /*  mAuthListner= (new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null)
                    flag=1;
                else
                    flag=0;
                Log.i("Whahatah",""+flag);

            }

        });
        mfirebaseAuth.addAuthStateListener(mAuthListner);
        */


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mainGrid =(GridLayout)findViewById(R.id.mainGrid);
        setSingleEvent(mainGrid);
        logoContainer=(LinearLayout)findViewById(R.id.logo_container);
        mfirebaseAuth =FirebaseAuth.getInstance();

       // mfirebaseAuth.addAuthStateListener(mAuthListner);
        /*Logo ImageButton */
        logoContainer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
 /*@Override
    public boolean onCreateOptionsMenu(Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_activity, menu);
        MenuItem item = menu.findItem(R.id.action_login);
        MenuItem itemn = menu.findItem(R.id.action_logout);
        if(flag==1) {

            item.setVisible(false);
            itemn.setVisible(true);

            Log.i("momomo",""+flag);
        }
        else{
            item.setVisible(true);
            itemn.setVisible(false);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            startActivity(new Intent(MainActivity.this, NumberVerifyActivity.class));

        }
        if(id==R.id.action_logout) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this,MainActivity.class));

        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_vision) {
            // Handle the vision action
            startActivity(new Intent(MainActivity.this,VisionActivity.class));
        } else if (id == R.id.nav_mission) {
            // Handle the vision mission action
            startActivity(new Intent(MainActivity.this,MissionActivity.class));
        } else if (id == R.id.nav_about) {
            // Handle the vision about action
            startActivity(new Intent(MainActivity.this,AboutActivity.class));
        } else if (id == R.id.nav_contacts) {
            // Handle the vision contact action
            startActivity(new Intent(MainActivity.this,ContactsActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i=0;i<mainGrid.getChildCount();i++){
            //you can see, all child item is carView , so we can cast   object to cardview
            CardView cardView= (CardView)mainGrid.getChildAt(i);
            final int final1 = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(final1==0)// open TrainingMethoACtivity
                    {
                        startActivity(new Intent(MainActivity.this,TrainingMethoActivity.class));
                    }


                    else  if(final1==1)// open EventsACtivity
                    {
                        int flag = checkConnectivity();
                        if(flag==1)
                            startActivity(new Intent(MainActivity.this,EventsActivity.class));
                        else
                            popConnectivityDialog();

                    }


                    else  if(final1==2)// open TrainingScheACtivity
                    {
                       int flag = checkConnectivity();
                        if(flag==1)
                        startActivity(new Intent(MainActivity.this,TrainingScheActivity.class));
                        else
                        popConnectivityDialog();
                    }


                    else  if(final1==3)// open TrainierReqACtivity
                    {
                        int flag = checkConnectivity();
                        if(flag==1)
                            startActivity(new Intent(MainActivity.this,TrainerReqActivity.class));
                        else
                            popConnectivityDialog();
                    }
                    else if(final1==4)// open SupportACtivity
                    {
                        startActivity(new Intent(MainActivity.this,SupportActivity.class));
                    }


                    else {
                        startActivity(new Intent(MainActivity.this,HelpActivity.class));
                    }

                }
            });
        }
    }

    private void popConnectivityDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this,R.style.MyDialogTrainer).create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setTitle("No conection");
        alertDialog.setMessage("Connect to internet Please");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }



private int checkConnectivity() {
    ConnectivityManager cm = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnected()) {
    return 1;
    }
    else return 0;
    }
    }

