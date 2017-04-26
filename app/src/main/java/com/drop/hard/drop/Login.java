package com.drop.hard.drop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email,password;
    TextView signin;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private  FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email= (EditText) findViewById(R.id.editText);
        password= (EditText) findViewById(R.id.editText2);
        signin= (TextView) findViewById(R.id.textView);
        sp= PreferenceManager.getDefaultSharedPreferences(Login.this);
        editor=sp.edit();
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }
    public void login(View view){
        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG", "signInWithEmail:onComplete:" + task.isSuccessful());
                        String uid=mAuth.getCurrentUser().getUid();
//                        editor.putString(Constants.CURRENT_UID,uid);
  //                      editor.putString(Constants.CURRENT_USER,mAuth.getCurrentUser().getEmail());
    //                    editor.commit();
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "signInWithEmail", task.getException());

                            Toast.makeText(Login.this, "Incorrect email or password!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            editor.putString(Constants.CURRENT_UID,uid);
                            editor.commit();
                            Intent intent=new Intent(Login.this, MainActivity.class);
                            intent.putExtra("uid", uid);
                            startActivity(intent);
                            finish();
                        }

                    }
                });
    }
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
