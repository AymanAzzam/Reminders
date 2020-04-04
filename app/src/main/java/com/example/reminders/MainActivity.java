package com.example.reminders;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {

    RemindersDbAdapter remindersDbAdapter;
    Cursor cursor = null;
    //String [] items = {"sunday", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday"};
    //Integer [] ids = {R.drawable.green,R.drawable.green,R.drawable.green,R.drawable.green,R.drawable.green,R.drawable.green,R.drawable.green};
    ListView listView;
    ArrayList<String> data;
    ArrayList<Integer> photos;
    ArrayList<Integer> indices;
    ArrayAdapter <String> arrayAdapter;
    CustomListView customListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String mContent;
        int mImportant;
        int mId;

        listView = (ListView)(findViewById(R.id.main_listview));
        data = new ArrayList<>();
        photos = new ArrayList<>();
        indices = new ArrayList<>();
        customListView = new CustomListView(this,data,photos,indices);
        remindersDbAdapter = new RemindersDbAdapter(this);

        try			{	remindersDbAdapter.open();	}
        catch(SQLException e) 	{	e.printStackTrace();		}

        //remindersDbAdapter.deleteAllReminders();
        cursor = remindersDbAdapter.fetchAllReminders();
        if(cursor.moveToFirst())
        {
            do {
                mContent = cursor.getString(cursor.getColumnIndex(remindersDbAdapter.COL_CONTENT));
                mImportant = cursor.getInt(cursor.getColumnIndex(remindersDbAdapter.COL_IMPORTANT));
                mId = cursor.getInt(cursor.getColumnIndex(remindersDbAdapter.COL_ID));
                data.add(mContent);
                indices.add(mId);
                if (mImportant == 0)
                    photos.add(R.drawable.green);
                else
                    photos.add(R.drawable.red);

            }
            while (cursor.moveToNext());
        }
        customListView.notifyDataSetChanged();

        //arrayAdapter = new ArrayAdapter<String>(this, R.layout.single_row,R.id.row_textview,data);
        listView.setAdapter(customListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showAlertDialog(position);
                //showReminderBox(false,data.get(position), position);
            }
        });
    }

    public void showAlertDialog(final int position)
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_alert_dialog);

        final TextView textEdit = (TextView)dialog.findViewById(R.id.edit_reminder);
        final TextView textDelete = (TextView)dialog.findViewById(R.id.delete_reminder);

        textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showReminderBox(false,position);
            }
        });

        textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
		    remindersDbAdapter.deleteReminderById(indices.get(position));
            data.remove(position);
            photos.remove(position);
            indices.remove(position);
            customListView.notifyDataSetChanged();
            dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void showReminderBox(final boolean add ,final int temp_position)
    {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_dialog_box);

	    final LinearLayout linearLayout = (LinearLayout)dialog.findViewById(R.id.dialog_box);
	    final TextView dialogType = (TextView)dialog.findViewById(R.id.dialog_type);
        final EditText editText = (EditText)dialog.findViewById(R.id.edit_text);
	    final Button bt_cancel = (Button) dialog.findViewById(R.id.cancel_button);
        Button bt_task = (Button) dialog.findViewById(R.id.edit_button);
        final CheckBox cb = (CheckBox) dialog.findViewById(R.id.important_checkbox);
        final ImageView img = (ImageView) findViewById(R.id.row_imageview);
        final int position;


        if(!add)
        {   
            position = temp_position;
            editText.setText(data.get(position));
            dialogType.setText(R.string.edit_reminder_type);
            dialogType.setBackgroundColor(getResources().getColor(R.color.blue));
            linearLayout.setBackgroundColor(getResources().getColor(R.color.blue));
        }
        else
        {
            position = 15;  //garbage
            bt_task.setText("Add");
            dialogType.setText(R.string.new_reminder_type);
            dialogType.setBackgroundColor(getResources().getColor(R.color.green));
            linearLayout.setBackgroundColor(getResources().getColor(R.color.green));
        }


        bt_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if(add)
            {
                long out;
                data.add(editText.getText().toString());
                photos.add(R.drawable.green);
                if(cb.isChecked())
		        {
                    photos.set(photos.size()-1, R.drawable.red);
                    out = remindersDbAdapter.createReminder(editText.getText().toString(), true);
		        }
		        else
		            out = remindersDbAdapter.createReminder(editText.getText().toString(), false);

		        if(out == -1)   System.out.println("Error When Creating Reminder on Database");
		        else            indices.add((int)out);
            }
            else
            {
                data.set(position, editText.getText().toString());
		        Reminder reminder;
                if(cb.isChecked())
		        {
                    photos.set(position, R.drawable.red);
			        reminder = new Reminder(indices.get(position),editText.getText().toString(), 1);
		        }
		        else
		        {
			        photos.set(position, R.drawable.green);
			        reminder = new Reminder(indices.get(position),editText.getText().toString(), 0);
		        }
		        remindersDbAdapter.updateReminder(reminder);
            }
            customListView.notifyDataSetChanged();
            dialog.dismiss();
            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
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
                showReminderBox(true,0);
                break;
            case R.id.exit:
                finish();
                System.exit(1);
                break;
        }
        return true;
    }
}
