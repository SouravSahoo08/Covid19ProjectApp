package com.example.covid19;

import androidx.appcompat.app.AppCompatActivity;

import androidx.cardview.widget.CardView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginScreen extends AppCompatActivity {
    private RelativeLayout uiScreen;
    private CardView SigninCardButton;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 0;
    private ImageView logoImage;
    private ObjectAnimator yAnimation;
    private ObjectAnimator alphaAnimation;
    private AnimatorSet set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        uiScreen = findViewById(R.id.UI);
        SigninCardButton = findViewById(R.id.SigninCardButton);
        logoImage = findViewById(R.id.logoimageView);
        animateUI();

        SigninCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.SigninCardButton:
                        signIn();
                        break;
                }
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Intent intent = new Intent(getApplicationContext(), DashBoard.class);
            startActivity(intent);
        }
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            Intent intent = new Intent(getApplicationContext(), DashBoard.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("filter", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    final long duration = 2000;
    private void animateUI() {

        alphaAnimation = ObjectAnimator.ofFloat(uiScreen, View.ALPHA, 0.0f, 1.0f).setDuration(duration);
        alphaAnimation.start();
        // animate logo
        yAnimation = ObjectAnimator.ofFloat(logoImage, View.TRANSLATION_Y, -100f, 0f);
        yAnimation.setDuration(duration);
        alphaAnimation = ObjectAnimator.ofFloat(logoImage, View.ALPHA, 0.0f, 1.0f);
        alphaAnimation.setDuration(duration);
        set = new AnimatorSet();
        set.playTogether(yAnimation, alphaAnimation);
        set.start();
        //animate google btn
        yAnimation = ObjectAnimator.ofFloat(SigninCardButton, View.TRANSLATION_Y, 100f, 0f);
        yAnimation.setDuration(duration);
        alphaAnimation = ObjectAnimator.ofFloat(SigninCardButton, View.ALPHA, 0.0f, 1.0f);
        alphaAnimation.setDuration(duration);
        set = new AnimatorSet();
        set.playTogether(yAnimation, alphaAnimation);
        set.start();

    }
}