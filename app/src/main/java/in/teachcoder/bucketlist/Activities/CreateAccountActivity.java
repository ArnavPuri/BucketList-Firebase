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

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import in.teachcoder.bucketlist.Constants;
import in.teachcoder.bucketlist.R;

public class CreateAccountActivity extends AppCompatActivity {
    TextInputEditText name, email, pass;
    Button createAccountBtn;
    TextView signIn;
    String userEmail;
    String userPass;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initializeViews();
        Firebase.setAndroidContext(this);
        final Firebase ref = new Firebase(Constants.FIREBASE_BASE_URL);
//        progress dialog setup
        progressDialog = new ProgressDialog(this, android.R.style.Theme_DeviceDefault_Light_Dialog);
        progressDialog.setMessage("Creating user in Firebase");
        progressDialog.setCancelable(false);

        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                userEmail = email.getText().toString();
                userPass = pass.getText().toString();
                ref.createUser(userEmail, userPass, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Log.d("CreateAccount", "user account created " + name);
                        progressDialog.dismiss();
                        Intent i = new Intent(CreateAccountActivity.this, LoginActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN)
                            email.setError("Email already exists");
                        Log.e("CreateAccount", firebaseError.toString());
                        progressDialog.dismiss();
                    }
                });
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intent);
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
