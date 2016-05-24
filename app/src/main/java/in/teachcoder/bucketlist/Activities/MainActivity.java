package in.teachcoder.bucketlist.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;

import in.teachcoder.bucketlist.CategoriesListAdapter;
import in.teachcoder.bucketlist.Constants;
import in.teachcoder.bucketlist.R;
import in.teachcoder.bucketlist.models.BucketCategory;
import in.teachcoder.bucketlist.models.UserModel;

public class MainActivity extends AppCompatActivity {
    ListView categoriesList;
    FloatingActionButton addCategory;
    Firebase firebaseRef, categoriesRef, usersRef;
    CategoriesListAdapter adapter;
    String owner;
    SharedPreferences sp;
    String mProvider, mEncodedEmail;
    private ValueEventListener mUserRefListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCategory = (FloatingActionButton) findViewById(R.id.addCategoryBtn);
        categoriesList = (ListView) findViewById(R.id.bucketCategoriesList);
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        mEncodedEmail = sp.getString(Constants.KEY_ENCODED_EMAIL, null);
        mProvider = sp.getString(Constants.KEY_PROVIDER, null);

        firebaseRef = new Firebase(Constants.FIREBASE_BASE_URL);
        categoriesRef = new Firebase(Constants.FIREBASE_CATEGORIES_URL);
        usersRef = new Firebase(Constants.FIREBASE_USER_URL).child(mEncodedEmail);
        Log.d("MainActivity pre", usersRef.toString());


        categoriesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String key = child.getKey();
                    Query whichUser = categoriesRef.child(key).orderByChild("owner").equalTo("Anon");
                    whichUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
//                    Firebase ref = categoriesRef.child(key).child("owner");
//                    ref.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            Log.d("MainActivity userQuery", dataSnapshot.getValue().toString());
//                        }
//
//                        @Override
//                        public void onCancelled(FirebaseError firebaseError) {
//
//                        }
//                    });
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        adapter = new CategoriesListAdapter(this, BucketCategory.class, categoriesRef);

        addCategory.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               showDialog();
                                           }
                                       }

        );
        categoriesList.setAdapter(adapter);

        mUserRefListener = usersRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModel user = dataSnapshot.getValue(UserModel.class);

                        if (user != null) {
                            String firstName = user.getName().split("\\s+")[0];
                            String title = firstName + "'s Lists";
                            setTitle(title);

                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                }

        );

        listListener();
    }

    public void showDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_category_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText categoryName = (EditText) dialogView.findViewById(R.id.category_name_input);

        dialogBuilder.setTitle("Add a Category");
        dialogBuilder.setMessage("Enter your category name");
        dialogBuilder.setPositiveButton("Add Category", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // send to firebase
                String category = categoryName.getText().toString();
                owner = mEncodedEmail;
                Firebase newRef = categoriesRef.push();
                HashMap<String, Object> timeStampCreatedAt = new HashMap<String, Object>();
                timeStampCreatedAt.put(Constants.FIREBASE_TIMESTAMP_PROPERTY, ServerValue.TIMESTAMP);
                BucketCategory newCategory = new BucketCategory(owner, category, timeStampCreatedAt);

                String itemId = newRef.getKey();
                newRef.setValue(newCategory);
            }
        });

        AlertDialog categoryDialog = dialogBuilder.create();
        categoryDialog.show();

    }

    public void listListener() {
        categoriesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = adapter.getRef(position).getKey();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(Constants.KEY_ID, key);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        usersRef.removeEventListener(mUserRefListener);
    }
}
