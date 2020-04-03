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
                showAlertDialog(data.get(position), position);
                //showReminderBox(false,data.get(position), position);
            }
        });
    }

    public void showAlertDialog(final String item,final int index)
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_alert_dialog);

        final TextView textEdit = (TextView)dialog.findViewById(R.id.edit_reminder);
        final TextView textDelete = (TextView)dialog.findViewById(R.id.delete_reminder);

        textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showReminderBox(false,item,index);
            }
        });

        textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.remove(index);
                photos.remove(index);
                customListView.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void showReminderBox(final boolean add ,final String oldItem,final int index_temp)
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_dialog_box);

        final EditText editText = (EditText)dialog.findViewById(R.id.edit_text);
        final Button bt_cancel = (Button) dialog.findViewById(R.id.cancel_button);
        Button bt_task = (Button) dialog.findViewById(R.id.edit_button);
        final CheckBox cb = (CheckBox) dialog.findViewById(R.id.important_checkbox);
        final ImageView img = (ImageView) findViewById(R.id.row_imageview);
        final int index;


        if(!add)    {   index = index_temp;     editText.setText(oldItem);  }
        else        {   index = data.size();    bt_task.setText("Add");  }


        bt_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add) {
                    data.add(editText.getText().toString());
                    photos.add(R.drawable.green);
                    if(cb.isChecked())
                        photos.set(index, R.drawable.red);
                }
                else {
                    data.set(index, editText.getText().toString());
                    photos.set(index, R.drawable.green);
                    if(cb.isChecked())
                        photos.set(index, R.drawable.red);
                }
                customListView.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(add) {
                    data.add(editText.getText().toString());
                }
                dialog.dismiss();
            }
        });

        dialog.show();
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
                showReminderBox(true,null,0);
                break;
            case R.id.exit:
                finish();
                System.exit(1);
                break;
        }
        return true;
    }
}
