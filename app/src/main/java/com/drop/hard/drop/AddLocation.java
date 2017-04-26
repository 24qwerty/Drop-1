package com.drop.hard.drop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddLocation extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap m_map;
    DatabaseReference databaseReference;
    String uid;
    private GoogleApiClient client;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        sp= PreferenceManager.getDefaultSharedPreferences(AddLocation.this);
        editor=sp.edit();
        Intent intent=getIntent();
        Bundle b=intent.getExtras();
        uid= (String) b.get("uid");
        final MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        m_map = googleMap;
      //  m_map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        LatLng vad = new LatLng(22.3072, 73.1812);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(AddLocation.this,"Please enable GPS.",Toast.LENGTH_SHORT).show();
            return;
        }
          m_map.setMyLocationEnabled(true);

        final CameraPosition cp = CameraPosition.builder().target(vad).zoom(10).build();
        m_map.moveCamera(CameraUpdateFactory.newCameraPosition(cp));

        m_map.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                addUserLocation2(latLng.latitude,latLng.longitude);
                Toast.makeText(AddLocation.this,"Location Added.\nPress Done",Toast.LENGTH_LONG).show();
            }
        });


    }
    public void done(View view){
        editor.putString(Constants.CURRENT_UID,uid);
        editor.commit();
        Intent intent=new Intent(AddLocation.this,MainActivity.class);
        intent.putExtra("uid",uid);
        finish();
        startActivity(intent);
    }

    void addUserLocation2(final double lat, final double log){
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference=databaseReference.child("users").child("User_Location").child(uid);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserLocation userLocation=dataSnapshot.getValue(UserLocation.class);
                userLocation.setLatitude(lat);
                userLocation.setLongitude(log);
                Log.e("eee","eee");
                addMarker(userLocation);
                databaseReference.setValue(userLocation);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    void addMarker(UserLocation userLocation) {
        LatLng latLng=new LatLng(userLocation.getLatitude(),userLocation.getLongitude());
        MarkerOptions buf = new MarkerOptions().position(latLng);//icon(BitmapDescriptorFactory.fromResource(resource)).
        m_map.addMarker(buf);

    }
}
