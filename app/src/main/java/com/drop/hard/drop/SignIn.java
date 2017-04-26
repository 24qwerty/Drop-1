package com.drop.hard.drop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private EditText mEmail,mPassword,mPasswordR,mName,mContactNumber;
    Button signin;
    TextView login;
    private FirebaseAuth mAuth;
    private ProgressDialog mAuthProgressDialog;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mDatabase;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        mEmail= (EditText) findViewById(R.id.editTexte);
        mName= (EditText) findViewById(R.id.editTextn);
        mContactNumber= (EditText) findViewById(R.id.editTextc);
        signin= (Button) findViewById(R.id.buttonsignin);
        mAuthProgressDialog = new ProgressDialog(this);
        mPassword= (EditText) findViewById(R.id.editTextp);
        sp= PreferenceManager.getDefaultSharedPreferences(SignIn.this);
        if(sp.getString(Constants.CURRENT_UID,"").length()!=0){
            Intent intent=new Intent(SignIn.this,MainActivity.class);
            startActivity(intent);
        }
        Firebase.setAndroidContext(this);
        mPasswordR= (EditText) findViewById(R.id.editTextrp);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        login= (TextView) findViewById(R.id.textView);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignIn.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signinX();
            }
        });
        mAuthProgressDialog.setTitle(getResources().getString(R.string.progress_dialog_loading));
        mAuthProgressDialog.setMessage("Processing");
        mAuthProgressDialog.setCancelable(false);
    }
    void signinX(){
        final String email_s=mEmail.getText().toString();
        final String name_s=mName.getText().toString();
        final String contact_s=mContactNumber.getText().toString();
        String password_s=mPassword.getText().toString();
        boolean validEmail = isEmailValid(email_s);
        boolean validPassword = isPasswordValid(password_s);
        if (!validEmail || !validPassword) return;
        if(mPasswordR.getText().toString().equals(password_s)) {
            mAuthProgressDialog.show();
            mAuth.createUserWithEmailAndPassword(email_s, password_s)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            if (task.isSuccessful()) {
                                Log.d(TAG, "success");
                                mAuthProgressDialog.dismiss();
                                mDatabase = FirebaseDatabase.getInstance().getReference();
                                String uid = task.getResult().getUser().getUid();
                                User newUser = new User(name_s, email_s, contact_s, 0, 0, uid);
                                UserLocation userLocation=new UserLocation(name_s, email_s, contact_s,uid,false, 0, 0, "GENERAL","");
                                Intent intent = new Intent(SignIn.this, AddLocation.class);
                                mDatabase.child("users").child(newUser.getUid()).setValue(newUser);
                                mDatabase.child("users").child("User_Location").child(newUser.getUid()).setValue(userLocation);
                                intent.putExtra("uid", uid);
                                finish();
                                startActivity(intent);
                            }

                            if (!task.isSuccessful()) {
                                mAuthProgressDialog.dismiss();
                                Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            mPasswordR.setError("Password doesn't match");
            return;
        }
    }
    private boolean isEmailValid(String email) {
        boolean isGoodEmail =(email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches());
        if (!isGoodEmail) {
            mEmail.setError(getResources().getString(R.string.error_invalid_email_not_valid));
            return false;
        }
        return isGoodEmail;
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 6) {
            mPassword.setError(getResources().getString(R.string.error_invalid_password_not_valid));
            return false;
        }
        return true;
    }
}
