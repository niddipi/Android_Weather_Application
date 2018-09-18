package com.example.neelesh.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Arrays;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String[] message = intent.getStringArrayExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = (TextView) findViewById(R.id.textview);
        if(message == null)
        {
            textView.setText("Error:\nPlease enter zipcodes in mentioned format\n or Check Internet connection");
        }
        else {
            textView.setText(Arrays.toString(message).replaceAll("\\[|\\]", ""));
            textView.setMovementMethod(new ScrollingMovementMethod());
        }
    }
    public void return_main(View view)throws IOException {
        finish();
        System.exit(0);
    }
}
