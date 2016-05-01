package in.teachcoder.bucketlist;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import in.teachcoder.bucketlist.models.BucketCategory;

public class DetailActivity extends AppCompatActivity {
    ListView itemsList;
    FloatingActionButton addBucketItem;
    Firebase activeCategoryListRef, itemsRef;
    String owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String listId = getIntent().getStringExtra(Constants.KEY_ID);

        activeCategoryListRef = new Firebase(Constants.FIREBASE_CATEGORIES_URL).child(listId);

        activeCategoryListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                BucketCategory category = dataSnapshot.getValue(BucketCategory.class);
                if (category != null)
                    setTitle(category.getTitle());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void initializeViews() {

    }
}
