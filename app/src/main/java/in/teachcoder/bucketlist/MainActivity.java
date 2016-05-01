package in.teachcoder.bucketlist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

public class MainActivity extends AppCompatActivity {
    ListView categoriesList;
    FloatingActionButton addCategory;
    Firebase firebaseRef, categoriesRef;
CategoriesListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCategory = (FloatingActionButton) findViewById(R.id.addCategoryBtn);
        categoriesList = (ListView) findViewById(R.id.bucketCategoriesList);
        Firebase.setAndroidContext(this);
        firebaseRef = new Firebase(Constants.FIREBASE_BASE_URL);
        categoriesRef = new Firebase(Constants.FIREBASE_CATEGORIES_URL);
        adapter = new CategoriesListAdapter(this, String.class,categoriesRef );
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        categoriesList.setAdapter(adapter);
    }

    public void showDialog(){
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
                Firebase newRef = categoriesRef.push();
                String itemId = newRef.getKey();
                newRef.setValue(category);
            }
        });

        AlertDialog categoryDialog = dialogBuilder.create();
        categoryDialog.show();

    }
}
