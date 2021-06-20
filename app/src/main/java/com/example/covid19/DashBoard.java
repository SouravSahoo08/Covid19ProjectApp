package com.example.covid19;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DashBoard extends AppCompatActivity {
    Button signOutBtn;
    GoogleSignInClient mGoogleSignInClient;
    ImageView imageView;
    TextView name;
    CardView tracker, vaccine;
    RecyclerView recyclerView;
    String statText;
    CountryListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        signOutBtn = findViewById(R.id.signout);
        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.Name);
        tracker = findViewById(R.id.track);
        vaccine = findViewById(R.id.vac);
        recyclerView = findViewById(R.id.countryRecycleView);
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            name.setText(personName);
            Glide.with(this).load(personPhoto).into(imageView);
        }

        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.signout:
                        signOut();
                        break;
                }
            }
        });

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTask task = new DownloadTask();
                //API of disease.sh
                task.execute("https://disease.sh/v2/all", "https://disease.sh/v3/covid-19/countries");
                recyclerView.setVisibility(View.VISIBLE);

            }
        });

        vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaccine.setMinimumWidth(140);
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }


    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(DashBoard.this, "Successfully signed out", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    public class DownloadTask extends AsyncTask<String, String, String> {

        int switchctrl = 1;
        ArrayList<CountryListData> myListData = new ArrayList<>();

        @SuppressLint({"SetTextI18n", "WrongThread"})
        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            URL url;
            Log.i("url count", String.valueOf(urls.length));
            try {
                for (String s : urls) {
                    Log.i("url", s);
                    result.delete(0, result.length());
                    Log.i("background res next", result.toString());
                    url = new URL(s);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(in);
                    int data = reader.read();

                    while (data != -1) {
                        char current = (char) data;
                        result.append(current);
                        data = reader.read();
                    }
                    Log.i("background res", result.toString());
                    publishProgress(result.toString());
                    Log.i("status", "publishProgress called");

                }
                return null;

            } catch (Exception e) {
                e.printStackTrace();

                return null;
            }
        }

        @Override
        protected void onProgressUpdate(String... s) {
            super.onProgressUpdate(s);

            Log.i("progress update", s[0]);
            //interpreting JSON Object and JSON Arrays


            if (switchctrl == 1) {
                worldJSON(s[0]);
            } else {
                try {
                    countryJSON(s[0]);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        }

        private void worldJSON(String json) {
            switchctrl = 2;
            try {
                JSONObject jsonObject = new JSONObject(json);
                String casesInString = String.valueOf(jsonObject.getLong("cases"));
                if (!casesInString.equals("")) {
                    /*myListData = new CountryListData[]{
                            new CountryListData("World", casesInString)
                    };*/
                    myListData.add(new CountryListData("World", casesInString));
                    adapter = new CountryListAdapter(getApplicationContext(), myListData);
                    recyclerView.setAdapter(adapter);
                    //Log.i("status",adapter.toString());
                }
            } catch (JSONException exception) {

                exception.printStackTrace();
            }
        }

        private void countryJSON(String json) throws JSONException {
            switchctrl = 1;
            JSONArray chkArr = new JSONArray(json);
            //myListData = new CountryListData[chkArr.length()];
            for (int i = 0; i < chkArr.length(); i++) {
                JSONObject jsonPart = chkArr.getJSONObject(i);
                String countryName = jsonPart.getString("country");
                String cases = String.valueOf(jsonPart.getLong("cases"));

                if (!countryName.equals("") && !cases.equals("")) {
                    myListData.add(new CountryListData(countryName, cases));
                    adapter = new CountryListAdapter(getApplicationContext(), myListData);
                    recyclerView.setAdapter(adapter);
                }
            }

        }

        /*@Override
        protected void onPreExecute() {
            super.onPreExecute();

        }*/
    }
}
