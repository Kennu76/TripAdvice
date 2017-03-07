package com.example.kennu76.tripadvice;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


public class MainActivity extends AppCompatActivity {
    Button btnOk;
    Button btn2;
    private int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initInstance();

    }
    public void initInstance(){
        final ListView mainList ;
        EditText editText;
        final ArrayAdapter<String> listAdapter;
        mainList = (ListView) findViewById( R.id.mainList);
        editText = (EditText) findViewById(R.id.editText);
        btnOk = (Button) findViewById(R.id.btnOk);
        ArrayList places_empty = null;
        //listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, places_empty);
        //mainList.setAdapter(listAdapter);
        btnOk.setOnClickListener(new View.OnClickListener(){
            EditText editText = (EditText) findViewById(R.id.editText);

            @Override
            public void onClick(View v) {

                String name = editText.getText().toString();

                //intent.putExtra("Name",name);

                //startActivityForResult(intent,REQUEST_CODE);
                disableSslVerification();
                ArrayList<String> countries = new ArrayList<String>();




                String url = "http://partners.api.skyscanner.net/apiservices/browseroutes/v1.0/US/EUR/en-GB/TLL/anywhere/anytime/anytime?apiKey=prtl6749387986743898559646983194";
                URL obj = null;
                try {
                    obj = new URL(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpURLConnection con = null;
                try {
                    con = (HttpURLConnection) obj.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // optional default is GET
                try {
                    con.setRequestMethod("GET");
                } catch (ProtocolException e) {
                    e.printStackTrace();
                }
                con.setRequestProperty("Accept","application/json");

                //add request header

                int responseCode = 0;
                try {
                    responseCode = con.getResponseCode();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("\nSending 'GET' request to URL : " + url);
                System.out.println("Response Code : " + responseCode);

                BufferedReader in = null;
                try {
                    in = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String inputLine;
                StringBuffer response = new StringBuffer();

                try {
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //print result
                String response_string = response.toString();
                JSONObject json = null;
                try {
                    json = new JSONObject(response_string.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray quotes = null;
                try {
                    quotes = (JSONArray) json.get("Quotes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray places = null;
                try {
                    places = (JSONArray) json.get("Places");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println("proov");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                int budget = Integer.parseInt(editText.getText().toString());
                ArrayList place_id = new ArrayList();
                ArrayList place_name = new ArrayList();
                int y = places.length();
                String lastel = new String();
                for(int i=0;i<y;i++){
                    int place_id_one = 0;
                    try {
                        place_id_one = Integer.parseInt(places.get(i).toString().split(",")[0].split(":")[1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    place_id.add(place_id_one);
                    String place_name_one = null;

                    try {
                        if(places.get(i).toString().split(",").length < 5) {
                            place_name_one = places.get(i).toString().split(",")[0].split(":")[1];
                            place_name_one = place_name_one.replace("\"", "");
                            place_name_one = place_name_one.replace("}", "");
                        }
                        else
                        {
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
                    System.out.println( places.get(180).toString().split(",")[5]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayList cool_places = new ArrayList();
                int x = quotes.length();
                for(int i=0;i<x;i++){
                    int quote = 0;
                    try {
                        quote = Integer.parseInt(quotes.get(i).toString().split("MinPrice")[1].split(",")[0].split(":")[1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (quote < budget){
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
                        if((lastel).equals(place_name.get(koha_id))){

                        }
                        else{
                            cool_places.add(place_name.get(koha_id));
                        }
                        lastel = (String) place_name.get(koha_id);
                    }

                }
                //for(int i=0;i<cool_places.size();i++){
                //    System.out.println(quotes.get((Integer) cool_places.get(i)).toString());
                //}
                //String quote = (String) quotes.get(1);
                //System.out.println(Integer.parseInt(quotes.get(4).toString().split("DestinationId")[1].split("}")[0].split(":")[1]));
                try {
                    System.out.println(quotes.get(4).toString().split("MinPrice")[1]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                List cool_places2 = (List) cool_places;
                //cool_places2 = (List) cool_places2.stream().distinct().collect(Collectors.toList());
                System.out.println("Lahedad kohad: " + cool_places.toString());
                Log.v("COOLPLACES: ", cool_places.toString());
                StringBuffer coolplaces = new StringBuffer();
                
                //Toast.makeText(getApplicationContext(),cool_places.toString(), Toast.LENGTH_LONG).show();
                for(int i=0;i< cool_places.size() ;i++){
                    coolplaces.append((String) cool_places.get(i));
                    coolplaces.append(",");
                    //listAdapter.add((String) cool_places.get(i));
                }
                String coolplaces2 = coolplaces.toString();
                Intent intent = new Intent(getApplicationContext(),ListActivity.class);
                intent.putExtra("cool_places",coolplaces2);
                //ArrayList routes = new ArrayList();

                startActivityForResult(intent,REQUEST_CODE);
            }



            private void disableSslVerification() {
                try
                {
                    // Create a trust manager that does not validate certificate chains
                    TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
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


    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==REQUEST_CODE){
            if(resultCode==RESULT_OK){
                String result = data.getStringExtra("Name");
                Toast.makeText(getApplicationContext(),"Your email has been sent "+ result, Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) throws JSONException {
        //launch(args);
    }


    private static void disableSslVerification() {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
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

}