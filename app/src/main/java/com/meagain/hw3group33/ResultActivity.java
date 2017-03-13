package com.meagain.hw3group33;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;

/*
*
* Megan Reiffer, Molly-Marie Frye
*/
public class ResultActivity extends AppCompatActivity {

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        int[] listCount = getIntent().getExtras().getIntArray(MainActivity.INTEGER_LIST);
        String[] stringList = getIntent().getExtras().getStringArray(MainActivity.STRING_LIST);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.resultsLinearLayout);
        for (int i = 0; i < stringList.length; i++) {
            TextView textView = new TextView(this);
            textView.setText(stringList[i] + ": "+listCount[i]);
            linearLayout.addView(textView);
        }
    }


    public void finishClicked(View view) {

        finish();
    }


}
