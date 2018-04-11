package com.example.abdelkerim.kvik;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.karan.churi.PermissionManager.PermissionManager;


public class ContactsActivity extends AppCompatActivity {
    ImageButton callUsButton;
    ImageButton facebookLink;
    ImageButton emailUsButton;
    ImageButton ourLocationButton;
    PermissionManager permission;
    String phoneNumber = "+91(80)28429788";
    Intent intent;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //code for phone caling
        String uri = "tel:" + phoneNumber.trim() ;
        intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));


        permission=new PermissionManager() {};
        permission.checkAndRequestPermissions(this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });


        callUsButton = (ImageButton)findViewById(R.id.imageButton5);
        emailUsButton = (ImageButton)findViewById(R.id.imageButton6);
        ourLocationButton = (ImageButton)findViewById(R.id.imageButton7);
        facebookLink = (ImageButton)findViewById(R.id.imageButton8);


        callUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });


        emailUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"kaushalyakendra@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "");
                i.putExtra(Intent.EXTRA_TEXT   , "");
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(ContactsActivity.this, "There are no email clients installed.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ourLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.google.com/maps/dir//12.842159,77.586104/@12.8437433,77.514711,12z");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        facebookLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/kaushalyakendravikas/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        permission.checkResult(requestCode,permissions, grantResults);
    }
}
