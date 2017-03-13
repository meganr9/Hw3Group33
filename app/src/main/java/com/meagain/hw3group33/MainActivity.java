package com.meagain.hw3group33;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
/*
*
* Megan Reiffer, Molly-Marie Frye
*/
public class MainActivity extends AppCompatActivity {

    public static final String INTEGER_LIST = "INTEGER LIST";
    public static final String STRING_LIST = "STRING LIST";
    TableLayout tableLayout;
    private View.OnClickListener addNewRowClickListener;
    String[] keywordsList;
    int[] keywordsCountList;
    private Boolean isMatchCase;
    ProgressDialog progressDialog;
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tableLayout = (TableLayout) findViewById(R.id.tableKeywordsLayout);


        addNewRowClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FloatingActionButton) v).setImageResource(android.R.drawable.button_onoff_indicator_off);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tableLayout.removeView((View) v.getParent());
                    }
                });
                EditText txtview = new EditText(MainActivity.this);
                txtview.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
                FloatingActionButton button = new FloatingActionButton(MainActivity.this);
                button.setOnClickListener(addNewRowClickListener);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    button.setImageResource(android.R.drawable.ic_input_add);
                }

                TableRow tableRow = new TableRow(MainActivity.this);
                tableRow.setLayoutParams(new LinearLayoutCompat.LayoutParams(LinearLayoutCompat.LayoutParams.FILL_PARENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT));

                tableRow.addView(txtview);
                tableRow.addView(button);
                tableLayout.addView(tableRow);

            }
        };

        findViewById(R.id.floatingActionButton2).setOnClickListener(addNewRowClickListener);


    }

    public void search(View view) {
        keywordsList = new String[tableLayout.getChildCount()];
        boolean isNotFilled;

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMax(keywordsList.length);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Compute Progress");
        progressDialog.show();
        isMatchCase = ((CheckBox) findViewById(R.id.match)).isChecked();
        counter = 0;

        keywordsCountList = new int[tableLayout.getChildCount()];
        for (int i = 0; i < tableLayout.getChildCount(); i++) {
            keywordsList[i] = ((EditText) ((TableRow) tableLayout.getChildAt(i)).getChildAt(0)).getText().toString();
            System.out.println("Keyword"+keywordsList[i]);
            isNotFilled = keywordsList[i] == null || keywordsList[i].length() == 0;


            if (!isNotFilled) {
                new DoWork().execute(keywordsList[i]);
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Fill in all fields or delete them!", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public class DoWork extends AsyncTask<String, Void, Integer> {



        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected Integer doInBackground(String... params) {
            try {
                int answer = 0;
                String keyword = params[0];
                if (!isMatchCase) {
                    keyword = keyword.toLowerCase();
                }
                InputStreamReader inputStream = new InputStreamReader(getAssets().open("textfile.txt"));
                BufferedReader bf = new BufferedReader(inputStream);
                String line = "";
                while ((line = bf.readLine()) != null) {
                    if (!isMatchCase) {
                        line = line.toLowerCase();
                    }
                    int index = 0;
                    while (line.indexOf(keyword, index) != -1) {
                        index = line.indexOf(keyword, index) + keyword.length();
                        answer++;
                    }
                }
                return answer;

            } catch (IOException e) {
                e.printStackTrace();
            }

            return 0;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            keywordsCountList[counter] = integer;
            counter++;
            progressDialog.setProgress(counter);
            if (counter >= keywordsList.length-1) {
                progressDialog.dismiss();
                startNextActivity();

            }
            super.onPostExecute(integer);
        }
    }

    private void startNextActivity() {
        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra(INTEGER_LIST, keywordsCountList);
        intent.putExtra(STRING_LIST, keywordsList);
        startActivity(intent);


    }
}
