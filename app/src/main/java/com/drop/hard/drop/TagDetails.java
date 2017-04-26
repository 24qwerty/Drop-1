package com.drop.hard.drop;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class TagDetails extends AppCompatActivity {
    TextView name,tag,email,number,description;
    Button call,message,emailb;
    static UserLocation tag1;
    Bundle bundle;
    SharedPreferences sp;
    private  static Firebase ref;
    Query queryRef;
    private com.google.firebase.database.ChildEventListener listener;
    DatabaseReference databaseReference;
    static String tag_id;
    String senderEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_details);
        sp= PreferenceManager.getDefaultSharedPreferences(TagDetails.this);
        name= (TextView) findViewById(R.id.tvNameD);
        description= (TextView) findViewById(R.id.tvDescriptionD);
        Firebase.setAndroidContext(this);
        tag= (TextView) findViewById(R.id.tvProfessionD);
        email= (TextView) findViewById(R.id.tvEmailD);
        number= (TextView) findViewById(R.id.tvContactD);
        call= (Button) findViewById(R.id.btCallD);
        emailb= (Button) findViewById(R.id.btEmailD);
        message= (Button) findViewById(R.id.btnMessageD);
        Intent i=getIntent();
        Bundle b=i.getExtras();
        senderEmail=b.getString("senderEmail").toString();

        databaseReference= FirebaseDatabase.getInstance().getReference();
        bundle=getIntent().getExtras();
        ref = new Firebase("https://drop-8f1b1.firebaseio.com/").child("users").child("User_Location");
        Log.e("O",bundle.get("email").toString());
        databaseReference=databaseReference.child("users").child("User_Location");
        queryRef = databaseReference.orderByChild("email").equalTo((String) bundle.get("email"));
         listener=queryRef.addChildEventListener(new com.google.firebase.database.ChildEventListener() {
            @Override
            public void onChildAdded(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {
                tag1=dataSnapshot.getValue(UserLocation.class);
                name.setText(tag1.getName());
                tag.setText(tag1.getCategory());
                email.setText(tag1.getEmail());
                number.setText(tag1.getContact_number());
                description.setText(tag1.getDescription());
            }

            @Override
            public void onChildChanged(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(com.google.firebase.database.DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(com.google.firebase.database.DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Intent.ACTION_DIAL);//, Uri.parse(num.getText().toString()));
                in.setData(Uri.parse("tel:"+tag1.getContact_number()));
                try {
                    startActivity(in);
                }
                catch(ActivityNotFoundException a){
                }
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Send SMS", "");
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setData(Uri.parse("smsto:"));
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address"  , tag1.getContact_number());
                smsIntent.putExtra("sms_body"  , "");
                try {
                    startActivity(smsIntent);
                    finish();
                    Log.i("Finished sending SMS...", "");
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(TagDetails.this,
                            "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        emailb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail2(email.getText().toString(),senderEmail);
            }
        });
    }



    protected void sendEmail2(String to,String from) {
        Log.i("Send email", "");
        String[] TO = {""};
        String[] CC = {""};
        TO[0]=to;
        CC[0]=from;
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(TagDetails.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        queryRef.removeEventListener(listener);
        super.onDestroy();
    }
}
