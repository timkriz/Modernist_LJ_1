package si.uni_lj.fe.tnuv.modernistlj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
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
                        startHomeActivity();
                        return true;
                    case R.id.map_button_menu:
                        return true;
                    case R.id.general_button_menu:
                        startGeneralActivity();
                        return true;
                }
                return false;
            }
        });

        /** initialize map **/
        initMap();
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
                try {
                    // Customise the styling of the base map using a JSON object defined
                    // in a raw resource file.
                    boolean success = googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    this, R.raw.style_json));

                    if (!success) {
                        Log.e(TAG, "Style parsing failed.");
                    }
                } catch (Resources.NotFoundException e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                }
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                // Constrain the camera target to the Ljubljana bounds.
                LatLngBounds LJUBLJANA = new LatLngBounds(
                        new LatLng(46.014673, 14.393995), new LatLng(46.087733, 14.588543));
                mGoogleMap.setLatLngBoundsForCameraTarget(LJUBLJANA);
                mGoogleMap.setMinZoomPreference(12.0f);
                mGoogleMap.setMaxZoomPreference(20.0f);
            }
        } catch (Exception e) {
            e.printStackTrace();
            /**Log.e("ERROR", "GOOGLE MAPS NOT LOADED");**/
        }
    }
    /** Called when the user clicks for HOME tab */
    public void startHomeActivity() {
        Intent intentHome = new Intent(MapActivity.this, MainActivity.class);
        overridePendingTransition(0,0);
        startActivity(intentHome);
    }
    /** Called when the user clicks for MAP tab */
    public void startGeneralActivity() {
        Intent intentGeneral = new Intent(MapActivity.this, GeneralActivity.class);
        //overridePendingTransition(0,0);
        startActivity(intentGeneral);
    }
}
