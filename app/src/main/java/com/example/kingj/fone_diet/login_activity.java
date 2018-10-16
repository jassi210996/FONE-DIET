package com.example.kingj.fone_diet;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class login_activity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference myRef;
    FirebaseDatabase database;
    private CallbackManager callbackManager;
    TextView textView;
    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    LoginButton loginButton;
    FirebaseAuth.AuthStateListener authStateListener;
    FacebookCallback<LoginResult> callback;
    private String TAG = "TaAAAg";
    Button viewDataButton;
    private AccessToken accessToken;

    SignInButton signInButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

         getSupportActionBar().hide();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.black));
        }

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        loginButton=findViewById(R.id.facebookLogIn);


        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        callbackManager= CallbackManager.Factory.create();

        loginButton.setReadPermissions("email","public_profile");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(login_activity.this,"Success",Toast.LENGTH_LONG).show();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Toast.makeText(login_activity.this,"Cancel",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(login_activity.this,"Error",Toast.LENGTH_LONG).show();
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser fUser = firebaseAuth.getCurrentUser();
                if(fUser!=null)
                {
//                    textView.setText(fUser.getDisplayName());
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"out",Toast.LENGTH_LONG).show();
                }
            }
        };

    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }

    private void updateUI(FirebaseUser currentProfile) {
        if(currentProfile!=null)
        {
            String name = currentProfile.getDisplayName();
            String email = currentProfile.getEmail();
            Uri photoUrl =  currentProfile.getPhotoUrl();

            addToFirebase(name,email,photoUrl);

//            textView.setText(""+currentProfile.getDisplayName());
        }

    }

    private void addToFirebase(String name, String email, Uri photoUrl) {

//        User user = new User();
//        String id = myRef.push().getKey();
//        user.setEmail(email);
//        user.setUsername(name);
//        user.setId(id);
//
////        user.setPhoctoUrl(photoUrl);
//
//        myRef.child(id).setValue(user);
        Toast.makeText(this,"Added",Toast.LENGTH_LONG).show();
    }


    private void handleFacebookAccessToken(AccessToken accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                    FirebaseUser user = mAuth.getCurrentUser();
//                    addData();
                    updateUI(user);
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.getException());
                    Toast.makeText(login_activity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });


    }

   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
