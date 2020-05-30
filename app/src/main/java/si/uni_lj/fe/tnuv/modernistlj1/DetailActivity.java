package si.uni_lj.fe.tnuv.modernistlj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        /* BIND DATA TO LAYOUT - DOLOCI PODATKE V UI */
        String name_of_building = getIntent().getExtras().getString("name_of_building");
        String read_detail_text = null;
        String arhitects_name = null;
        String year_built = null;
        JSONObject objekt = findBuildinginJSON(name_of_building);
        JSONArray image_names_json;
        int[] images = {0};
        double lat = 0;
        double lon = 0;

        /* GET DATA FROM JSON - dobi podatke iz jsona */
        try {
            read_detail_text = objekt.getString("Description_detail");
            arhitects_name = objekt.getString("Architect");
            year_built = objekt.getString("Year");
            image_names_json = objekt.getJSONArray("Image_names");
            images = new int[image_names_json.length()];
            for(int i = 0; i < image_names_json.length(); i++) {
                String name_of_image = image_names_json.getString(i);
                int resource_id_image = getResources().getIdentifier(name_of_image, "drawable", getPackageName()); // get resource id
                images[i] = resource_id_image;
            }
            lat = objekt.getDouble("Lat");
            lon = objekt.getDouble("Lon");
        } catch (JSONException e) {
            Toast.makeText(this, "Unable to find info about " + name_of_building, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        final TextView detailTitle = (TextView) findViewById(R.id.title_id);
        detailTitle.setText(name_of_building);
        TextView text_detail_building = (TextView) findViewById(R.id.text_detail_building);
        text_detail_building.setText(read_detail_text);

        TextView architect_name = (TextView) findViewById(R.id.architect_name);
        architect_name.setText(arhitects_name);
        TextView year_built_UI = (TextView) findViewById(R.id.year_value);
        year_built_UI.setText(year_built);

        /* IMAGE CAROUSEL - SLIKE */
        recyclerView = findViewById(R.id.carousel_id);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        mAdapter = new MyAdapter(this, images);
        recyclerView.setAdapter(mAdapter);
        //adapter = new AdapterCarouselDetail(this, image_names);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,  false){
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                // force height of viewHolder here, this will override layout_height from xml
                lp.width = getWidth() / 2;
                return true;
            }
        });

        /* BUTTON THAT LINKS TO MAP - GUMB KI ODPRE ZEMLJEVID */

        Button open_map_button = (Button) findViewById(R.id.button_open_in_map_id);
        open_map_button.setOnClickListener(new MapButtonOnClickListener(this, lat, lon));

        /* zaenkrat brez spodnje navigacije ker je problem pri vračanju in ugasanju :S */
        /*
        //Initialize and assign bottom navigation variable
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //Set home selected
        navigation.setSelectedItemId(R.id.home_button_menu);
        //Perform ItemSelectedListener
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home_button_menu:
                        startHomeActivity();
                        return true;
                    case R.id.map_button_menu:
                        startMapActivity();
                        return true;
                    case R.id.general_button_menu:
                        startGeneralActivity();
                        return true;
                }
                return false;
            }
        });*/
    }

    /* Called when the user clicks for HOME tab */
    public void startHomeActivity() {
        Intent intentHome = new Intent(DetailActivity.this, MainActivity.class);
        overridePendingTransition(0,0);
        startActivity(intentHome);
    }
    /* Called when the user clicks for MAP tab */
    public void startMapActivity() {
        Intent intentMap = new Intent(DetailActivity.this, MapActivity.class);
        //overridePendingTransition(0,0);
        startActivity(intentMap);
    }

    /* Called when the user clicks for GENERAL tab */
    public void startGeneralActivity() {
        Intent intentGeneral = new Intent(DetailActivity.this, GeneralActivity.class);
        //overridePendingTransition(0,0);
        startActivity(intentGeneral);
    }
    public JSONObject findBuildinginJSON(String Name_to_Find) {
        try{
            JSONArray array = new JSONArray(readJSONFromAsset());
            String name_read;
            JSONObject searchObject = array.getJSONObject(0);
            for (int i = 0; i < array.length(); i++) {
                JSONObject currObject = array.getJSONObject(i);
                name_read = currObject.getString("Name");

                Log.e("BEREM", name_read);
                Log.e("BEREM z", Name_to_Find);
                if (name_read.equals(Name_to_Find)) {
                    Log.e("nasel", Name_to_Find);
                    searchObject = currObject;
                }
            }
            return searchObject;
        } catch (JSONException e) {
            Toast.makeText(this, "Unable to find info about " + Name_to_Find, Toast.LENGTH_SHORT).show();
            Log.e("Ne Najde", "pizda");
            e.printStackTrace();
            return null;
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
