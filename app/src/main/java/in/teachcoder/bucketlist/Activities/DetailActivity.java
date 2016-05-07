package in.teachcoder.bucketlist.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import in.teachcoder.bucketlist.Constants;
import in.teachcoder.bucketlist.R;
import in.teachcoder.bucketlist.models.BucketCategory;
import in.teachcoder.bucketlist.models.BucketCategoryItem;

public class DetailActivity extends AppCompatActivity {
    ListView itemsList;
    FloatingActionButton addBucketItem;
    Firebase activeCategoryListRef, itemsRef;
    String owner;
    ValueEventListener eventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeViews();
        String listId = getIntent().getStringExtra(Constants.KEY_ID);

        activeCategoryListRef = new Firebase(Constants.FIREBASE_CATEGORIES_URL).child(listId);
        itemsRef = new Firebase(Constants.FIREBASE_ITEMS_URL).child(listId);
        eventListener = activeCategoryListRef.addValueEventListener(new ValueEventListener() {
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
        addBucketItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_category_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText itemName = (EditText) dialogView.findViewById(R.id.category_name_input);

        dialogBuilder.setTitle("Add a item");
        dialogBuilder.setMessage("Enter your Item name");
        dialogBuilder.setPositiveButton("Add Item", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send to firebase
                String item = itemName.getText().toString();
                owner = "Anon";
                Firebase newRef = itemsRef.push();
                HashMap<String, Object> timeStampCreatedAt = new HashMap<String, Object>();
                timeStampCreatedAt.put(Constants.FIREBASE_TIMESTAMP_PROPERTY, ServerValue.TIMESTAMP);
                BucketCategoryItem newCategory = new BucketCategoryItem(owner, item, timeStampCreatedAt);

                String itemId = newRef.getKey();
                newRef.setValue(newCategory);
            }
        });

        AlertDialog categoryDialog = dialogBuilder.create();
        categoryDialog.show();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activeCategoryListRef.removeEventListener(eventListener);
    }


    public void initializeViews() {
        addBucketItem = (FloatingActionButton) findViewById(R.id.addbucketItemBtn);
        itemsList = (ListView) findViewById(R.id.bucketItemsList);
    }
}
