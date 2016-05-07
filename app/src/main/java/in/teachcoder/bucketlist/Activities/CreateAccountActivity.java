package in.teachcoder.bucketlist.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import in.teachcoder.bucketlist.Constants;
import in.teachcoder.bucketlist.R;
import in.teachcoder.bucketlist.models.UserModel;

public class CreateAccountActivity extends AppCompatActivity {
    TextInputEditText name, email, pass;
    Button createAccountBtn;
    TextView signIn;
    String userEmail;
    String userPass;
    String userName;
    private ProgressDialog authenticationDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initializeViews();
        Firebase.setAndroidContext(this);
        authenticationDialog = new ProgressDialog(this);
        authenticationDialog.setTitle("Loading...");
        authenticationDialog.setMessage("Authenticating with backend");
        authenticationDialog.setCancelable(false);


        final Firebase ref = new Firebase(Constants.FIREBASE_BASE_URL);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                userPass = pass.getText().toString();
                userName = name.getText().toString();
                final String encodedEmail = Constants.updateEmail(userEmail);
                authenticationDialog.show();
                ref.createUser(userEmail, userPass, new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> result) {
                        createUserInFirebase(encodedEmail);
                        authenticationDialog.dismiss();
                        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN)
                            email.setError("Email already exists");
                        Log.e("CreateAccount", firebaseError.toString());
                        authenticationDialog.dismiss();
                    }
                });
            }
        });


    }

    private void createUserInFirebase(String encodedEmail) {
        final Firebase usersRef = new Firebase(Constants.FIREBASE_USER_URL).child(encodedEmail);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    HashMap<String, Object> timeJoined = new HashMap<String, Object>();
                    timeJoined.put(Constants.FIREBASE_TIMESTAMP_PROPERTY, ServerValue.TIMESTAMP);

                    UserModel newUser = new UserModel(userName, userEmail, timeJoined);
                    usersRef.setValue(newUser);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.d("CreateAccount", firebaseError.toString());
            }
        });
    }

    public void initializeViews() {
        name = (TextInputEditText) findViewById(R.id.user_name_input);
        email = (TextInputEditText) findViewById(R.id.user_email_input);
        pass = (TextInputEditText) findViewById(R.id.user_pass_input);
        signIn = (TextView) findViewById(R.id.tv_sign_in);
        createAccountBtn = (Button) findViewById(R.id.sign_up_button);
    }


}
