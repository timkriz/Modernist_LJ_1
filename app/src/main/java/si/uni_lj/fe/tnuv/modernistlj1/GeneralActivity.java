package si.uni_lj.fe.tnuv.modernistlj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class GeneralActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_general);

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
                        startHomeActivity();
                        return true;
                    case R.id.map_button_menu:
                        startMapActivity();
                        return true;
                    case R.id.general_button_menu:
                        return true;
                }
                return false;
            }
        });
    }
    /** Called when the user clicks for HOME tab */
    public void startHomeActivity() {
        Intent intentHome = new Intent(GeneralActivity.this, MainActivity.class);
        overridePendingTransition(0,0);
        startActivity(intentHome);
    }
    /** Called when the user clicks for MAP tab */
    public void startMapActivity() {
        Intent intentMap = new Intent(GeneralActivity.this, MapActivity.class);
        //overridePendingTransition(0,0);
        startActivity(intentMap);
    }
}
