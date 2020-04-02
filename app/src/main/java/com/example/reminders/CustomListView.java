package com.example.reminders;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomListView extends ArrayAdapter<String> {
    @NonNull

    private ArrayList<String> data;
    private ArrayList<Integer> photos;
    private Activity context;

    public CustomListView(@NonNull Activity context, ArrayList<String> data, ArrayList<Integer> photos) {
        super(context, R.layout.single_row,data);
        this.context = context;
        this.data = data;
        this.photos = photos;


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View r = convertView;
        ViewHolder viewholder = null;

        if ( r== null ){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.single_row, null);
            viewholder = new ViewHolder(r);
            r.setTag(viewholder);
        }
        else {
            viewholder = (ViewHolder) r.getTag();

        }
        viewholder.redOrGreen.setImageResource(photos.get(position));
        viewholder.reminder.setText(data.get(position));

        return r;
    }

    class ViewHolder {
        TextView reminder;
        ImageView redOrGreen;

        ViewHolder(View v){
            reminder = (TextView) v.findViewById(R.id.row_textview);
            redOrGreen = (ImageView) v.findViewById(R.id.row_imageview);
        }
    }
}
