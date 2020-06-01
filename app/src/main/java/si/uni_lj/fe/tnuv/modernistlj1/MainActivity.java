package si.uni_lj.fe.tnuv.modernistlj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterCarousel.ItemClickListener {
    AdapterCarousel adapter;
    List<ModelObject> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* CREATE BUILDINGS LIST - USTVARI SEZNAM ZGRADB */
        models = new ArrayList<>();
        try {
            JSONArray jArray = new JSONArray(readJSONFromAsset());
            for (int i = 0; i < jArray.length(); ++i) {
                String name = jArray.getJSONObject(i).getString("Name"); // name
                String image_file_name = jArray.getJSONObject(i).getString("Image_name_main"); // image_file_name
                String description_main = jArray.getJSONObject(i).getString("Description_main");
                int resource_id_image = getResources().getIdentifier(image_file_name, "drawable", getPackageName()); // get resource id
                String sketched_image_file_name = jArray.getJSONObject(i).getString("Image_name_sketch"); // image_file_name
                int resource_id_image_sketched = getResources().getIdentifier(sketched_image_file_name, "drawable", getPackageName()); // get resource id
                models.add(new ModelObject(i,name, description_main, resource_id_image, resource_id_image_sketched)); // pass id and name to ModelCarousel class
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to access information.", Toast.LENGTH_SHORT).show();
        }

        /* MAIN WINDOW CARD - GLAVNO OKNO */
        TextView title = findViewById((R.id.title_id));
        title.setText(models.get(0).getTitle());
        TextView description_main = findViewById(R.id.description_main_id);
        description_main.setText(models.get(0).getDescription_main());
        ImageView sketched_image = findViewById(R.id.main_window_photo_id);
        sketched_image.setImageResource(models.get(0).getImage_sketched());
        sketched_image.setVisibility((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) ? View.GONE : View.VISIBLE);

        /* BUTTON THAT OPENS DETAIL - GUMB NA DETAIL POGLED */
        Button open_rep_sq = (Button) findViewById(R.id.main_window_button_id);
        open_rep_sq.setOnClickListener(new DetailButtonOnClickListener(models.get(0).getTitle(), this));

        /* SAVING STATE OF CHECKBOX CODE - MEHANIZEM ZA SHRANJEVANJE CHECKBOXA */
        final SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        String key = getString(R.string.CHECKBOX_KEY_0);
        boolean value = getResources().getBoolean(R.bool.BOOL_0);
        boolean is_it_checked = sharedPrefs.getBoolean(key, value);
        CheckBox visited_checkbox =  (CheckBox) findViewById(R.id.main_window_checkbox);
        visited_checkbox.setChecked(is_it_checked);
        visited_checkbox.setOnClickListener(new CarouselOnClickListener(key, sharedPrefs));

        /* CAROUSEL - GALERIJA KARTIC */
        RecyclerView recyclerView = findViewById(R.id.carousel_id);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        adapter = new AdapterCarousel(this, models);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,  false){
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                // force height of viewHolder here, this will override layout_height from xml
                lp.width = getWidth() / 3;
                return true;
            }
        });

        /** BOTTOM MENU - SPODNJI MENI **/
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
                        return true;
                    case R.id.map_button_menu:
                        // Switch to map activity
                        Intent intent_map = new Intent(MainActivity.this, MapActivity.class);
                        intent_map.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent_map);
                        return true;
                    case R.id.general_button_menu:
                        // Switch to general activity
                        Intent intent_general = new Intent(MainActivity.this, GeneralActivity.class);
                        intent_general.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent_general);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.home_button_menu);
        super.onResume();
    }
    /*HELPERS*/
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
    /* CLICKED ITEM IN CAROUSEL - BINDS DATA FROM MODEL TO VIEW */
    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on item position " + position, Toast.LENGTH_SHORT).show();

        TextView title = findViewById((R.id.title_id));
        title.setText(models.get(position).getTitle());

        TextView description_main = findViewById(R.id.description_main_id);
        description_main.setText(models.get(position).getDescription_main());

        final SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);

        String pre_key = "CHECKBOX_KEY_" + position;
        int resourceIdKey = getResources().getIdentifier(pre_key,"string", getPackageName());
        String key = getString(resourceIdKey);

        String pre_bool = "BOOL_" + position;
        int resourceIdBool = getResources().getIdentifier(pre_bool,"bool", getPackageName());
        boolean value = getResources().getBoolean(resourceIdBool);

        boolean is_it_checked = sharedPrefs.getBoolean(key, value);
        CheckBox visited_checkbox =  (CheckBox) findViewById(R.id.main_window_checkbox);
        visited_checkbox.setChecked(is_it_checked);
        visited_checkbox.setOnClickListener(new CarouselOnClickListener(key, sharedPrefs));

        ImageView sketched_image = findViewById(R.id.main_window_photo_id);
        sketched_image.setImageResource(models.get(position).getImage_sketched());

        /* NEW BUTTON LINK */
        Button open_rep_sq = (Button) findViewById(R.id.main_window_button_id);
        open_rep_sq.setOnClickListener(new DetailButtonOnClickListener(models.get(position).getTitle(), this));
    }
}
