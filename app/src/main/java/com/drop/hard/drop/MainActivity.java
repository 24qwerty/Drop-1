package com.drop.hard.drop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    Spinner spinner;
    DatabaseReference databaseReference;
    String itemSelected,uid;
    private GoogleApiClient client;
    double la,lo;
    String senderEmail;
    UserLocation c_user;
    GoogleMap m_map;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        Bundle b = intent.getExtras();


        sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        spinner = (Spinner) findViewById(R.id.spinnerff);
        uid = sp.getString(Constants.CURRENT_UID,"");//(String) b.get("uid");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference = databaseReference.child("users").child(uid);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                itemSelected = adapterView.getItemAtPosition(i).toString();
                spnSelected();
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
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment2);
        mapFragment.getMapAsync(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
    void spnSelected(){ m_map.clear();
        final ArrayList<String> arrayList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference=databaseReference.child("users").child("User_Location");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserLocation userLocation=postSnapshot.getValue(UserLocation.class);
                    if(userLocation.getPublic_visibilty() && userLocation.getCategory().equals(itemSelected)){
                        addMarker(userLocation);
                    }
                    if(userLocation.getUid().equals(uid)){
                        addMarker(userLocation,"Me");
                        senderEmail=userLocation.getEmail();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });}
    public void find(View view){
        m_map.clear();
        final ArrayList<String> arrayList=new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference=databaseReference.child("users").child("User_Location");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    UserLocation userLocation=postSnapshot.getValue(UserLocation.class);
                    if(userLocation.getPublic_visibilty() && userLocation.getCategory().equals(itemSelected)){
                        addMarker(userLocation);
                    }
                    if(userLocation.getUid().equals(uid)){
                        addMarker(userLocation,"Me");
                        senderEmail=userLocation.getEmail();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    void addMarker(UserLocation userLocation) {
        LatLng latLng=new LatLng(userLocation.getLatitude(),userLocation.getLongitude());
        MarkerOptions buf = new MarkerOptions().position(latLng).title(userLocation.getName().toUpperCase()).snippet(userLocation.getEmail());//icon(BitmapDescriptorFactory.fromResource(resource)).
        m_map.addMarker(buf);

    }
    void addMarker(UserLocation userLocation,String s) {
        LatLng latLng=new LatLng(userLocation.getLatitude(),userLocation.getLongitude());
        MarkerOptions buf = new MarkerOptions().position(latLng).title(s).snippet(userLocation.getEmail()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));//icon(BitmapDescriptorFactory.fromResource(resource)).
        m_map.addMarker(buf);
        Circle circle5 = m_map.addCircle(new CircleOptions()
                .center(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()))
                .radius(10000)
                .strokeColor(Color.WHITE)
                .fillColor(0x7F1F8E9));
        Circle circle4 = m_map.addCircle(new CircleOptions()
                .center(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()))
                .radius(5000)
                .strokeColor(Color.WHITE)
                .fillColor(0x70DCEDC8));
        Circle circle3 = m_map.addCircle(new CircleOptions()
                .center(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()))
                .radius(3000)
                .strokeColor(Color.WHITE)
                .fillColor(0x70C5E1A5));
        Circle circle2 = m_map.addCircle(new CircleOptions()
                .center(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()))
                .radius(2000)
                .strokeColor(Color.WHITE)
                .fillColor(0x70AED581));
        Circle circle = m_map.addCircle(new CircleOptions()
                .center(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()))
                .radius(1000)
                .strokeColor(Color.WHITE)
                .fillColor(0x709CCC65));


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_map = googleMap;
       // m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng vad = new LatLng(22.3072, 73.1812);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        m_map.setMyLocationEnabled(true);

        Location location=m_map.getMyLocation();
//        LatLng myLatLng=new LatLng(location.getLatitude(),location.getLongitude());
        final CameraPosition cp = CameraPosition.builder().target(vad).zoom(10).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));
        m_map.setInfoWindowAdapter(new CustomInfoWindowAdapter());
        m_map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent detailIntent = new Intent(MainActivity.this, TagDetails.class).putExtra("email", marker.getSnippet());
                detailIntent.putExtra("senderEmail",senderEmail);
                startActivity(detailIntent);
            }
        });
    }
    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        View wContent;

        CustomInfoWindowAdapter() {
            wContent = getLayoutInflater().inflate(R.layout.info_window_layout, null);

        }

        @Override
        public View getInfoWindow(Marker marker) {
            render(marker, wContent, R.drawable.cast_abc_scrubber_control_to_pressed_mtrl_000);
            return wContent;
        }

        @Override
        public View getInfoContents(Marker marker) {
            render(marker, wContent, R.drawable.cast_abc_scrubber_control_off_mtrl_alpha);
            return wContent;
        }

        private void render(final Marker marker, View view, int resource) {

            ((TextView) view.findViewById(R.id.textView3)).setText(marker.getTitle());

        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        /* Inflate the menu; this adds items to the action bar if it is present. */
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /**
         * Open SettingsActivity with sort options when Sort icon was clicked
         */
        if (id == R.id.action_logout) {
            Firebase userLocation = new Firebase("https://drop-8f1b1.firebaseio.com/");
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();
            userLocation.unauth();
            Intent to_log_in = new Intent(MainActivity.this, Login.class);
            startActivity(to_log_in);
            finish();
            return true;
        }
        else if(id==R.id.action_enable_presence){
            Intent i = new Intent(MainActivity.this, EnablePublicPresence.class);
            i.putExtra("uid", uid);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
