package com.example.kennu76.tripadvice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        Button button;
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        Intent intent = getIntent();
        final ListView mainList ;
        //ArrayAdapter<String> listAdapter;
        mainList = (ListView) findViewById( R.id.uuslist);
        ArrayList<String> places_empty = new ArrayList<>();
        //listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, places_empty);
        //.setAdapter(listAdapter);
        String name = intent.getStringExtra("cool_places");
        System.out.println("asi: " +name.toString());
        ArrayList cool_places = new ArrayList();
        for(int i=0;i< name.split(",").length ;i++){
            places_empty.add(name.split(",")[i].toString());
        }

        final ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, places_empty);
        mainList.setAdapter(listAdapter);
        int requestCode = 1;
        //textView2.setText("Thanks for the name " + name + " !");


        final int finalRequestCode = requestCode;
        button = (Button) findViewById(R.id.button2);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                String email = editText2.getText().toString();
                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                String[] to = new String[]{email};
                emailIntent.putExtra(Intent.EXTRA_EMAIL,to);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"App Message");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"Greetings from my awesome app!");
                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent,"Email"));

                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
