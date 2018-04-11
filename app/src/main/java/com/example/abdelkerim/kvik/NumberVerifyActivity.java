package com.example.abdelkerim.kvik;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class NumberVerifyActivity extends AppCompatActivity {
    Button SendOtpButton, verifyOTP;
    FirebaseAuth mfirebaseAuth;
    EditText enterOtp, enterYourNumber;
    String verification_code;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_verify);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        enterYourNumber=(EditText)findViewById(R.id.enterno_editText);
        enterOtp = (EditText) findViewById(R.id.enter_otp);
        SendOtpButton=(Button)findViewById(R.id.sendotp_button);
        verifyOTP=(Button)findViewById(R.id.verify_otp);
        verifyOTP.requestFocus();
        mfirebaseAuth =FirebaseAuth.getInstance();




        mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
              mfirebaseAuth.signInWithCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(NumberVerifyActivity.this,"Incorrect Phone Number "+ e.getMessage(),Toast.LENGTH_SHORT).show();


            }
            // method called when code is sent
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verification_code=s;
                Toast.makeText(getApplicationContext(),"OTP Sent",Toast.LENGTH_SHORT).show();
                if(!verification_code.equals("")) {
                    verifyOTP.setEnabled(true);
                }
            }
            //called after timeout if  onVerificationCompleted had not been called
            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(NumberVerifyActivity.this,"TimeOut Please try again "+s,Toast.LENGTH_SHORT).show();
            }
        };

    }
    // send sms button

    public  void send_sms(View view){
        enterYourNumber.clearFocus();
        enterOtp.requestFocus();
        String phoneNumber= enterYourNumber.getText().toString();
       if(!phoneNumber.equals(""))
        {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    // Phone number to verify
                    // Timeout duration
                    // Unit of timeout
                    // Activity (for callback binding)
                    // OnVerificationStateChangedCallbacks
                    phoneNumber, 60, TimeUnit.SECONDS, this, mCallback);


            new CountDownTimer(60000, 1000) {

                public void onTick(long millisUntilFinished) {
                    SendOtpButton.setEnabled(false);
                    SendOtpButton.setText("remaining: " + millisUntilFinished / 1000);
                }

                public void onFinish() {
                    SendOtpButton.setText("SEND OTP");
                    SendOtpButton.setEnabled(true);
                }
            }.start();





        }

    }
    public void signInWithAuthCredential(PhoneAuthCredential phoneAuthCredential){
        mfirebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Number Verified",Toast.LENGTH_SHORT).show();
                           // startActivity(new Intent(NumberVerifyActivity.this,EnterYourDetailsActivity.class));
                            TextView errorTextView =  (TextView)findViewById(R.id.errortextview);
                            errorTextView.setVisibility(View.GONE);

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Number Not Verified",Toast.LENGTH_SHORT).show();
                            TextView errorTextView =  (TextView)findViewById(R.id.errortextview);
                            errorTextView.setVisibility(View.VISIBLE);

                        }
                    }
                });
    }
    public void verifyPhoneNumber(String verifyOtp, String input_code){
        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(verifyOtp,input_code);
        signInWithAuthCredential(phoneAuthCredential);

    }
    //called when Verify OTP button is called
    public void verify_otp(View view){
        enterOtp.clearFocus();
        enterYourNumber.clearFocus();
        String input_code=enterOtp.getText().toString();

            if (!input_code.equals("") && input_code.equals(verification_code)) {
                verifyPhoneNumber(verification_code, input_code);
                Log.i("BLALAL","Test");
                startActivity(new Intent(NumberVerifyActivity.this,EnterYourDetailsActivity.class));

            }





    }
}
