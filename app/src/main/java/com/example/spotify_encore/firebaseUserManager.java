package com.example.spotify_encore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Handles operations related to user profiles using Firebase Realtime Database or Firestore.
// As well as overall App Management

public class firebaseUserManager extends AppCompatActivity {
    public static final String CLIENT_ID = "edd6322503f640c1a8514e901e175453";
    public static final String REDIRECT_URI = "com.example.spotifyencore://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;
    private ImageView profileImageView;
    private TextView userNameProfile;
    private TextView userLocationProfile;
    private AppCompatButton HomeButton;
    private AppCompatButton ViewSummaryButton;
    private AppCompatButton SpotifyInfromationButton;
    private AppCompatButton friendsButton;
    private Button AccountSettingsButton;


    FirebaseAuth auth;
    AppCompatButton signOut;
    FirebaseUser user;


    // These are only outline methods they can be changed if needed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), authentication.class);
            intent.putExtra("userAction", "LogIn");
            startActivity(intent);
            finish();
        }

        String action = getIntent().getStringExtra("userAction");

        if (action != null) {
            if (action.equals("accountSettings")) {
                setContentView(R.layout.account_setting);

                signOut = findViewById(R.id.signOutAccountSettings);
                signOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        auth.signOut();

                        Intent intent = new Intent(getApplicationContext(), authentication.class);
                        intent.putExtra("userAction", "LogIn");
                        startActivity(intent);
                        finish();
                    }
                });

            } else if (action.equals("homepage")) {
                setContentView(R.layout.homepage);

            } else if (action.equals("gamepage")) {
                setContentView(R.layout.game);
            }
        }

    }

    public void profileButtonClick(View view) {
        Intent sign = new Intent(this, firebaseUserManager.class);
        String authentication = "accountSettings";
        sign.putExtra("userAction", authentication);
        startActivity(sign);
    }

    /**
     * Get user profile
     * This method will get the user profile using the token
     */
    /*
    public void getUserProfile() {
        if (mAccessToken == null) {
            Toast.makeText(this, "You need to get an access token first!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a request to get the user profile
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("HTTP", "Failed to fetch data: " + e);
                Toast.makeText(authentication.this, "Failed to fetch data, watch Logcat for more details",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());
                    //setTextAsync(jsonObject.toString(3), profileTextView);
                } catch (JSONException e) {
                    Log.d("JSON", "Failed to parse data: " + e);
                    Toast.makeText(authentication.this, "Failed to parse data, watch Logcat for more details",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    */


    /**
     * Changes Info based on the account // subject to change(name, function, and parameters)
     *
     */
    private void changeInfo () {
        userNameProfile = findViewById(R.id.userNameProfile);
        userLocationProfile = findViewById(R.id.userLocationProfile);
        profileImageView = findViewById(R.id.profileImageView);
    }

    private void pressAccountSettingsButton() {
        AccountSettingsButton = findViewById(R.id.AccountSettingsButton);

    }


    private void pressHomeButton() {
        HomeButton = findViewById(R.id.HomeButton);

    }

    private void pressViewSummaryButton() {
        ViewSummaryButton = findViewById(R.id.ViewSummaryButton);

    }


    private void pressSpotifyInfromationButton() {
        SpotifyInfromationButton = findViewById(R.id.SpotifyInfromationButton);

    }

    private void pressfriendsButton() {
        AccountSettingsButton = findViewById(R.id.friendsButton);

    }




    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(firebaseUserManager.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-read-email" }) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }


}
