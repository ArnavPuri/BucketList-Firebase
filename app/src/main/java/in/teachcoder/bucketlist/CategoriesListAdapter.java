package in.teachcoder.bucketlist;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import in.teachcoder.bucketlist.models.BucketCategory;

/**
 * Created by Arnav on 30-Apr-16.
 */
public class CategoriesListAdapter extends FirebaseListAdapter<BucketCategory> {
    public CategoriesListAdapter(Activity activity, Class<BucketCategory> modelClass, Firebase ref) {
        super(activity, modelClass, android.R.layout.simple_list_item_1, ref);
    }
//
//    public CategoriesListAdapter(Activity activity, Class<BucketCategory> modelClass, Query ref) {
//        super(activity, modelClass, android.R.layout.simple_list_item_1, ref);
//    }

    @Override
    protected void populateView(View v, BucketCategory model) {
        TextView title = (TextView) v.findViewById(android.R.id.text1);
        title.setText(model.getTitle());
    }

}
