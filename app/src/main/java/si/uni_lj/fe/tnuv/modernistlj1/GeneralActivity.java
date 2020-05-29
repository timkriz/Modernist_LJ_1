package si.uni_lj.fe.tnuv.modernistlj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class GeneralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

        /* CAROUSEL - GALERIJA ARHITEKTOV (recyclerview) */
        RecyclerView recyclerView = findViewById(R.id.carousel_id);
        LinearLayoutManager horizontalLayoutManager
                = new LinearLayoutManager(GeneralActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(horizontalLayoutManager);
        int num_of_arhitects = 5;  // needs fixing
        ArrayList models = new ArrayList<>();
        for (int i = 0; i < num_of_arhitects; ++i) {
            int resId = getResources().getIdentifier("arhitect_" + i, "string", getPackageName());
            getString(resId);
            int resource_id_image = getResources().getIdentifier("carousel_arhitects_" + i, "drawable", getPackageName());
            models.add(new ModelObject(0, getString(resId), "", resource_id_image));
        }
        AdapterCarousel adapter = new AdapterCarousel(this, models);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,  false){
            @Override
            public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
                // force height of viewHolder here, this will override layout_height from xml
                lp.width = getWidth() / 3;
                return true;
            }
        });

        /* BOTTOM MENU - SPODNJI MENI */
        //Initialize and assign bottom navigation variable
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //Set home selected
        navigation.setSelectedItemId(R.id.general_button_menu);
        //Perform ItemSelectedListener
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.home_button_menu:
                        Intent intent_main = new Intent(GeneralActivity.this, MainActivity.class);
                        intent_main.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent_main);
                        return true;
                    case R.id.map_button_menu:
                        Intent intent_map = new Intent(GeneralActivity.this, MapActivity.class);
                        intent_map.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent_map);
                        return true;
                    case R.id.general_button_menu:
                        return true;
                }
                return false;
            }
        });
    }
    @Override
    protected void onResume() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.general_button_menu);
        super.onResume();
    }
}
