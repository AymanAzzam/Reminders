package com.example.reminders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    String [] items = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
    Integer [] ids = {R.drawable.green,R.drawable.green,R.drawable.green,R.drawable.green,R.drawable.green,R.drawable.green,R.drawable.green};
    ListView listView;
    ArrayList<String> data;
    ArrayList<Integer> photos;
    ArrayAdapter <String> arrayAdapter;
    CustomListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)(findViewById(R.id.main_listview));
        data = new ArrayList<>(Arrays.asList(items));
        photos = new ArrayList<>(Arrays.asList(ids));
        customListView = new CustomListView(this,data,photos);

        //arrayAdapter = new ArrayAdapter<String>(this, R.layout.single_row,R.id.row_textview,data);
        listView.setAdapter(customListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditReminderBox(data.get(position), position);
            }
        });


    }

    public void showEditReminderBox(String oldItem, final int index)
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Edit Reminder");
        dialog.setContentView(R.layout.activity_dialog_box);

        final EditText editText = (EditText)dialog.findViewById(R.id.edit_text);
        editText.setText(oldItem);

        Button bt_edit = (Button) dialog.findViewById(R.id.edit_button);
        bt_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.set(index, editText.getText().toString());
                customListView.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        final Button bt_cancel = (Button) dialog.findViewById(R.id.cancel_button);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        final CheckBox cb = (CheckBox) dialog.findViewById(R.id.important_checkbox);
        final ImageView img = (ImageView) findViewById(R.id.row_imageview);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //img.setImageResource(R.drawable.red);
                    photos.set(index, R.drawable.red);
                    customListView.notifyDataSetChanged();
                    buttonView.setChecked(true);
                }

                else {
                    //img.setImageResource(R.drawable.green);
                    photos.set(index , R.drawable.green);
                    customListView.notifyDataSetChanged();
                    buttonView.setChecked(false);
                }
            }
        });


    }

    public void showAddReminderBox () {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setTitle("Add Reminder");
        dialog.setContentView(R.layout.activity_dialog_box);

        final EditText editText = (EditText)dialog.findViewById(R.id.edit_text);


        Button bt_add = (Button) dialog.findViewById(R.id.edit_button);
        bt_add.setText("Add");
        final int[] index = new int[1];
        index[0] = data.size();

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.add(editText.getText().toString());
                //photos.add(R.drawable.green);
                customListView.notifyDataSetChanged();
                //index[0] = data.size();
                dialog.dismiss();
            }
        });

        final Button bt_cancel = (Button) dialog.findViewById(R.id.cancel_button);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

        final CheckBox cb = (CheckBox) dialog.findViewById(R.id.important_checkbox);
        final ImageView img = (ImageView) findViewById(R.id.row_imageview);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //img.setImageResource(R.drawable.red);
                    photos.add(R.drawable.green);
                    photos.set(index[0], R.drawable.red);
                    customListView.notifyDataSetChanged();
                    buttonView.setChecked(true);
                }

                else {
                    //img.setImageResource(R.drawable.green);
                    photos.add(R.drawable.green);
                    photos.set(index[0], R.drawable.green);
                    customListView.notifyDataSetChanged();
                    buttonView.setChecked(false);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch(id){
            case R.id.add_reminder:
                showAddReminderBox();
                break;
            case R.id.exit:
                finish();
                System.exit(1);
                break;
        }
        return true;
    }
}
