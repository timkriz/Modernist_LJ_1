package si.uni_lj.fe.tnuv.modernistlj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String TAG = "Error";
    private double[] lat = new double[20];
    private double[] lon = new double[20];
    private double focus_on_this_lat = 0;
    private double focus_on_this_lon = 0;
    private String[] names_of_buildings = new String[20];
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        if (getIntent().hasExtra("lon") && getIntent().hasExtra("lat")) {
            focus_on_this_lat = getIntent().getExtras().getDouble("lat");
            focus_on_this_lon = getIntent().getExtras().getDouble("lon");
        }

        /* GET LAT AND LON values from JSON */
        try {
            JSONArray jArray = new JSONArray(readJSONFromAsset());
            for (int i = 0; i < jArray.length(); ++i) {
                double read_lat = jArray.getJSONObject(i).getDouble(getString(R.string.JSON_property_lat)); // lat
                double read_lon = jArray.getJSONObject(i).getDouble(getString(R.string.JSON_property_lon)); // lat
                String read_name = jArray.getJSONObject(i).getString(getString(R.string.JSON_property_name)); // lat
                lat[i] = read_lat;
                lon[i] = read_lon;
                names_of_buildings[i] = read_name;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
    /* Called in onCreate when initializing map*/
    public void initMap() {
        MapFragment map = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragmentID);
        map.getMapAsync(this);
    }
    /* Called when map is ready to be used */
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
                        Toast.makeText(this, R.string.No_access_maps, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Can't find style. Error: ", e);
                    Toast.makeText(this, R.string.No_access_maps, Toast.LENGTH_SHORT).show();
                }

                // Constrain the camera target to the Ljubljana bounds.
                LatLngBounds LJUBLJANA = new LatLngBounds(
                        new LatLng(46.014673, 14.393995), new LatLng(46.087733, 14.588543));
                mGoogleMap.setLatLngBoundsForCameraTarget(LJUBLJANA);
                mGoogleMap.setMinZoomPreference(12.0f);
                mGoogleMap.setMaxZoomPreference(20.0f);

                //Markers
                for (int i = 0; i < lat.length; ++i) {
                    LatLng position = new LatLng(lat[i], lon[i]);
                    mGoogleMap.addMarker(new MarkerOptions().position(position).title(names_of_buildings[i])
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                }

                /* OPEN DETAIL ACTIVITY FROM MAP TITLE */
                mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        String name = marker.getTitle();
                        Intent intent_detail = new Intent(getApplicationContext(), DetailActivity.class);
                        intent_detail.putExtra(getString(R.string.key_name_of_building), name);
                        startActivity(intent_detail);
                    }
                });

                // Focus on position of building from detail view

                if (focus_on_this_lat !=0 && focus_on_this_lon !=0) {
                    mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(focus_on_this_lat,focus_on_this_lon), 17.0f));
                    focus_on_this_lat = 0;
                    focus_on_this_lat = 0;
                }
                else {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ERROR", "GOOGLE MAPS NOT LOADED");
            Toast.makeText(this, R.string.No_access_maps, Toast.LENGTH_SHORT).show();
        }
    }
    /* READ JSON FILE IN ASSETS FOLDER */
    public String readJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open(getString(R.string.JSON_file_name));
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}