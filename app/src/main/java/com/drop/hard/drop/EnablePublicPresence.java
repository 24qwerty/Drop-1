package com.drop.hard.drop;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EnablePublicPresence extends AppCompatActivity {
    Spinner spinner;
    Switch aSwitch;
    Button button;
    EditText description;
    DatabaseReference databaseReference;
    String itemSelected,uid;
    boolean getpublic;
    ValueEventListener l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable_public_presence);
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        uid= (String) b.get("uid");
        Log.e("uid",uid);
        spinner = (Spinner) findViewById(R.id.spinner);
        aSwitch= (Switch) findViewById(R.id.switch1);
        button= (Button) findViewById(R.id.button4);
        description= (EditText) findViewById(R.id.editTextDescription);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserLocation();
            }
        });
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    getpublic=true;
                }
            }
        });
        // Spinner click listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                itemSelected="GENERAL";
            }
        });

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Event Organizer");
        categories.add("Hospital");
        categories.add("Plumber");
        categories.add("Restaurant");
        categories.add("Resort");
        categories.add("School");
        categories.add("Technician");
        categories.add("Tutor");
        categories.add("Others");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addUserLocation();
            }
        });
    }
    void addUserLocation(){
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference=databaseReference.child("users").child("User_Location").child(uid);

        Log.e("ee","E");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserLocation userLocation=dataSnapshot.getValue(UserLocation.class);
                userLocation.setPublic_visibilty(getpublic);
                userLocation.setCategory(itemSelected);
                userLocation.setDescription(description.getText().toString());
                Log.e("ee",userLocation.getCategory()+" "+userLocation.getPublic_visibilty()+" "+uid);
                databaseReference.setValue(userLocation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
