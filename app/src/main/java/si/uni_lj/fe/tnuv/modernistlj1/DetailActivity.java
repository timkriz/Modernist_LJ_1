package si.uni_lj.fe.tnuv.modernistlj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // SET CONTENT
        final TextView detailTitle = (TextView) findViewById(R.id.textViewDetail);      // 1. set title
        final TextView architectName = (TextView) findViewById(R.id.architect_name);    // 2. set architect's name
        final TextView text1 = (TextView) findViewById(R.id.text_building_1);           // 3. set text 1
        final ImageView buildingImg = (ImageView) findViewById(R.id.building_image);    // 4. set picture
        final TextView text2 = (TextView) findViewById(R.id.text_building_2);           // 5. set text 2

        switch(MainActivity.vrsta_gumba){
            case 1: detailTitle.setText(R.string.republic_square);
                    architectName.setText(R.string.edvard_ravnikar_small);
                    text1.setText(R.string.text_republic_square_1);
                    buildingImg.setImageResource(R.drawable.republic_square_image);
                    text2.setText(R.string.text_republic_square_2);
                    break;
            case 2: detailTitle.setText(R.string.cankar_centre);
                    architectName.setText(R.string.edvard_ravnikar_small);
                    text1.setText(R.string.text_cankar_centre_1);
                    buildingImg.setImageResource(R.drawable.cankar_centre_image);
                    text2.setText(R.string.text_cankar_centre_2);
                    break;
            case 3: detailTitle.setText(R.string.ferant_garden);
                    architectName.setText(R.string.edvard_ravnikar_small);
                    text1.setText(R.string.text_ferant_garden_1);
                    buildingImg.setImageResource(R.drawable.ferant_garden_image);
                    text2.setText(R.string.text_ferant_garden_2);
                    break;
        }

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
        });
    }

    /** Called when the user clicks for HOME tab */
    public void startHomeActivity() {
        Intent intentHome = new Intent(DetailActivity.this, MainActivity.class);
        overridePendingTransition(0,0);
        startActivity(intentHome);
    }
    /** Called when the user clicks for MAP tab */
    public void startMapActivity() {
        Intent intentMap = new Intent(DetailActivity.this, MapActivity.class);
        //overridePendingTransition(0,0);
        startActivity(intentMap);
    }

    /** Called when the user clicks for GENERAL tab */
    public void startGeneralActivity() {
        Intent intentGeneral = new Intent(DetailActivity.this, GeneralActivity.class);
        //overridePendingTransition(0,0);
        startActivity(intentGeneral);
    }
}
