package si.uni_lj.fe.tnuv.modernistlj1;

import android.content.Context;
import android.content.Intent;
import android.view.View;

public class DetailButtonOnClickListener implements View.OnClickListener{
    private String keyResource;
    private Context context;

    DetailButtonOnClickListener(String keyResource, Context context) {
        this.keyResource = keyResource;
        this.context = context;
    }
    @Override
    public void onClick(View v) {
        Intent intent_detail = new Intent(context, DetailActivity.class);
        //intent_map.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        intent_detail.putExtra("name_of_building", keyResource);
        context.startActivity(intent_detail);
    }
}
