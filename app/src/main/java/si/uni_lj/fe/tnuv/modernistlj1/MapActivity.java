package si.uni_lj.fe.tnuv.modernistlj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "napaka";
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Initialize and assign bottom navigation variable
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //Set home selected
        navigation.setSelectedItemId(R.id.map_button_menu);
        //Perform ItemSelectedListener
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home_button_menu:
                        Intent intent_main = new Intent(MapActivity.this, MainActivity.class);
                        intent_main.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent_main);
                        return true;
                    case R.id.map_button_menu:
                        return true;
                    case R.id.general_button_menu:
                        Intent intent_general = new Intent(MapActivity.this, GeneralActivity.class);
                        intent_general.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent_general);
                        return true;
                }
                return false;
            }
        });
        if(mGoogleMap == null) {
            initMap();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.map_button_menu);
    }
    @Override
    protected void onPause() {
        super.onPause();
        MapStateManager mgr = new MapStateManager(this);
        mgr.saveMapState(mGoogleMap);
        //Log.e("ERROR", "Map State has been saved");
    }
    /** Called in onCreate when initializing map**/
    public void initMap() {
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragmentID);
        map.getMapAsync(this);
    }
    /** Called when map is ready to be used **/
    public void onMapReady(GoogleMap googleMap) {
        try {
            if (googleMap != null) {
                mGoogleMap = googleMap;
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);    // Style of a map
                // Style of a map from JSON in res/raw
                try {
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.style_json));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }

                // Constrain the camera target to the Ljubljana bounds.
                LatLngBounds LJUBLJANA = new LatLngBounds(
                        new LatLng(46.014673, 14.393995), new LatLng(46.087733, 14.588543));
                mGoogleMap.setLatLngBoundsForCameraTarget(LJUBLJANA);
                mGoogleMap.setMinZoomPreference(12.0f);
                mGoogleMap.setMaxZoomPreference(20.0f);

                //Markers
                /*mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(10, 10))
                        .title("Hello world"));*/

                //Remebering position when resuming
                MapStateManager mgr = new MapStateManager(this);
                CameraPosition position = mgr.getSavedCameraPosition();
                if (position != null) {
                    CameraUpdate update = CameraUpdateFactory.newCameraPosition(position);
                    //Log.e("ERROR", "entering Resume State");
                    mGoogleMap.moveCamera(update);
                    mGoogleMap.setMapType(mgr.getSavedMapType());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "GOOGLE MAPS NOT LOADED");
        }
    }
}