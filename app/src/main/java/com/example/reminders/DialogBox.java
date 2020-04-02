package com.example.reminders;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class DialogBox extends AppCompatActivity {

    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_box);

        textview= (TextView)(findViewById(R.id.edit_text));
        String tempHolder = getIntent().getStringExtra("Reminder_text");
        textview.setText(tempHolder);
    }
}
