package com.example.kennu76.tripadvice.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.kennu76.tripadvice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity {
    Button btnOk;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initInstance();

    }

    public void initInstance() {
        btnOk = findViewById(R.id.searchButton);
        btnOk.setOnClickListener(new View.OnClickListener() {
            EditText editTravelSum = findViewById(R.id.editTravelSum);
            SearchView searchView = findViewById(R.id.destinationSearch);

            @Override
            public void onClick(View v) {
                disableSslVerification();
                String url = "http://partners.api.skyscanner.net/apiservices/browseroutes/v1.0/US" +
                        "/EUR/en-GB/TLL/anywhere/anytime/anytime?apiKey=prtl6749387986743898559646983194";
                URL obj;
                HttpURLConnection con = null;

                try {
                    obj = new URL(url);
                    con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("GET");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                con.setRequestProperty("Accept", "application/json");

                //add request header
                BufferedReader in;
                StringBuilder response = new StringBuilder();
                String inputLine;
                try {
                    in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //print result
                String response_string = response.toString();
                JSONObject json;
                JSONArray quotes = null;
                JSONArray places = null;
                try {
                    json = new JSONObject(response_string);
                    quotes = (JSONArray) json.get("Quotes");
                    places = (JSONArray) json.get("Places");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                int budget = Integer.parseInt(editTravelSum.getText().toString());
                ArrayList place_id = new ArrayList();
                ArrayList place_name = new ArrayList();
                int y = places.length();
                String lastel = new String();
                for (int i = 0; i < y; i++) {
                    int place_id_one = 0;
                    try {
                        place_id_one = Integer.parseInt(places.get(i).toString().split(",")[0].split(":")[1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    place_id.add(place_id_one);
                    String place_name_one = null;

                    try {
                        if (places.get(i).toString().split(",").length < 5) {
                            place_name_one = places.get(i).toString().split(",")[0].split(":")[1];
                            place_name_one = place_name_one.replace("\"", "");
                            place_name_one = place_name_one.replace("}", "");
                        } else {
                            place_name_one = places.get(i).toString().split(",")[7].split(":")[1];
                            place_name_one = place_name_one.replace("\"", "");
                            place_name_one = place_name_one.replace("}", "");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    place_name.add(place_name_one);


                }
                try {
                    System.out.println(places.get(180).toString().split(",")[5]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList cool_places = new ArrayList();
                int x = quotes.length();
                for (int i = 0; i < x; i++) {
                    int quote = 0;
                    try {
                        quote = Integer.parseInt(quotes.get(i).toString().split("MinPrice")[1].split(",")[0].split(":")[1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (quote < budget) {
                        //cool_places.add(i);
                        try {
                            System.out.println(quotes.get(i).toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int dest_id = 0;
                        try {
                            dest_id = Integer.parseInt(quotes.get(i).toString().split("DestinationId")[1].split(",")[0].split(":")[1]);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        int koha_id = place_id.indexOf(dest_id);
                        //System.out.println(place_name.get(koha_id));
                        if ((lastel).equals(place_name.get(koha_id))) {

                        } else {
                            cool_places.add(place_name.get(koha_id));
                        }
                        lastel = (String) place_name.get(koha_id);
                    }

                }
                try {
                    System.out.println(quotes.get(4).toString().split("MinPrice")[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                System.out.println("Lahedad kohad: " + cool_places.toString());
                Log.v("COOLPLACES: ", cool_places.toString());
                StringBuffer coolplaces = new StringBuffer();

                //Toast.makeText(getApplicationContext(),cool_places.toString(), Toast.LENGTH_LONG).show();
                for (int i = 0; i < cool_places.size(); i++) {
                    coolplaces.append((String) cool_places.get(i));
                    coolplaces.append(",");
                    //listAdapter.add((String) cool_places.get(i));
                }
                String coolplaces2 = coolplaces.toString();
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("cool_places", coolplaces2);
                //ArrayList routes = new ArrayList();

                startActivityForResult(intent, REQUEST_CODE);
            }


            private void disableSslVerification() {
                try {
                    // Create a trust manager that does not validate certificate chains
                    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
                    };

                    // Install the all-trusting trust manager
                    SSLContext sc = SSLContext.getInstance("SSL");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

                    // Create all-trusting host name verifier
                    HostnameVerifier allHostsValid = new HostnameVerifier() {
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    };

                    // Install the all-trusting host verifier
                    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                }
            }


        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("Name");
                Toast.makeText(getApplicationContext(), "Your email has been sent " + result, Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) throws JSONException {
        //launch(args);
    }

}