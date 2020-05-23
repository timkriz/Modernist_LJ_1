package si.uni_lj.fe.tnuv.modernistlj1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        startMapActivity();
                        return true;
                    case R.id.general_button_menu:
                        startGeneralActivity();
                        return true;
                }
                return false;
            }
        });

        // to kasneje izbrisi (zaenkrat dodan le gumb za lazji dostop do DetailActivity)
        Button open_detail = (Button) findViewById(R.id.open_detail);
        open_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDetail();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /** Called when the user clicks for MAP tab */
    public void startMapActivity() {
        Intent intentMap = new Intent(MainActivity.this, MapActivity.class);
        //overridePendingTransition(0,0);
        startActivity(intentMap);
    }

    /** Called when the user clicks for GENERAL tab */
    public void startGeneralActivity() {
        Intent intentGeneral = new Intent(MainActivity.this, GeneralActivity.class);
        //overridePendingTransition(0,0);
        startActivity(intentGeneral);
    }

    // to kasneje izbrisi
    public void openDetail(){
        Intent intentDetail = new Intent(this, DetailActivity.class);
        startActivity(intentDetail);
    }
}
