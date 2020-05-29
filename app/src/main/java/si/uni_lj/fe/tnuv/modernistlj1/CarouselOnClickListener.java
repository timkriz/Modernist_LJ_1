package si.uni_lj.fe.tnuv.modernistlj1;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.CompoundButton;

public class CarouselOnClickListener implements View.OnClickListener {
    private String keyResource;
    private SharedPreferences sharedPrefs;


    CarouselOnClickListener(String keyResource, SharedPreferences sharedPrefs) {
        this.keyResource = keyResource;
        this.sharedPrefs = sharedPrefs;
    }

    @Override
    public void onClick(View v)
    {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        if(((CompoundButton) v).isChecked()){
            editor.putBoolean(keyResource, true);
            editor.apply();

        } else {
            editor.putBoolean(keyResource, false);
            editor.apply();
        }
    }
}