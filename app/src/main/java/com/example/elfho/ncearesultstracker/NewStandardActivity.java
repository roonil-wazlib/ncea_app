package com.example.elfho.ncearesultstracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by elfho on 20/12/2017.
 */
public class NewStandardActivity extends Activity {

    private Button savebtn;
    private EditText enteredStandard;
    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newstandard);
        myDb = new DatabaseHelper(this);
        savebtn = (Button)findViewById(R.id.savebtn);
        enteredStandard = (EditText)findViewById(R.id.enteredStandard);
    }

    public void onClick(View view) {

        if (view.getId() == R.id.savebtn) {

            Bundle extras = getIntent().getExtras();
            int subjectId = extras.getInt("id");
            String subjectId_str = Integer.toString(subjectId);
            String subject = extras.getString("subject");
            int level = extras.getInt("level");

            Intent i = new Intent(NewStandardActivity.this, NewStandardDetailsActivity.class);
            i.putExtra("id", subjectId_str);
            i.putExtra("standard", enteredStandard.getText().toString());
            i.putExtra("subject", subject);
            i.putExtra("level", level);
            startActivity(i);
            overridePendingTransition(0, 0);


        }
    }
}