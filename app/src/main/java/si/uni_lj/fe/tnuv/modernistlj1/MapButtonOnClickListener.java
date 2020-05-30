package si.uni_lj.fe.tnuv.modernistlj1;

import android.content.Context;
import android.content.Intent;
import android.view.View;

class MapButtonOnClickListener implements View.OnClickListener {
    private Context context;
    private double lat;
    private double lon;

    public MapButtonOnClickListener(Context context, double lat, double lon) {
        this.context = context;
        this.lat = lat;
        this.lon = lon;
    }

    @Override
    public void onClick(View v) {
        Intent intent_map = new Intent(context, MapActivity.class);
        //intent_map.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent_map.putExtra("lat", lat);
        intent_map.putExtra("lon", lon);
        context.startActivity(intent_map);
    }
}
