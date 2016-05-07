package in.teachcoder.bucketlist.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ServerValue;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.util.HashMap;

import in.teachcoder.bucketlist.Constants;
import in.teachcoder.bucketlist.R;
import in.teachcoder.bucketlist.models.UserModel;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    TextInputEditText userEmail, password;
    SignInButton googleSignInBtn;
    Button signIn;
    TextView signUp;
    String updatedEmailId;
    private Firebase mFirebase;
    private Firebase.AuthStateListener myAuthStateListener;
    GoogleSignInOptions gso;
    public static GoogleApiClient googleApiClient;
    GoogleSignInAccount googleAccount;
    boolean googleIntentInProgress;
    private static final int RC_SIGN_IN = 1;
    private ProgressDialog authenticationDialog;
    Firebase.AuthStateListener getMyAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeViews();
        setupGoogleSignIn();
        onSignUpPressed();
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase(Constants.FIREBASE_BASE_URL);
        final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//      authentication dialog
        authenticationDialog = new ProgressDialog(this);
        authenticationDialog.setTitle("Loading...");
        authenticationDialog.setMessage("Authenticating with backend");
        authenticationDialog.setCancelable(false);

        getMyAuthStateListener = new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    SharedPreferences.Editor spe = sp.edit();
                    spe.putString(Constants.KEY_ENCODED_EMAIL, null);
                    spe.putString(Constants.KEY_PROVIDER, null);

                }
            }
        };
        mFirebase.addAuthStateListener(getMyAuthStateListener);


    }

    public void initializeViews() {
        userEmail = (TextInputEditText) findViewById(R.id.user_email_input);
        password = (TextInputEditText) findViewById(R.id.user_pass_input);
        signIn = (Button) findViewById(R.id.sign_in_button);
        googleSignInBtn = (SignInButton) findViewById(R.id.login_with_google);
        signUp = (TextView) findViewById(R.id.tv_sign_in);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSignInPressed();
            }
        });
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogleButton();
            }
        });

    }

    public void onSignInPressed() {
        Log.d("LoginActivity", "google login pressed");
        authenticationDialog.show();
        String email = userEmail.getText().toString();
        String pass = password.getText().toString();
        mFirebase.authWithPassword(email, pass, new MyAuthResultHandler("password"));
    }

    public void onSignUpPressed() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(i);
            }
        });

    }

    public void setupGoogleSignIn() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    public void signInWithGoogleButton() {
        Log.d("LoginActivity", "google login pressed");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
        authenticationDialog.show();

    }

    public void signInWithGoogle(String token) {
        mFirebase.authWithOAuthToken("google", token, new MyAuthResultHandler("google"));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        authenticationDialog.dismiss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Log.d("LoginActivity", "google login success");

                googleAccount = result.getSignInAccount();
                getGoogleAuthToken();
            } else {
                if (result.getStatus().getStatusCode() == GoogleSignInStatusCodes.SIGN_IN_CANCELLED) {
//                    Toast.makeText(LoginActivity.this, "The sign in was cancelled. Make sure you're connected to the internet and try again.", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(LoginActivity.this, "Error handling the sign in: " + result.getStatus().getStatusMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private class MyAuthResultHandler implements Firebase.AuthResultHandler {
        private final String provider;

        public MyAuthResultHandler(String provider) {
            this.provider = provider;
        }


        @Override
        public void onAuthenticated(AuthData authData) {
            if (authData != null) {
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor spe = sp.edit();

                final String emailId;
                if (authData.getProvider().equals(Constants.PASSWORD_PROVIDER)) {
                    emailId = authData.getProviderData().get(Constants.FIREBASE_PROPERTY_EMAIL).toString().toLowerCase();
                    updatedEmailId = Constants.updateEmail(emailId);
                } else if (authData.getProvider().equals(Constants.GOOGLE_PROVIDER)) {


                    if (googleApiClient.isConnected()) {
                        emailId = googleAccount.getEmail().toLowerCase();
                        spe.putString(Constants.GOOGLE_ID, emailId).apply();
                    } else {
                        emailId = sp.getString(Constants.GOOGLE_ID, null);

                    }
                    updatedEmailId = Constants.updateEmail(emailId);


                    final String userName = (String) authData.getProviderData().get("displayName");

                    final Firebase usersRef = new Firebase(Constants.FIREBASE_USER_URL).child(updatedEmailId);
                    usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                HashMap<String, Object> timeJoined = new HashMap<String, Object>();
                                timeJoined.put(Constants.FIREBASE_TIMESTAMP_PROPERTY, ServerValue.TIMESTAMP);

                                UserModel newUser = new UserModel(userName, updatedEmailId, timeJoined);
                                usersRef.setValue(newUser);
                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });

                }
                spe.putString(Constants.KEY_PROVIDER, authData.getProvider()).apply();
                spe.putString(Constants.KEY_ENCODED_EMAIL, updatedEmailId).apply();
                spe.commit();

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                authenticationDialog.dismiss();
                finish();
            }
        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            userEmail.setError(firebaseError.getMessage());
            authenticationDialog.dismiss();
        }
    }


    private void getGoogleAuthToken() {
        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            String mErrorMessage = null;

            @Override
            protected String doInBackground(Void... params) {
                String token = null;

                try {
                    String scope = "oauth2:profile email";

                    token = GoogleAuthUtil.getToken(LoginActivity.this, googleAccount.getEmail(), scope);
                } catch (IOException transientEx) {
                    /* Network or server error */
//                    Log.e(LOG_TAG, getString(R.string.google_error_auth_with_google) + transientEx);
//                    mErrorMessage = getString(R.string.google_error_network_error) + transientEx.getMessage();
                } catch (UserRecoverableAuthException e) {
//                    Log.w(LOG_TAG, getString(R.string.google_error_recoverable_oauth_error) + e.toString());

                    /* We probably need to ask for permissions, so start the intent if there is none pending */
                    if (!googleIntentInProgress) {
                        googleIntentInProgress = true;
                        Intent recover = e.getIntent();
                        startActivityForResult(recover, RC_SIGN_IN);
                    }
                } catch (GoogleAuthException authEx) {
                    /* The call is not ever expected to succeed assuming you have already verified that
                     * Google Play services is installed. */
//                    Log.e(LOG_TAG, " " + authEx.getMessage(), authEx);
//                    mErrorMessage = getString(R.string.google_error_auth_with_google) + authEx.getMessage();
                }
                return token;
            }

            @Override
            protected void onPostExecute(String token) {
                authenticationDialog.dismiss();
                if (token != null) {
                    /* Successfully got OAuth token, now login with Google */
                    signInWithGoogle(token);
                } else if (mErrorMessage != null) {
                    Toast.makeText(LoginActivity.this, mErrorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        };

        task.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFirebase.removeAuthStateListener(getMyAuthStateListener);
    }
}

