package com.example.covid19;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
    CountryListAdapter adapter;
    ProgressDialog p;

    EditText txtIn1;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private String binarySearching(JSONArray arr, String inputText, String regionType, String regionIDType) throws JSONException {
        int lb = 0, ub = arr.length() - 1;
        while (lb <= ub) {
            int mid = (lb + ub) / 2;
            JSONObject jsonPart = arr.getJSONObject(mid);
            int res = inputText.compareToIgnoreCase(jsonPart.getString(regionType));
            Log.i("sourav", String.valueOf(res));
            if (res == 0) {
                Log.i(regionType + " id ", jsonPart.getString(regionIDType));
                Toast.makeText(DashBoard.this, "Id of" + jsonPart.getString(regionType) + "is" + jsonPart.getString(regionIDType), Toast.LENGTH_LONG).show();
                String id;

                //checks if searching in district api or state api
                if (regionIDType.equalsIgnoreCase("district_id")) {
                    id = String.valueOf(jsonPart.getInt(regionIDType));
                } else {
                    id = jsonPart.getString(regionIDType);
                }
                Log.i("sourav", id);
                return id;
            } else if (res > 0)
                lb = mid + 1;
            else
                ub = mid - 1;
        }
        return null;
    }

    public class TrackingDataDwnld extends AsyncTask<String, String, String> {

        int switchctrl = 1;
        ArrayList<CountryListData> myListData = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(DashBoard.this);
            p.setMessage("Setting up data, Please wait...");
            p.setIndeterminate(false);
            p.setCancelable(false);
            p.show();
        }

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
                String populationInString = String.valueOf(jsonObject.getLong("population"));
                String casesInString = String.valueOf(jsonObject.getLong("cases"));
                String activeCaseInString = String.valueOf(jsonObject.getLong("active"));
                String criticalCaseInString = String.valueOf(jsonObject.getLong("critical"));
                String recoveredInString = String.valueOf(jsonObject.getLong("recovered"));
                String deathsInString = String.valueOf(jsonObject.getLong("deaths"));
                String noOfCountriesInString = String.valueOf(jsonObject.getInt("affectedCountries"));
                if (!populationInString.equals("") && !casesInString.equals("") && !activeCaseInString.equals("") && !criticalCaseInString.equals("")
                        && !recoveredInString.equals("") && !deathsInString.equals("") && !noOfCountriesInString.equals("")) {

                    myListData.add(new CountryListData("World", populationInString, casesInString, activeCaseInString, criticalCaseInString, recoveredInString
                            , deathsInString, noOfCountriesInString));
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
                String populationInString = String.valueOf(jsonPart.getLong("population"));
                String casesInString = String.valueOf(jsonPart.getLong("cases"));
                String activeCaseInString = String.valueOf(jsonPart.getLong("active"));
                String criticalCaseInString = String.valueOf(jsonPart.getLong("critical"));
                String recoveredInString = String.valueOf(jsonPart.getLong("recovered"));
                String deathsInString = String.valueOf(jsonPart.getLong("deaths"));

                if (!countryName.equals("") && !populationInString.equals("") && !casesInString.equals("") && !activeCaseInString.equals("") && !criticalCaseInString.equals("")
                        && !recoveredInString.equals("") && !deathsInString.equals("")) {
                    myListData.add(new CountryListData(countryName, populationInString, casesInString, activeCaseInString, criticalCaseInString, recoveredInString
                            , deathsInString));
                }
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (myListData != null) {
                p.hide();
                adapter = new CountryListAdapter(getApplicationContext(), myListData);
                recyclerView.setAdapter(adapter);
            } else {
                p.show();
            }

        }

    }

    EditText txtIn2;

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

        TrackingDataDwnld task = new TrackingDataDwnld();
        //API of disease.sh
        task.execute("https://disease.sh/v2/all", "https://disease.sh/v3/covid-19/countries");
        tracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView;
                tracker.setCardBackgroundColor(getResources().getColor(R.color.white));
                textView = findViewById(R.id.tracktxtbtn);
                textView.setTextColor(getResources().getColor(R.color.black));

                vaccine.setCardBackgroundColor(getResources().getColor(R.color.black));
                textView = findViewById(R.id.vactxtbtn);
                textView.setTextColor(getResources().getColor(R.color.white));

                findViewById(R.id.categoryLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.vacLayout).setVisibility(View.INVISIBLE);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        //AutoCompleteTextView states = findViewById(R.id.txtIn1);
        //AutoCompleteTextView dist = findViewById(R.id.txtIn2);


        vaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView;
                tracker.setCardBackgroundColor(getResources().getColor(R.color.black));
                textView = findViewById(R.id.tracktxtbtn);
                textView.setTextColor(getResources().getColor(R.color.white));

                vaccine.setCardBackgroundColor(getResources().getColor(R.color.white));
                textView = findViewById(R.id.vactxtbtn);
                textView.setTextColor(getResources().getColor(R.color.black));

                vaccine.setCardBackgroundColor(getResources().getColor(R.color.white));
                findViewById(R.id.vacLayout).setVisibility(View.VISIBLE);
                findViewById(R.id.categoryLayout).setVisibility(View.INVISIBLE);
            }
        });

        findViewById(R.id.searchVax).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StateDataDwnld().execute("https://cdn-api.co-vin.in/api/v2/admin/location/states");
            }
        });
    }

    public class StateDataDwnld extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            result.delete(0, urls.length);
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result.append(current);
                    data = reader.read();
                }

                return result.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("status 1", "inside vaccine post");
            try {
                JSONObject jsonObject = new JSONObject(s);

                String statesInfo = jsonObject.getString("states");

                Log.i("State content", statesInfo);

                JSONArray arr = new JSONArray(statesInfo);

                txtIn1 = findViewById(R.id.txtIn1);
                txtIn1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        txtIn2.getText().clear();
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                //binary searching
                String resultID = binarySearching(arr, txtIn1.getText().toString(), "state_name", "state_id");
                Log.i("Sourav", resultID);
                new DistDataDwnld().execute("https://cdn-api.co-vin.in/api/v2/admin/location/districts/" + resultID);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public class DistDataDwnld extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            result.delete(0, urls.length);
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result.append(current);
                    data = reader.read();
                }

                return result.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                String distInfo = jsonObject.getString("districts");

                Log.i("Dist content", distInfo);

                JSONArray arr = new JSONArray(distInfo);
                txtIn2 = findViewById(R.id.txtIn2);
                //binary searching
                String resultID = binarySearching(arr, txtIn2.getText().toString(), "district_name", "district_id");

                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("district_id", resultID);
                intent.putExtra("state_name", txtIn1.getText().toString());
                intent.putExtra("dist_name", txtIn2.getText().toString());
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
