package in.teachcoder.bucketlist.Activities;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import in.teachcoder.bucketlist.Constants;
import in.teachcoder.bucketlist.R;

public class CreateAccountActivity extends AppCompatActivity {
    TextInputEditText name, email, pass;
    Button createAccountBtn;
    TextView signIn;
    String userEmail;
    String userPass;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initializeViews();
        Firebase.setAndroidContext(this);


        final Firebase ref = new Firebase(Constants.FIREBASE_BASE_URL);
        createAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = email.getText().toString();
                userPass = pass.getText().toString();
                ref.createUser(userEmail, userPass, new Firebase.ResultHandler() {
                    @Override
                    public void onSuccess() {
                        Log.d("CreateAccount", "user account created " + name);
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN)
                            email.setError("Email already exists");
                        Log.e("CreateAccount", firebaseError.toString());
                    }
                });
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
