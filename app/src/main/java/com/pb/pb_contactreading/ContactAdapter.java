package com.pb.pb_contactreading;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<Contact> {
    Context context;
    AllContactActivity activity;
    public ContactAdapter(Context context, ArrayList<Contact> contact, AllContactActivity activity) {
        super(context, 0, contact);
        this.context = context;
        this.activity = activity;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Contact c = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_lists, parent, false);

        }
        final LinearLayout ll_layout = convertView.findViewById(R.id.ll_layout);
        TextView txt_name = convertView.findViewById(R.id.txt_name);
        TextView txt_number = convertView.findViewById(R.id.txt_phone);
        TextView txt_add = convertView.findViewById(R.id.txt_address);
        txt_name.setText(c.getName());
        txt_number.setText(c.getNumber());
        txt_add.setText(c.getAdd());
        ll_layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation jumAni = AnimationUtils.loadAnimation(context, R.anim.anim);
                view.startAnimation(jumAni);

                return false;
            }
        });
        ll_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(activity!=null && !c.getNumber().equals("---")){
                if(activity.conCont(position)){
                    ll_layout.setBackgroundColor(context.getResources().getColor(R.color.default_color));
                    activity.remCont(position);
                } else{
                    ll_layout.setBackgroundColor(context.getResources().getColor(R.color.pressed_color));
                    activity.addCont(position);
                }}
                return false;
            }
        });

        if(activity!=null && activity.conCont(position)){
            ll_layout.setBackgroundColor(context.getResources().getColor(R.color.pressed_color));
        } else{
            ll_layout.setBackgroundColor(context.getResources().getColor(R.color.default_color));
        }
        return convertView;

    }
}
