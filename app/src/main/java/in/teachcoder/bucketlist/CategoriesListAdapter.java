package in.teachcoder.bucketlist;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Query;
import com.firebase.ui.FirebaseListAdapter;

/**
 * Created by Arnav on 30-Apr-16.
 */
public class CategoriesListAdapter extends FirebaseListAdapter<String> {
    public CategoriesListAdapter(Activity activity, Class<String> modelClass, Query ref) {
        super(activity, modelClass, android.R.layout.simple_list_item_1, ref);
    }

    @Override
    protected void populateView(View v, String model) {
        TextView title = (TextView) v.findViewById(android.R.id.text1);
        title.setText(model);

    }
}
